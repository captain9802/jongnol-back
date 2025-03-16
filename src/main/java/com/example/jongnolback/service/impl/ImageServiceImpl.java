package com.example.jongnolback.service.impl;

import com.example.jongnolback.common.FileUtils;
import com.example.jongnolback.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final FileUtils fileUtils;
    @Override
    public String saveTemporaryImage(String image) {
        try {
            String imageName = UUID.randomUUID() + ".question_image.png";
            MultipartFile multipartFile = fileUtils.convertBase64ToMultipartFile(image, imageName);

            String filePath = fileUtils.uploadFile(multipartFile, "temp/");

            scheduleImageDeletion(filePath, 3600 * 1000L);  // 1시간(3600초) 후 삭제 예약

            return fileUtils.getObjectUrl(filePath);
        } catch (Exception e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }

    @Override
    public String deleteTemporaryImage(String image) {
        String key = image.substring(image.indexOf("/temp/") + 1);
        fileUtils.deleteFile(key);
        return null;
    }

    private void scheduleImageDeletion(String filePath, long delayMillis) {
        new Thread(() -> {
            try {
                Thread.sleep(delayMillis);
                String key = filePath.substring(filePath.indexOf("/temp/") + 1);
                if (fileUtils.isValidImageUrl(filePath)){
                    fileUtils.deleteFile(key);
                    System.out.println("1시간이 지나 파일이 삭제되었습니다." + key);
                } else {
                    System.out.println("파일이 존재하지 않아 삭제가 실패되었습니다.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
