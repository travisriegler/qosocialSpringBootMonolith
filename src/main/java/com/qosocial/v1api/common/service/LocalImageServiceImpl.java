package com.qosocial.v1api.common.service;

import com.qosocial.v1api.common.exception.GenericSaveImageException;
import com.qosocial.v1api.common.exception.InvalidImageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Profile("dev")
public class LocalImageServiceImpl implements ImageService {

    private static final Logger logger = LoggerFactory.getLogger(LocalImageServiceImpl.class);

    @Override
    public void deleteImage(String pictureUrl) {

        try {
            // if the pictureUrl is null or empty, there is nothing for us to do
            if (pictureUrl == null || pictureUrl.isEmpty()) return;

            // Define the base directory
            Path baseDir = Paths.get("uploads/");

            // Convert the relative path to an absolute path and normalize it
            Path filePath = baseDir.resolve(pictureUrl.replace("/uploads/", "")).normalize();

            // Ensure the filePath is actually within the uploads folder
            if (!filePath.startsWith(baseDir)) {
                logger.warn("LocalImageService deleteImage attempted to delete a file outside the uploads directory: " + filePath);
                return;
            }

            // if the file does not exist, there is nothing for us to do
            if (!Files.exists(filePath)) return;

            // Delete the file
            Files.delete(filePath);

        } catch (Exception ex) {
            logger.warn("LocalImageService deleteImage was unable to delete pictureUrl: " + pictureUrl, ex);
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

            // Define the target directory
            Path uploadPath = Paths.get("uploads/");

            // Create the target directory if it doesn't exist
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

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

            // Generate the unique file path
            Path filePath = uploadPath.resolve(fileName);

            // Write the new image as a JPEG
            ImageIO.write(newImage, format, filePath.toFile());

            return "/uploads/" + fileName;

        } catch (InvalidImageException ex) {
            logger.warn("LocalImageService saveImage was provided an invalid image");
            throw ex;
        } catch (Exception ex) {
            logger.error("LocalImageService saveImage encountered an unexpected error", ex);
            throw new GenericSaveImageException(ex);
        }
    }
}
