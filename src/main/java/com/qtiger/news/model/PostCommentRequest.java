package com.qtiger.news.model;

import lombok.Data;

@Data
public class PostCommentRequest {

    Long parentId;

    String replyName;

    String content;

    String replyEmail;

}
