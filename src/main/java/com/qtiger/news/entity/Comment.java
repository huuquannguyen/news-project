package com.qtiger.news.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JsonIgnore
    private NewsEntity news;

    private String content;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> commentChildren = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment parentComment;

    private String replyUserId;

    private String replyUserName;

    private String createdName;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    @JsonFormat(timezone = "Asia/Bangkok", shape = JsonFormat.Shape.STRING, pattern="dd MMM yyyy, HH:mm")
    private Date createdDate;

    @LastModifiedDate
    @JsonFormat(timezone = "Asia/Bangkok", shape = JsonFormat.Shape.STRING, pattern="dd MMM yyyy, HH:mm")
    private Date updatedDate;

    @PreRemove
    public void removeChildrenComment() {
        Long id = this.getId();
        this.getCommentChildren().forEach(c -> c.setParentComment(null));
        this.getCommentChildren().clear();
        this.getNews().getComments().removeIf(c -> c.getId().equals(id));
        this.setNews(null);
    }

}
