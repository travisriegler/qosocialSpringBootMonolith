package com.qosocial.v1api.common.service;

import com.qosocial.v1api.common.exception.GenericSaveImageException;
import com.qosocial.v1api.common.exception.InvalidImageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.UUID;

@Service
@Profile({"staging", "prod"})
public class AwsImageServiceImpl implements ImageService {

    private final S3Client s3Client;
    @Value("${aws.s3.bucket-name}")
    private String bucketName;
    private static final Logger logger = LoggerFactory.getLogger(AwsImageServiceImpl.class);

    @Autowired
    public AwsImageServiceImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public void deleteImage(String pictureUrl) {
        try {
            // if the pictureUrl is null or empty, there is nothing for us to do
            if (pictureUrl == null || pictureUrl.isEmpty()) return;

            // Extract the object key from the pictureUrl
            String objectKey = pictureUrl.substring(pictureUrl.lastIndexOf("/") + 1);

            // Use the AWS SDK to delete the object from the S3 bucket
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build());

        } catch (Exception ex) {
            // fail silently
            logger.warn("AwsImageServiceImpl deleteImage was unable to delete pictureUrl: " + pictureUrl, ex);
        }
    }

    @Override
    public String saveImage(MultipartFile imageFile) {

        try {
            // Define max dimensions for the image received
            int maxWidth = 200;
            int maxHeight = 200;

            // Define the target file type of the final saved image
            String format = "jpg";

            // Read the image from the InputStream
            BufferedImage originalImage  = ImageIO.read(imageFile.getInputStream());

            // Check if the image was successfully read
            if (originalImage == null) throw new InvalidImageException();

            // Validate the dimensions of the image
            if (originalImage.getWidth() > maxWidth || originalImage.getHeight() > maxHeight) throw new InvalidImageException();

            // Create a new BufferedImage without an alpha channel
            BufferedImage newImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

            // Draw the original image onto the new image
            Graphics2D g = newImage.createGraphics();
            g.drawImage(originalImage, 0, 0, null);
            g.dispose();

            // Generate a unique file name
            String fileName = UUID.randomUUID().toString() + "." + format;

            // Convert to InputStream for S3 upload
            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {

                ImageIO.write(newImage, format, os);
                byte[] buffer = os.toByteArray();

                try (InputStream is = new ByteArrayInputStream(buffer)) {
                    // Upload to S3
                    s3Client.putObject(PutObjectRequest.builder()
                                    .bucket(bucketName)
                                    .key(fileName)
                                    .contentType("image/jpeg")
                                    .build(),
                            RequestBody.fromInputStream(is, buffer.length));
                }
            }

            return fileName;

        } catch (InvalidImageException ex) {
            logger.warn("AwsImageServiceImpl saveImage was provided an invalid image");
            throw ex;
        } catch (Exception ex) {
            logger.error("AwsImageServiceImpl saveImage encountered an unexpected error", ex);
            throw new GenericSaveImageException();
        }
    }
}
