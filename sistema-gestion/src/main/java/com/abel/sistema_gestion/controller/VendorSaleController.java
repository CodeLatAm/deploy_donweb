package com.abel.sistema_gestion.controller;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.image.ImageResponse;
import com.abel.sistema_gestion.dto.vendorSale.*;
import com.abel.sistema_gestion.serviceimpl.EmailService;
import com.abel.sistema_gestion.serviceimpl.service.VendorSaleService;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
import java.text.NumberFormat;
import java.util.*;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;

import java.io.ByteArrayOutputStream;
import java.util.List;
@Log4j2
@RestController
@RequestMapping(value = "vendorSales")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class VendorSaleController {

    private final VendorSaleService vendorSaleService;

    @PostMapping("/cart/{cartId}/client/{clientId}")
    public ResponseEntity<MessageResponse> createVendorSale(@PathVariable Integer cartId,
                                                            @PathVariable Integer clientId,
                                                            @Valid @RequestBody VendorSalesRequest request){
        MessageResponse response = vendorSaleService.createVendorSale(cartId, clientId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<VendorSaleResponse>> getVendorSale(@PathVariable Integer vendorId){
        List<VendorSaleResponse> response = vendorSaleService.getVendorSale(vendorId);
        return ResponseEntity.ok(response);
    }

    //Paginacion sin filtros
    @GetMapping("/vendor/{vendorId}/page")
    public ResponseEntity<Map<String, Object>> getVendorSalePage(
            @PathVariable Integer vendorId,
            @RequestParam(defaultValue = "0") int page,  // Número de página
            @RequestParam(defaultValue = "10") int size  // Tamaño de la página
    ) {
        Page<VendorSaleResponse> salesPage = vendorSaleService.getVendorSalePage(vendorId, page, size);

        // Crear un mapa con los datos de la paginación y los enlaces
        Map<String, Object> response = new HashMap<>();
        response.put("sales", salesPage.getContent());
        response.put("currentPage", salesPage.getNumber());
        response.put("totalItems", salesPage.getTotalElements());
        response.put("totalPages", salesPage.getTotalPages());

        // Crear enlaces para la navegación
        if (salesPage.hasNext()) {
            response.put("nextPageUrl", "/vendor/" + vendorId + "/page" + "?page=" + (page + 1) + "&size=" + size);
        }
        if (salesPage.hasPrevious()) {
            response.put("previousPageUrl", "/vendor/" + vendorId + "/page" + "?page=" + (page - 1) + "&size=" + size);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/vendor/{vendorId}/page/filter")
    public ResponseEntity<Map<String, Object>> getVendorSalePageFilter(
            @PathVariable Integer vendorId,
            @RequestParam(defaultValue = "0") int page,  // Número de página
            @RequestParam(defaultValue = "10") int size, // Tamaño de la página
            @RequestParam(required = false) String search,  // Búsqueda por cualquier atributo
            @RequestParam(defaultValue = "0", required = false) Integer dateFilter // Puede ser null
    ) {
        // Lógica para manejar el filtro de fecha

        // Llama al servicio pasando el filtro de búsqueda y filtro de fecha
        Page<VendorSaleResponse> salesPage = vendorSaleService.getFilteredVendorSalePage(vendorId, page, size, search, dateFilter);

        // Crear la respuesta de la paginación
        Map<String, Object> response = new HashMap<>();
        response.put("sales", salesPage.getContent());
        response.put("currentPage", salesPage.getNumber());
        response.put("totalItems", salesPage.getTotalElements());
        response.put("totalPages", salesPage.getTotalPages());

        // Enlaces de navegación
        if (salesPage.hasNext()) {
            response.put("nextPageUrl", "/vendor/" + vendorId + "/page/filter?page=" + (page + 1) + "&size=" + size + (search != null ? "&search=" + search : "") + (dateFilter != null ? "&dateFilter=" + dateFilter : ""));
        }
        if (salesPage.hasPrevious()) {
            response.put("previousPageUrl", "/vendor/" + vendorId + "/page/filter?page=" + (page - 1) + "&size=" + size + (search != null ? "&search=" + search : "") + (dateFilter != null ? "&dateFilter=" + dateFilter : ""));
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //PAGE NUEVOS
    @GetMapping("/vendor/{vendorId}/page/search")
    public ResponseEntity<Map<String, Object>> getVendorsSalesPageFilter(
            @PathVariable Integer vendorId,
            @RequestParam(defaultValue = "0") int page,  // Número de página
            @RequestParam(defaultValue = "8") int size, // Tamaño de la página
            @RequestParam(required = false) String search  // Búsqueda por cualquier atributo

    ) {
        Page<VendorSaleResponse> saleResponses = vendorSaleService.getVendorsSalesPageFilter(vendorId,page,size,search);
        // Crear la respuesta de la paginación
        Map<String, Object> response = new HashMap<>();
        response.put("sales", saleResponses.getContent());
        response.put("currentPage", saleResponses.getNumber());
        response.put("totalItems", saleResponses.getTotalElements());
        response.put("totalPages", saleResponses.getTotalPages());
        return ResponseEntity.ok(response);

    }
    @PreAuthorize("hasAnyAuthority('VENDEDOR')")
    @GetMapping("/vendor/{vendorId}/dateSearch")
    public ResponseEntity<Map<String, Object>> getVendorSalesByFilter(
            @PathVariable Integer vendorId,
            @RequestParam(defaultValue = "0") int page,  // Número de página
            @RequestParam(defaultValue = "8") int size,  // Tamaño de página
            @RequestParam(required = false) Integer year,  // Filtro por año
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,  // Fecha de inicio
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate     // Fecha de fin
    ) {
        log.info("En controller getVendorSalesByFilter");
        // Si startDate o endDate son vacíos o nulos, ignorarlos
        Page<VendorSaleResponse> salesPage = vendorSaleService.getVendorSalesByFilterDate(
                vendorId, year, (startDate != null ? startDate : null), (endDate != null ? endDate : null), page, size);

        // Crear la respuesta de la paginación
        Map<String, Object> response = new HashMap<>();
        response.put("sales", salesPage.getContent());
        response.put("currentPage", salesPage.getNumber());
        response.put("totalItems", salesPage.getTotalElements());
        response.put("totalPages", salesPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    //

    @GetMapping("/suggestions")
    public ResponseEntity<List<ClientVendorSaleResponse>> getSuggestedVendorSales(
            @RequestParam("vendorId") Integer vendorId,
            @RequestParam("search") String search) {

        // Llamar al servicio para obtener las ventas sugeridas
        List<ClientVendorSaleResponse> suggestedSales = vendorSaleService.getSuggestedVendorSales(vendorId, search);

        // Verificar si se encontraron coincidencias
        if (suggestedSales.isEmpty()) {
            return ResponseEntity.noContent().build(); // Si no hay resultados, devolver 204 No Content
        } else {
            return ResponseEntity.ok(suggestedSales); // Si hay resultados, devolver 200 OK con la lista
        }
    }

    @PutMapping("/{id}/update-status")
    public ResponseEntity<MessageResponse> updateProductStatus(@PathVariable Integer id){
        return ResponseEntity.ok(vendorSaleService.updateProductStatus(id));
    }

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-pdf")
    public ResponseEntity<String> sendPDF(@RequestBody VendorSaleResponse sale) {
        // Generar el PDF en Base64
        String pdfBase64 = generatePDF2(sale);

        // Verificar si el PDF se generó correctamente
        if (pdfBase64 != null) {
            // Enviar el correo electrónico con el PDF
            emailService.sendEmailWithPDF(sale.getClientResponse().getEmail(), pdfBase64);
            return ResponseEntity.ok("Correo enviado con éxito.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al generar el PDF.");
        }
    }


    private String generatePDF2(VendorSaleResponse sale) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Inicializar el documento PDF
            PdfWriter pdfWriter = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);

            // Crear una tabla que servirá como recuadro principal
            Table mainTable = new Table(1);
            mainTable.setWidth(UnitValue.createPercentValue(100)); // Ancho completo
            mainTable.setBorder(new SolidBorder(ColorConstants.BLACK, 2)); // Borde de la tabla
            mainTable.setMarginTop(10); // Margen superior
            mainTable.setMarginBottom(10); // Margen inferior

            // Crear una celda para el contenido del PDF
            Cell mainCell = new Cell();
            mainCell.setPadding(10); // Relleno dentro de la celda
            mainCell.setBackgroundColor(new DeviceRgb(220, 240, 255)); // Fondo suave
            mainCell.setBorder(Border.NO_BORDER); // Sin borde para la celda interna

            // Crear el contenido del PDF
            // Título de la factura
            mainCell.add(new Paragraph("Factura: " + sale.getInvoiceNumber())
                    .setFont(PdfFontFactory.createFont("Helvetica-Bold"))
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.RIGHT)); // Alinear a la derecha

            // Crear una tabla interna para los detalles del vendedor y la fecha
            Table detailTable = new Table(1);
            detailTable.setBorder(new SolidBorder(ColorConstants.BLACK, 1)); // Borde de la tabla interna

            // Crear un párrafo para "Vendedor" y "Fecha"
            Paragraph vendorAndDateParagraph = new Paragraph();
            vendorAndDateParagraph.add(new Text("Vendedor: ").setFont(PdfFontFactory.createFont("Helvetica-Bold")).setFontSize(10));
            vendorAndDateParagraph.add(new Text(sale.getVendorResponse().getName()).setFont(PdfFontFactory.createFont("Helvetica")).setFontSize(10));
            vendorAndDateParagraph.add(new Text("\n")); // Salto de línea
            vendorAndDateParagraph.add(new Text("Fecha: ").setFont(PdfFontFactory.createFont("Helvetica-Bold")).setFontSize(10));
            String formattedDate = sale.getDate().toString(); // O usar un DateTimeFormatter para formatear
            vendorAndDateParagraph.add(new Text(formattedDate).setFont(PdfFontFactory.createFont("Helvetica")).setFontSize(10));

            // Añadir el párrafo a la celda de la tabla interna
            Cell detailCell = new Cell().add(vendorAndDateParagraph);
            detailCell.setBorder(new SolidBorder(ColorConstants.BLACK, 1)); // Borde de la celda
            detailCell.setPadding(10); // Relleno dentro de la celda
            detailCell.setBackgroundColor(new DeviceRgb(255, 200, 200)); // Color de fondo blanco
            detailTable.addCell(detailCell); // Añadir la celda a la tabla interna

            // Añadir la tabla interna a la celda principal
            mainCell.add(detailTable);
            mainCell.add(new Paragraph().setMarginBottom(10)); // Espacio de 10 unidades
            // Detalles del cliente (si están disponibles)
            if (sale.getClientResponse() != null) {
                Paragraph clientParagraph = new Paragraph();
                String name = sale.getClientResponse().getName();
                String dni = sale.getClientResponse().getDni();
                clientParagraph.add(new Text("Cliente: ").setFont(PdfFontFactory.createFont("Helvetica-Bold")).setFontSize(10));
                clientParagraph.add((name != null ? name : "No disponible")).setFont(PdfFontFactory.createFont("Helvetica")).setFontSize(10);
                clientParagraph.add(new Text("\n"));
                clientParagraph.add(new Text("Dni: ").setFont(PdfFontFactory.createFont("Helvetica-Bold")).setFontSize(10));
                clientParagraph.add((dni != null ? dni : "No disponible")).setFont(PdfFontFactory.createFont("Helvetica")).setFontSize(10);

                // Añadir otro recuadro para el cliente
                Table clientTable = new Table(1);
                Cell clientCell = new Cell().add(clientParagraph);

                clientCell.setBorder(new SolidBorder(ColorConstants.BLACK, 1));
                clientCell.setPadding(10);
                clientCell.setBackgroundColor(new DeviceRgb(255, 200, 200)); // Color de fondo suave
                clientTable.addCell(clientCell);
                mainCell.add(clientTable); // Añadir la tabla del cliente a la celda principal
            }

            // Detalles de productos
            Paragraph detailsHeader = new Paragraph("Detalles de la Compra:")
                    .setFont(PdfFontFactory.createFont("Helvetica-Bold"))
                    .setFontSize(12)
                    .setMarginTop(10); // Agregar margen superior aquí

            mainCell.add(detailsHeader); // Añadir el párrafo a la celda principal

            Double total = 0.0;
            // Lista de productos
            NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault()); // Utiliza la configuración regional por defecto
            for (CartItemBasicResponse item : sale.getCartItemResponseList()) {
                String articleName = (item.getArticleResponse() != null)
                        ? item.getArticleResponse().getName()
                        : "Artículo desconocido";

                String formattedTotal = numberFormat.format(item.getTotal()); // Formatear el total
                mainCell.add(new Paragraph(articleName + " - Cantidad: " + item.getQuantity() + " - Total: $" + formattedTotal)
                        .setFont(PdfFontFactory.createFont("Helvetica"))
                        .setFontSize(10));
                total = total + item.getTotal();
                // Manejo de la imagen
                if (item.getArticleResponse() != null && item.getArticleResponse().getImageResponse() != null) {
                    ImageResponse imageResponse = item.getArticleResponse().getImageResponse().get(0);
                    byte[] imageBytes = imageResponse.getData(); // Esto es un Uint8Array (byte[])

                    // Crear la imagen
                    try {
                        ImageData imageData = ImageDataFactory.create(imageBytes);
                        Image productImage = new Image(imageData);
                        productImage.scaleAbsolute(50, 50); // Escala de la imagen
                        mainCell.add(productImage);
                    } catch (Exception e) {
                        System.err.println("Error al cargar la imagen: " + e.getMessage());
                        mainCell.add(new Paragraph("Error al cargar la imagen.")
                                .setFont(PdfFontFactory.createFont("Helvetica"))
                                .setFontSize(10));
                    }
                }
            }

            // Total compra
            String formattedTotalSale = numberFormat.format(total); // Formatear el total
            mainCell.add(new Paragraph("Totales: $").add(String.valueOf(formattedTotalSale))
                    .setFont(PdfFontFactory.createFont("Helvetica-Bold"))
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20));

            // Mensaje final de agradecimiento
            mainCell.add(new Paragraph("Gracias por su compra")
                    .setFont(PdfFontFactory.createFont("Helvetica-Bold"))
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20));

            // Añadir la celda principal a la tabla principal
            mainTable.addCell(mainCell);

            // Agregar la tabla del recuadro principal al documento
            document.add(mainTable);

            // Cerrar el documento
            document.close();

            // Convertir el PDF a Base64
            byte[] pdfBytes = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(pdfBytes);
        } catch (Exception e) { // Captura de excepciones genéricas
            e.printStackTrace();
            return null; // Maneja el error de manera adecuada
        }
    }

    @GetMapping("/sales-count-by-year/{vendorId}")
    public ResponseEntity<Map<String, Long>> getSalesCountByVendor(@PathVariable Integer vendorId) {
        Map<String, Long> salesCountByVendor = vendorSaleService.getSalesCountByVendorId(vendorId);
        return ResponseEntity.ok(salesCountByVendor);
    }
    @PreAuthorize("hasAnyAuthority('VENDEDOR')")
    @GetMapping("/sales-area/vendor/{vendorId}")
    public ResponseEntity<Map<String, Long>> getSalesByShippingArea(@PathVariable Integer vendorId) {
        Map<String, Long> salesByArea = vendorSaleService.getSalesCountByShippingArea(vendorId);
        return new ResponseEntity<>(salesByArea, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('VENDEDOR')")
    @GetMapping("/vendor/{vendorId}/sales-count")
    public ResponseEntity<Long> getSalesCount(@PathVariable Integer vendorId) {
        Long salesCount = vendorSaleService.getSalesCount(vendorId);
        return ResponseEntity.ok(salesCount);
    }

    @PreAuthorize("hasAnyAuthority('PROPIETARIO')")
    @GetMapping("/sales-count-by-year/propietario/{userId}")
    public ResponseEntity<Map<String, Long>> getSalesCountByYearAndUserId(@PathVariable Integer userId) {
        Map<String, Long> salesCountByYear = vendorSaleService.getSalesCountByYearAndUserId(userId);
        return ResponseEntity.ok(salesCountByYear);
    }

    @PreAuthorize("hasAnyAuthority('PROPIETARIO')")
    @GetMapping("/sales-area/user/{userId}")
    public ResponseEntity<Map<String, Long>> getSalesByShippingAreaForUser(@PathVariable Integer userId) {
        Map<String, Long> salesByArea = vendorSaleService.getSalesCountByShippingAreaAndUserId(userId);
        return new ResponseEntity<>(salesByArea, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('PROPIETARIO')")
    @GetMapping("/count-by-user/{userId}")
    public ResponseEntity<Long> getCountSalesByUserId(@PathVariable Integer userId) {
        Long countSales = vendorSaleService.getCountSalesByUserId(userId);
        return new ResponseEntity<>(countSales, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('PROPIETARIO')")
    @GetMapping("/payment-type/{userId}")
    public ResponseEntity<List<PaymentTypeSalesResponse>> getSalesCountByPaymentType(@PathVariable Integer userId){
        List<PaymentTypeSalesResponse> response = vendorSaleService.getSalesCountByPaymentType(userId);
        return ResponseEntity.ok(response);
    }


}
