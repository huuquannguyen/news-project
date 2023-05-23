package com.qtiger.news.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class NewsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String category;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String imgUrl;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL)
    private List<Paragraph> paragraphs;

    private int view;

    private boolean mainPage;

    private String createdBy;

    private String tag1;

    private String tag2;

    private String tag3;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd MMM yyyy")
    private Date createdDate;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd MMM yyyy")
    private Date updatedDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd MMM yyyy")
    private Date updatedDateManual;
}
