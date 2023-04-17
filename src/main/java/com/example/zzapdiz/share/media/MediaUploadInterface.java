package com.example.zzapdiz.share.media;

import org.springframework.web.multipart.MultipartFile;

public interface MediaUploadInterface {

    void uploadMedia(MultipartFile media, String mediaPurpose, String projectTitle);
    void deleteFile(String fileName);
    String createFileName(String fileName);
    String getFileExtension(String fileName);
}
