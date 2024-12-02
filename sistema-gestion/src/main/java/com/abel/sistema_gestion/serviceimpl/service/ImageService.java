package com.abel.sistema_gestion.serviceimpl.service;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.entity.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    MessageResponse updtateImage(Integer articleId, MultipartFile[] newImages);

    void save(Image image);
}
