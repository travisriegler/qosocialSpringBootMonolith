package com.qosocial.v1api.common.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    public void deleteImage(String pictureUrl);
    public String saveImage(MultipartFile imageFile);
}
