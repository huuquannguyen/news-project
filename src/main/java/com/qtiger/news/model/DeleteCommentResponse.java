package com.qtiger.news.model;

import com.qtiger.news.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCommentResponse {

    private Comment comment;

    private boolean isSubComment;

    private long totalDeleted;

}
