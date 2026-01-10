package com._blog._blog.util.utils;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com._blog._blog.exception.CustomException;

import io.jsonwebtoken.io.IOException;

public class Postutil {
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/jpg",
            "video/mp4");

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "jpg", "jpeg", "png", "mp4");

    public static void validateMedia(MultipartFile file) throws IOException, java.io.IOException {

        if (file == null || file.isEmpty()) {
            throw new CustomException("media", "File is empty");
        }

        // 1️⃣ Content-Type
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new CustomException("mediaType", "Invalid content type");
        }

        // 2️⃣ Extension
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.contains(".")) {
            throw new CustomException("media", "Invalid file name");
        }

        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new CustomException("media", "Invalid file extension");
        }

        // 3️⃣ Magic bytes (signature)
        try (InputStream is = file.getInputStream()) {
            byte[] header = is.readNBytes(8);

            boolean isImage = header[0] == (byte) 0xFF && header[1] == (byte) 0xD8 || // JPG
                    header[0] == (byte) 0x89 && header[1] == (byte) 0x50; // PNG

            boolean isMp4 = header[4] == 'f' && header[5] == 't' &&
                    header[6] == 'y' && header[7] == 'p';

            if (!isImage && !isMp4) {
                throw new CustomException("media", "File content is not valid image or video");
            }
        }
    }

    public static com._blog._blog.util.MediaType getMediaType(MultipartFile image) {
        List<String> allowedTypes = List.of("image/jpeg", "image/png", "image/jpg", "video/mp4");
        if (!allowedTypes.contains(image.getContentType())) {
            throw new CustomException("mediaType", "Invalid media type: " + image.getContentType());
        }

        if (image.getContentType().startsWith("image/")) {
            return com._blog._blog.util.MediaType.IMAGE; // or other image types based on actual content type
        } else if (image.getContentType().equals("video/mp4")) {
            return com._blog._blog.util.MediaType.VIDEO;
        } else {
            throw new CustomException("mediaType", "Unsupported media type: " + image.getContentType());
        }
    }

    public static String uploadImage(MultipartFile image) throws IOException, java.io.IOException {

        if (image == null || image.isEmpty()) {
            return null;
        }

        long maxSize = 5 * 1024 * 1024;
        if (image.getSize() > maxSize) {
            throw new CustomException("image", "Image size must be less than 5MB");
        }

        Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();// normalize for rm . and ..
        Files.createDirectories(uploadDir);

        String originalFileName = Paths.get(image.getOriginalFilename()).getFileName().toString();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        String fileName = UUID.randomUUID() + extension;
        Path targetLocation = uploadDir.resolve(fileName);

        Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/" + fileName;
    }

    public static void deleteFile(String mediaUrl) {
        try {
            if (mediaUrl == null || mediaUrl.isBlank()) {
                return;
            }

            String filePathStr = mediaUrl.replaceFirst("^/", "");

            Path filePath = Paths.get(filePathStr)
                    .toAbsolutePath()
                    .normalize();

            Files.deleteIfExists(filePath);

        } catch (Exception e) {
            System.out.println("Failed to delete file: " + mediaUrl);
            e.printStackTrace();
        }
    }
}
