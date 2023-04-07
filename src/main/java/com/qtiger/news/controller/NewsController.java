package com.qtiger.news.controller;

import com.qtiger.news.entity.NewsEntity;
import com.qtiger.news.exception.AppException;
import com.qtiger.news.model.CreateNewsRequest;
import com.qtiger.news.model.CreateParagraphRequest;
import com.qtiger.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/create")
    public String createNews(CreateNewsRequest createNewsRequest){
        return "create-news.html";
    }

    @GetMapping("/{id}")
    public String singlePage(@PathVariable Long id, Model model){
        NewsEntity newsEntity = newsService.getNews(id);
        model.addAttribute("news", newsEntity);
        return "single.html";
    }

    @PostMapping(value = "/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String createNewsWithParagraph(@ModelAttribute @Valid CreateNewsRequest createNewsRequest,
                                          BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            model.addAttribute("newsErrors", errors);
            return "create-news.html";
        }
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
    public String createNewsProcess(@ModelAttribute @Valid CreateNewsRequest createNewsRequest,
                                    BindingResult bindingResult, Model model) throws IOException, AppException {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            model.addAttribute("errors", errors);
            return "create-news.html";
        }
        Long id = newsService.createNews(createNewsRequest);
        return "redirect:/news/" + id;
    }

}
