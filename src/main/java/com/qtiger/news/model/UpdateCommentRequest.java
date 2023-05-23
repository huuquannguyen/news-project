package com.qtiger.news.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateCommentRequest {

    @NotNull(message = "Comment ID cannot be null")
    Long commentId;

    String content;

}
