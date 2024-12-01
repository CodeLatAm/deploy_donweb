package com.abel.sistema_gestion.serviceimpl;

import com.abel.sistema_gestion.entity.Article;
import com.abel.sistema_gestion.entity.Category;
import com.abel.sistema_gestion.entity.Image;
import com.abel.sistema_gestion.entity.User;
import com.abel.sistema_gestion.exception.ProductUploadException;
import com.abel.sistema_gestion.serviceimpl.service.*;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Log4j2
@Service
public class ExcelServiceImpl implements ExcelService {

    private static final Logger log = LoggerFactory.getLogger(ExcelServiceImpl.class);
    private CategoryService categoryService;
    private ArticleService articleService;
    private UserService userService;
    private ImageService imageService;

    public ExcelServiceImpl(CategoryService categoryService, ArticleService articleService,
                            UserService userService, ImageService imageService) {
        this.categoryService = categoryService;
        this.articleService = articleService;
        this.userService = userService;
        this.imageService = imageService;
    }

    @Override
    public ByteArrayOutputStream generateExcelTemplate() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Productos");

        // Crear encabezados sin la columna ID
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Nombre");
        headerRow.createCell(1).setCellValue("Precio");
        headerRow.createCell(2).setCellValue("Cantidad");
        headerRow.createCell(3).setCellValue("Ganancia");
        headerRow.createCell(4).setCellValue("Descripción");
        headerRow.createCell(5).setCellValue("Nombre de la Imagen");
        headerRow.createCell(6).setCellValue("Categoría (ID)");

        // Agregar las categorías al desplegable (valores de ejemplo)
        String[] categories = {
                "1 (Almacén)", "2 (Bebidas)", "3 (Frescos)", "4 (Congelados)",
                "5 (Limpieza)", "6 (Perfumería)", "7 (Electro)", "8 (Textil)",
                "9 (Hogar)", "10 (Aire libre)"
        };

        // Crear un rango para aplicar el desplegable de categorías (columna 6)
        DataValidationHelper validationHelper = sheet.getDataValidationHelper();
        CellRangeAddressList addressList = new CellRangeAddressList(1, 1000, 6, 6); // Filas desde 1 hasta 1000 en la columna Categoría
        DataValidationConstraint constraint = validationHelper.createExplicitListConstraint(categories);
        DataValidation dataValidation = validationHelper.createValidation(constraint, addressList);
        dataValidation.setSuppressDropDownArrow(true);
        sheet.addValidationData(dataValidation);

        // Ajustar el ancho de las columnas
        for (int i = 0; i < 7; i++) {
            sheet.autoSizeColumn(i);
        }

        // Escribir el contenido a un ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream;
    }

    private Map<String, byte[]> extractImagesFromZip(MultipartFile zipFile) throws IOException {
        Map<String, byte[]> imageMap = new HashMap<>();
        ZipInputStream zis = new ZipInputStream(zipFile.getInputStream());
        ZipEntry entry;

        while ((entry = zis.getNextEntry()) != null) {
            if (!entry.isDirectory()) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = zis.read(buffer)) > 0) {
                    baos.write(buffer, 0, length);
                }
                // Almacenar la imagen usando solo el nombre del archivo sin la ruta completa
                String fileName = new File(entry.getName()).getName();
                imageMap.put(fileName, baos.toByteArray());
                log.info("Archivo encontrado en el ZIP: " + entry.getName());
            }
        }

        zis.close();
        return imageMap;
    }

    @Transactional
    @Override
    public void processProductUpload(Integer userId, MultipartFile excelFile, MultipartFile zipFile) {
        User user = userService.getUserByUserId(userId);
        List<String> errors = new ArrayList<>();  // Lista para acumular los errores
        // Verificar la cantidad actual de artículos del usuario
        Long existingArticleCount = articleService.getCountArticlesByUserId(userId);
        int maxArticlesAllowed = 50;
        try {
            // Procesar archivo Excel
            Workbook workbook = new XSSFWorkbook(excelFile.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            // Contar los artículos a crear
            int articlesToCreate = 0;
            // Procesar ZIP de imágenes
            Map<String, byte[]> images = extractImagesFromZip(zipFile);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Saltar encabezado

                String productName = row.getCell(0).getStringCellValue();

                // Validar si el artículo ya existe en la base de datos
                Optional<Article> existingArticle = articleService.findByNameAndUserId(productName, userId);
                if (existingArticle.isPresent()) {
                    errors.add("El artículo '" + productName + "' ya existe en la base de datos.");
                    continue; // Saltar a la siguiente fila si el artículo ya existe
                }
                // Verificar si se ha excedido el límite
                if (existingArticleCount + articlesToCreate >= maxArticlesAllowed) {
                    errors.add("El usuario ya tiene el número máximo permitido de artículos para el plan FREE.");
                    break; // Terminar el procesamiento si se excede el límite
                }

                // Crear nuevo artículo
                Article article = new Article();
                article.setName(productName);
                article.setPrice(row.getCell(1).getNumericCellValue());
                article.setQuantity((int) row.getCell(2).getNumericCellValue());
                article.setRevenue((int) row.getCell(3).getNumericCellValue());
                article.setDescription(row.getCell(4).getStringCellValue());

                // Asignar imágenes (basado en el nombre de la imagen en la columna 6)
                String imageName = row.getCell(5).getStringCellValue();
                byte[] imageData = images.get(imageName);

                // Validación: Si la imagen no coincide con el nombre en la celda, no se crea el artículo
                if (imageData != null) {
                    Image image = new Image();
                    image.setName(imageName);
                    image.setData(imageData);
                    image.setContentType("image/jpg"); // Ajustar según el tipo de archivo
                    article.addImage(image); // Establecer relación inversa
                } else {
                    errors.add("La imagen '" + imageName + "' no fue encontrada en el ZIP para el artículo '" + productName + "'.");
                    continue; // Saltar la creación del artículo si no se encuentra la imagen
                }

                // Asignar la categoría (columna 6)
                String categoryCell = row.getCell(6).getStringCellValue();
                try {
                    Long categoryId = Long.parseLong(categoryCell.replaceAll("[^0-9]", ""));
                    Category category = categoryService.findById(categoryId);
                    article.setCategory(category);
                } catch (Exception e) {
                    errors.add("La categoría '" + categoryCell + "' no es válida para el artículo '" + productName + "'.");
                    continue; // Saltar si la categoría no es válida
                }

                // Asociar el artículo con el usuario antes de guardar
                article.setUser(user);
                user.addArticle(article); // Método para asociar el artículo al usuario

                // Guardar el artículo (esto también debería guardar las imágenes asociadas)
                articleService.save(article);
                articlesToCreate++;
            }

            workbook.close();

            // Si se acumularon errores, lanzar una excepción
            if (!errors.isEmpty()) {
                throw new ProductUploadException("" + String.join(", ", errors));
            }

        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new ProductUploadException("Error al procesar los archivos de carga.");
        }
    }

}
