package com.abel.sistema_gestion.controller;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.serviceimpl.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/images")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class ImageController {

    private final ImageService imageService;

    @PutMapping("/update-image/{articleId}")
    private ResponseEntity<MessageResponse> updateImage(
            @PathVariable Integer articleId,
            @RequestParam("images") MultipartFile[] newImages){
        MessageResponse response = imageService.updtateImage(articleId, newImages);
        return ResponseEntity.ok(response);
    }


}
