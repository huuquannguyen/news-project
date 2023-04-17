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

    @CreatedDate
    @JsonFormat(pattern="dd-MM-yyyy")
    private Date createdDate;

    @LastModifiedDate
    @JsonFormat(pattern="dd-MM-yyyy")
    private Date updatedDate;

    @JsonFormat(pattern="dd-MM-yyyy")
    private Date updatedDateManual;
}
