package com.qtiger.news.controller;

import com.qtiger.news.entity.Comment;
import com.qtiger.news.entity.NewsEntity;
import com.qtiger.news.model.PostCommentRequest;
import com.qtiger.news.model.UpdateCommentRequest;
import com.qtiger.news.service.CommentService;
import com.qtiger.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    private final CommentService commentService;

    @Value("${app-uri}")
    private String appUri;

    @GetMapping("")
    public String homePage(Authentication authentication, Model model) {
        if (Objects.nonNull(authentication)) {
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
    public String singlePage(@PathVariable Long id, Model model, Authentication authentication) {
        if (Objects.nonNull(authentication)) {
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

    @PostMapping("/api/news/{id}/comment")
    @ResponseBody
    public ResponseEntity<Comment> postComment (@PathVariable(name = "id") Long newsId,
                                                PostCommentRequest postCommentRequest,
                                                Authentication authentication) {
        if (Objects.isNull(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        Comment comment = commentService.postComment(newsId, postCommentRequest, authentication);
        return ResponseEntity.ok().body(comment);
    }

    @PostMapping("/api/news/{id}/comment/update")
    public ResponseEntity<Comment> updateComment(@PathVariable(name = "id") Long newsId,
                                                 UpdateCommentRequest updateCommentRequest,
                                                 Authentication authentication) {
        Comment comment = commentService.updateComment(newsId, updateCommentRequest.getCommentId(),
                updateCommentRequest.getContent(), authentication);
        return ResponseEntity.ok().body(comment);
    }

    @PostMapping("/api/news/{id}/comment/delete")
    public ResponseEntity<Long> deleteComment(@PathVariable(name = "id") Long newsId,
                                              Long commentId,
                                              Authentication authentication) {
        Long deletedCommentId = commentService.deleteComment(newsId, commentId, authentication);
        return ResponseEntity.ok().body(deletedCommentId);
    }

}
