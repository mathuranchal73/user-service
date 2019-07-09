package com.sms.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

	 public ResponseEntity<?>  saveImage(@RequestParam("file") MultipartFile File);
}
