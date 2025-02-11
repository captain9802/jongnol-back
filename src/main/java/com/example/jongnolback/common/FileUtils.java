package com.example.jongnolback.common;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.jongnolback.configuration.AmazonConfiguration;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class FileUtils {

    private final AmazonS3 s3;

    private String accessKey;
    private String secretKey;
    private String regionName;
    private String endPoint;
    private String bucketName;

    @Autowired
    public FileUtils(AmazonConfiguration amazonConfiguration) {
        // AmazonConfiguration에서 값을 주입받음
        this.accessKey = amazonConfiguration.getAccessKey();
        this.secretKey = amazonConfiguration.getSecretKey();
        this.regionName = amazonConfiguration.getRegionName();
        this.endPoint = amazonConfiguration.getEndPoint();
        this.bucketName = amazonConfiguration.getBucketName();
        // AWS 자격 증명 및 클라이언트 초기화
        s3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(accessKey, secretKey)
                ))
                .build();
    }

    public String uploadFile(MultipartFile multipartFile, String directory) {
        String fileOrigin = multipartFile.getOriginalFilename();

        // 파일 이름을 랜덤화하여 덮어쓰지 않도록 설정
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmsss");
        Date nowDate = new Date();
        String nowDateStr = formater.format(nowDate);
        UUID uuid = UUID.randomUUID();
        String fileName = nowDateStr + "_" + uuid.toString() + "_" + fileOrigin;

        // 파일 업로드 될 파일 경로
        String filePath = directory + fileName;

        // 파일을 S3에 업로드
        try (InputStream fileInputStream = multipartFile.getInputStream()) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());

            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName,
                    filePath,
                    fileInputStream,
                    objectMetadata
            ).withCannedAcl(CannedAccessControlList.PublicRead); // 퍼블릭 접근 설정

            s3.putObject(putObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // 업로드된 파일의 경로 리턴
        return filePath;
    }

    public String getObjectUrl(String filePath) {
        return s3.getUrl(bucketName, filePath).toString();  // 파일 URL 반환
    }

    // base64 문자열을 MultipartFile로 변환하는 메서드
    public MultipartFile convertBase64ToMultipartFile(String base64Str, String fileName) throws Exception {
        String[] base64Arr = base64Str.split(",");
        byte[] decodedBytes = Base64.getDecoder().decode(base64Arr[1]);  // base64 값 추출
        ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);

        // CustomMultipartFile을 사용하여 MultipartFile을 구현
        return new CustomMultipartFile(fileName, decodedBytes, bis);
    }

    public void deleteFile(String filePath) {
        s3.deleteObject(new DeleteObjectRequest(bucketName, filePath));
    }

    // CustomMultipartFile 클래스 구현
    public static class CustomMultipartFile implements MultipartFile {
        private final String fileName;
        private final byte[] content;
        private final InputStream inputStream;

        public CustomMultipartFile(String fileName, byte[] content, InputStream inputStream) {
            this.fileName = fileName;
            this.content = content;
            this.inputStream = inputStream;
        }

        @Override
        public String getName() {
            return fileName;
        }

        @Override
        public String getOriginalFilename() {
            return fileName;
        }

        @Override
        public String getContentType() {
            return "image/png";  // 예시로 "image/png"로 설정
        }

        @Override
        public boolean isEmpty() {
            return content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte[] getBytes() {
            return content;
        }

        @Override
        public InputStream getInputStream() {
            return inputStream;
        }

        @Override
        public void transferTo(java.io.File dest) throws java.io.IOException, IllegalStateException {
            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(dest)) {
                fos.write(content);
            }
        }
    }
}
