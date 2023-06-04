package com.qtiger.news.service.impl;

import com.qtiger.news.entity.Comment;
import com.qtiger.news.entity.NewsEntity;
import com.qtiger.news.model.DeleteCommentResponse;
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

import java.util.Objects;

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
        OidcUser user = (OidcUser) authentication.getPrincipal();
        if (request.getReplyCommentId() != null) {
            Comment replyComment = news.getComments().stream()
                    .filter(c -> Objects.equals(c.getId(), request.getReplyCommentId()))
                    .findFirst()
                    .orElse(null);
            if (replyComment != null && !replyComment.getCreatedBy().equals(user.getSubject())) {
                comment.setReplyUserId(replyComment.getCreatedBy());
                comment.setReplyUserName(replyComment.getCreatedName());
            } else {
                log.error("The replied comment with id {} does not exist in news with id {}", request.getReplyCommentId(), news.getId());
            }
        }
        comment.setContent(request.getContent());
        comment.setCreatedName((String) user.getUserInfo().getClaims().get("name"));
        if (request.getParentId() != null) {
            Comment parentComment = news.getComments().stream()
                    .filter(c -> Objects.equals(c.getId(), request.getParentId()) && c.getParentComment() == null)
                    .findFirst()
                    .orElse(null);
            if (parentComment == null) {
                log.error("The comment's parent with id {} does not exist in news with id {}.", request.getParentId(), news.getId());
                return null;
            } else {
                comment.setParentComment(parentComment);
                comment = commentRepository.save(comment);
                parentComment.getCommentChildren().add(comment);
                commentRepository.save(parentComment);
            }
        } else {
            return commentRepository.save(comment);
        }
        return comment;
    }

    @Override
    public Comment updateComment(Long newsId, Long commentId, String content, Authentication authentication) {
        OidcUser user = (OidcUser) authentication.getPrincipal();
        NewsEntity news = newsRepository.findById(newsId).orElse(null);
        Comment comment = checkValidComment(news, commentId);
        if (comment == null) {
            return null;
        }
        if (comment.getCreatedBy() != null && !comment.getCreatedBy().equals(user.getSubject())) {
            log.error("The comment with id {} is not belong to {}", commentId, user.getSubject());
        }
        if (content != null && !content.isBlank()) {
            comment.setContent(content);
            return commentRepository.save(comment);
        }
        return comment;
    }

    @Override
    @Transactional
    public DeleteCommentResponse deleteComment(Long newsId, Long commentId, Authentication authentication) {
        OidcUser user = (OidcUser) authentication.getPrincipal();
        NewsEntity news = newsRepository.findById(newsId).orElse(null);
        if (news == null) {
            log.error("News does not exist");
            return null;
        }
        Comment comment = checkValidComment(news, commentId);
        if (comment == null) {
            return null;
        }
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_admin"));
        if (isAdmin || comment.getCreatedBy().equals(user.getSubject())) {
            boolean isParentComment = comment.getParentComment() == null;
            if (isParentComment) {
                long totalDeletedComment = news.getComments().stream()
                        .map(Comment::getParentComment)
                        .filter(Objects::nonNull)
                        .map(Comment::getId)
                        .filter(e -> e.equals(commentId)).count() + 1;
                commentRepository.delete(comment);
                log.info("Comment id {} is deleted with its sub-comments.", commentId);
                return new DeleteCommentResponse(comment, false, totalDeletedComment);
            } else {
                commentRepository.delete(comment);
                log.info("Comment id {} is deleted with its reply comments", commentId);
                return new DeleteCommentResponse(comment, true, 1);
            }
        }
        log.error("The comment with id {} is not belong to {}", commentId, user.getEmail());
        return null;
    }

    private Comment checkValidComment(NewsEntity news, Long commentId) {
        if (news == null) {
            log.error("News does not exist");
            return null;
        }
        Comment comment = news.getComments().stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElse(null);
        if (comment == null) {
            log.error("Comment with id {} does not exist in news with id {}", commentId, news.getId());
            return null;
        }
        return comment;
    }
}
