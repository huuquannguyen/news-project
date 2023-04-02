package com.qtiger.news.controller;

import com.qtiger.news.model.CreateNewsRequest;
import com.qtiger.news.model.CreateParagraphRequest;
import com.qtiger.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public String homePage() {
        return "index.html";
    }

    @GetMapping("/single")
    public String singlePage(){
        return "single.html";
    }

    @GetMapping("/create")
    public String createNews(CreateNewsRequest createNewsRequest){
        return "create-news.html";
    }

    @PostMapping(value = "/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String createNewsWithParagraph(@ModelAttribute CreateNewsRequest createNewsRequest , Model model) {
        if (createNewsRequest.getParagraphNumber() != null) {
            for (int i = 0; i < createNewsRequest.getParagraphNumber(); i++) {
                CreateParagraphRequest createParagraphRequest = new CreateParagraphRequest();
                createNewsRequest.getParagraphRequests().add(createParagraphRequest);
            }
        }
        model.addAttribute("createNewRequest", createNewsRequest);
        return "create-news.html";
    }

    @PostMapping(value = "/create/process", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String createNewsProcess(@ModelAttribute CreateNewsRequest createNewsRequest) throws IOException {
        newsService.createNews(createNewsRequest);
        return "single.html";
    }

}
