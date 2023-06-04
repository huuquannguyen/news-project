package com.qtiger.news.service;

import com.qtiger.news.entity.Comment;
import com.qtiger.news.model.DeleteCommentResponse;
import com.qtiger.news.model.PostCommentRequest;
import org.springframework.security.core.Authentication;

public interface CommentService {

    Comment postComment(Long newsId, PostCommentRequest postCommentRequest, Authentication authentication);

    Comment updateComment(Long newsId, Long commentId, String content, Authentication authentication);

    DeleteCommentResponse deleteComment(Long newsId, Long commentId, Authentication authentication);

}
