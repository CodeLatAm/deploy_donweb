package com.abel.sistema_gestion.serviceimpl;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.entity.Article;
import com.abel.sistema_gestion.entity.Image;
import com.abel.sistema_gestion.mapper.ImageMapper;
import com.abel.sistema_gestion.repository.ImageRepository;
import com.abel.sistema_gestion.serviceimpl.service.ArticleService;
import com.abel.sistema_gestion.serviceimpl.service.ImageService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService {
    private ImageRepository imageRepository;
    private ImageMapper imageMapper;
    private ArticleService articleService;

    public ImageServiceImpl(ImageRepository imageRepository, ImageMapper imageMapper,
                            ArticleService articleService) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
        this.articleService = articleService;
    }

    @Transactional
    @Override
    public MessageResponse updtateImage(Integer articleId, MultipartFile[] newImages) {
        Article article = articleService.getArticleById(articleId);
        // Eliminar las imágenes existentes si es necesario
        imageRepository.deleteByArticleId(article.getId());
        // Procesar las nuevas imágenes y guardarlas
        List<Image> images = Arrays.stream(newImages)
                .map(newImage -> {
                    Image image = new Image();
                    try {
                        image.setName(newImage.getOriginalFilename());
                        image.setContentType(newImage.getContentType());
                        image.setData(newImage.getBytes());
                    } catch (IOException e) {
                        // Aquí puedes decidir cómo manejar el error de una imagen fallida, por ejemplo, retornando null
                        throw new RuntimeException("Error al procesar la imagen", e);
                    }
                    image.setArticle(article);
                    return image;
                })
                .collect(Collectors.toList());

        imageRepository.saveAll(images); // Guardar todas las imágenes al final
        return new MessageResponse(
                "Imagen actualizada", HttpStatus.OK
        );
    }

    @Override
    public void save(Image image) {
        imageRepository.save(image);
    }
}
