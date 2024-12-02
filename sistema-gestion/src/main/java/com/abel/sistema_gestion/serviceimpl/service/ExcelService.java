package com.abel.sistema_gestion.serviceimpl.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface ExcelService {
    ByteArrayOutputStream generateExcelTemplate() throws IOException;

    void processProductUpload(Integer userId, MultipartFile excelFile, MultipartFile zipFile);
}
