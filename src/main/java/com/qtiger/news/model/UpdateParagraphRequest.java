package com.qtiger.news.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateParagraphRequest {
    private Long paraId;
    private String title;
    private String content;
    private MultipartFile img;
    private MultipartFile video;
}
