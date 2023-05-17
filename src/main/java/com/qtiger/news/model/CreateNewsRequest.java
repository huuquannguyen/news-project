package com.qtiger.news.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
public class CreateNewsRequest {

    @NotBlank(message = "Tiêu đề báo không thể để trống")
    private String title;

    @NotBlank(message = "Thể loại báo không thể để trống")
    private String category;

    @NotBlank(message = "Nội dung bài báo không thể để trống")
    private String content;

    private MultipartFile img;

    private Integer view;

    private boolean mainPage;

    private String updatedDateManual;

    @Valid
    private List<CreateParagraphRequest> paragraphRequests = new ArrayList<>();

    private Integer paragraphNumber;

}
