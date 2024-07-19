package org.routineade.RoutineAdeServer.config;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client S3Client;

    public void uploadFileToS3(final MultipartFile file, final String fileName) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        try {
            S3Client.putObject(
                    new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata)
            );
        } catch (IOException e) {
            throw new RuntimeException("사진 읽기 실패");
        }
    }

    public URL getFileURLFromS3(final String fileName) {
        return S3Client.getUrl(bucket, fileName);
    }

}
