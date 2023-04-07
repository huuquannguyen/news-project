package com.qtiger.news.util;

import com.qtiger.news.constant.ErrorCode;
import com.qtiger.news.constant.UploadType;
import com.qtiger.news.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Slf4j
public class FileUtil {


    public static String uploadFile(MultipartFile file, Long id, UploadType type, String fileLocation) throws IOException, AppException {
        Path uploadPath = Paths.get(fileLocation);
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        if (!Objects.isNull(type)) {
            String fileType = type.toString().split("_")[1];
            if (!validateFileType(fileExtension, fileType)) {
                throw new AppException(ErrorCode.FILE_EXTENSION_NOT_MATCH);
            }
        }

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = type.toString() + "_" + id + fileExtension;
        Path filePath = uploadPath.resolve(fileName);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Lỗi khi tải ảnh lên");
            return "";
        }
        return "/" + fileLocation + "/" + fileName;
    }

    public static boolean validateFileType(String fileExtension, String fileType) {
        List<String> imageExtensions = List.of(".jpg", ".png", ".jpeg", ".gif", ".svg");
        if (!fileType.equals("IMAGE") && !fileType.equals("VIDEO")) {
            return false;
        }
        if (fileType.equals("VIDEO") && !fileExtension.equals(".mp4")) {
            return false;
        }
        if (fileType.equals("IMAGE") && !imageExtensions.contains(fileExtension)) {
            return false;
        }
        return true;
    }

}
