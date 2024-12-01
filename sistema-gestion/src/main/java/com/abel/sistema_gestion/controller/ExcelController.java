package com.abel.sistema_gestion.controller;


import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.serviceimpl.service.ExcelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/excel")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class ExcelController {

    private final ExcelService excelService;
    @GetMapping("/template")
    public ResponseEntity<byte[]> downloadTemplate() {
        try {
            ByteArrayOutputStream outputStream = excelService.generateExcelTemplate();
            byte[] bytes = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=template_productos.xlsx");
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/upload/{userId}")
    public ResponseEntity<MessageResponse> uploadProducts(
            @PathVariable Integer userId,
            @RequestParam("excel") MultipartFile excelFile,
            @RequestParam("images") MultipartFile zipFile) {
        try {
            excelService.processProductUpload(userId, excelFile, zipFile);
            return ResponseEntity.ok(new MessageResponse("Productos cargados exitosamente", HttpStatus.OK));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(new MessageResponse("Error al cargar los productos: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
