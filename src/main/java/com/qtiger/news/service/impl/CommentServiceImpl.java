package com.qtiger.news.service.impl;

import com.qtiger.news.entity.Comment;
import com.qtiger.news.entity.NewsEntity;
import com.qtiger.news.model.PostCommentRequest;
import com.qtiger.news.repository.CommentRepository;
import com.qtiger.news.repository.NewsRepository;
import com.qtiger.news.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final NewsRepository newsRepository;

    @Override
    @Transactional
    public Comment postComment(Long newsId, PostCommentRequest request, Authentication authentication) {
        Comment comment = new Comment();
        NewsEntity news = newsRepository.findById(newsId).orElse(null);
        if (news == null) {
            log.error("The news with id = {} does not exist.", newsId);
            return null;
        }
        comment.setNews(news);
        if (request.getParentId() != null) {
            Comment parentComment = commentRepository.findById(request.getParentId()).orElse(null);
            if (parentComment == null) {
                log.error("The comment's parent with id {} does not exist.", request.getParentId());
            } else {
                comment.setParentComment(parentComment);
            }
        }
        OidcUser user = (OidcUser) authentication.getPrincipal();
        if (request.getReplyName() != null && !user.getEmail().equals(request.getReplyEmail())) {
            boolean existReplyName = news.getComments().stream()
                    .map(Comment::getCreatedName)
                    .anyMatch(n -> n.equals(request.getReplyName()));
            if (existReplyName) {
                comment.setReplyName(request.getReplyName());
            } else {
                log.error("Reply name {} does not exist.", request.getReplyName());
            }
        }
        comment.setContent(request.getContent());
        comment.setCreatedName((String) user.getUserInfo().getClaims().get("name"));

        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(Long newsId, Long commentId, String content, Authentication authentication) {
        OidcUser user = (OidcUser) authentication.getPrincipal();

        Comment comment = checkValidComment(newsId, commentId);
        if (comment == null) {
            return null;
        }
        if (comment.getCreatedBy() != null && !comment.getCreatedBy().equals(user.getEmail())) {
            log.error("The comment with id {} is not belong to {}", commentId, user.getEmail());
        }
        if (content != null && content.isBlank()) {
            comment.setContent(content);
        }
        return comment;
    }

    @Override
    public Long deleteComment(Long newsId, Long commentId, Authentication authentication) {
        OidcUser user = (OidcUser) authentication.getPrincipal();

        Comment comment = checkValidComment(newsId, commentId);
        if (comment == null) {
            return null;
        }
        boolean isAdmin = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_ADMIN"));
        if (isAdmin || comment.getCreatedBy().equals(user.getEmail())) {
            commentRepository.delete(comment);
            return commentId;
        }
        log.error("The comment with id {} is not belong to {}", commentId, user.getEmail());
        return null;
    }

    private Comment checkValidComment(Long newsId, Long commentId) {
        NewsEntity news = newsRepository.findById(newsId).orElse(null);
        if (news == null) {
            log.error("News with id {} does not exist", newsId);
            return null;
        }
        Comment comment = news.getComments().stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElse(null);
        if (comment == null) {
            log.error("Comment with id {} does not exist in news with id {}", commentId, newsId);
            return null;
        }
        return comment;
    }
}
