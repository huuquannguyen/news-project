package com.qtiger.news.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private NewsEntity news;

    private String content;

    @ManyToOne
    private Comment parentComment;

    private String replyName;

    private String createdName;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    @JsonFormat(timezone = "Asia/Bangkok", shape = JsonFormat.Shape.STRING, pattern="dd MMM yyyy, HH:mm")
    private Date createdDate;

    @LastModifiedDate
    @JsonFormat(timezone = "Asia/Bangkok", shape = JsonFormat.Shape.STRING, pattern="dd MMM yyyy, HH:mm")
    private Date updatedDate;



}
