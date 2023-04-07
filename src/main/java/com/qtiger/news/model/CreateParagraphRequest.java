package com.qtiger.news.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Data
public class CreateParagraphRequest {

    @NotBlank(message = "Tiêu đề đoạn văn không thể để trống")
    private String title;

    @NotBlank(message = "Nội dung đoạn văn không thể để trống")
    private String content;

    private MultipartFile img;

    private MultipartFile video;
}
