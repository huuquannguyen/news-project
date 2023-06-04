package com.qtiger.news.model;

import lombok.Data;

@Data
public class PostCommentRequest {

    Long parentId;

    Long replyCommentId;

    String content;

}
