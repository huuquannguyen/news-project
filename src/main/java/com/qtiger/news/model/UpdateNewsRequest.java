package com.qtiger.news.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Data
public class UpdateNewsRequest {
    private String title;
    private String category;
    private String content;
    private MultipartFile img;
    private Integer view;
    private Boolean mainPage;
    List<UpdateParagraphRequest> updateParagraphRequests = new ArrayList<>();
    @Valid
    List<CreateParagraphRequest> addedParagraphRequests = new ArrayList<>();
    private String updatedDateManual;
    private Integer addedParaNumber;
}
