package com.example.zzapdiz.share.media;

import org.springframework.web.multipart.MultipartFile;

public interface MediaUploadInterface {

    Media uploadMedia(MultipartFile media, String mediaPurpose);
    void deleteFile(String fileName);
    String createFileName(String fileName);
    String getFileExtension(String fileName);
}
