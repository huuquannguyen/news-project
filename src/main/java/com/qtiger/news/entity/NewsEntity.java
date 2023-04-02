package com.qtiger.news.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
public class NewsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String category;

    private String content;

    private String imgUrl;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL)
    private List<Paragraph> paragraphs;

    private Timestamp createdDate;
}
