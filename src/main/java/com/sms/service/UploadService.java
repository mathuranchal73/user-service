package com.sms.service;

import java.io.IOException;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

	 public FileSystemResource getUserFileResource(MultipartFile File) throws IOException;
}
