package com.qtiger.news.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Paragraph {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String imgUrl;

    private String videoUrl;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JsonIgnore
    private NewsEntity news;
}
