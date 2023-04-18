package com.qtiger.news.controller;

import com.qtiger.news.entity.NewsEntity;
import com.qtiger.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping("")
    public String homaPage(Principal principal, Model model) {
        if (Objects.nonNull(principal)) {
            model.addAttribute("authenticated", true);
        }
        return "index.html";
    }

    @GetMapping("/contact")
    public String contact(Principal principal, Model model) {
        if (Objects.nonNull(principal)) {
            model.addAttribute("authenticated", true);
        }
        return "contact.html";
    }

    @GetMapping("/news/{id}")
    public String singlePage(@PathVariable Long id, Model model, Principal principal) {
        if (Objects.nonNull(principal)) {
            model.addAttribute("authenticated");
        }
        NewsEntity newsEntity = newsService.increaseView(id);
        if (!Objects.nonNull(newsEntity)) {
            return "redirect:/";
        }
        model.addAttribute("news", newsEntity);
        return "single.html";
    }

    @GetMapping("/news/search/{type}")
    @ResponseBody
    public ResponseEntity<List<NewsEntity>> searchListNews(@PathVariable String type,
                                                           @RequestParam(required = false, defaultValue = "") String cateType,
                                                           @RequestParam(required = false, defaultValue = "") String keyword,
                                                           @RequestParam(required = false, defaultValue = "3") String limit) {
        int limitNumber;
        try {
            limitNumber = Integer.parseInt(limit);
        } catch (NumberFormatException e) {
            limitNumber = 3;
        }
        return ResponseEntity.ok().body(newsService.searchListNews(type, cateType, keyword, limitNumber));
    }

}
