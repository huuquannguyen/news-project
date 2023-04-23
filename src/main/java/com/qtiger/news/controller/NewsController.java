package com.qtiger.news.controller;

import com.qtiger.news.entity.NewsEntity;
import com.qtiger.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    private final RestTemplate restTemplate;

    @Value("${app-uri}")
    private String appUri;

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
            model.addAttribute("authenticated", true);
        }
        NewsEntity newsEntity = newsService.increaseView(id);
        if (!Objects.nonNull(newsEntity)) {
            return "redirect:/";
        }
        model.addAttribute("news", newsEntity);
        return "single.html";
    }

    @GetMapping("/news/search/{type}")
    public String newsList(@PathVariable String type,
                             @RequestParam(required = false, defaultValue = "") String cateType,
                             @RequestParam(required = false, defaultValue = "") String keyword,
                             @RequestParam(required = false, defaultValue = "5") String limit,
                             Model model, Principal principal) {

        if (Objects.nonNull(principal)) {
            model.addAttribute("authenticated", true);
        }

        int limitNumber;
        try {
            limitNumber = Integer.parseInt(limit);
        } catch (NumberFormatException e) {
            limitNumber = 5;
        }
        String url = appUri + "/api/news/search/" + type;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("cateType", cateType)
                .queryParam("keyword", keyword)
                .queryParam("limit", limitNumber);
        ResponseEntity<NewsEntity[]> response = restTemplate.exchange(builder.toUriString(),
                HttpMethod.GET, null, NewsEntity[].class);
        List<NewsEntity> newsList = Arrays.asList(response.getBody());
        model.addAttribute("newsList", newsList);
        model.addAttribute("nextLimit", limitNumber + 5);
        model.addAttribute("showSeeMore", newsList.size() >= limitNumber);
        model.addAttribute("keyword", keyword);
        model.addAttribute("cateType", cateType);
        return "news-list.html";
    }

    @GetMapping("/api/news/search/{type}")
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
        return ResponseEntity.ok().body(newsService.searchListNews(type, cateType.toLowerCase(), keyword, limitNumber));
    }

}
