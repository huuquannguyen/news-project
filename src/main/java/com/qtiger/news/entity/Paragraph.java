package com.qtiger.news.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paragraph {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String imgUrl;

    private String videoUrl;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne (fetch = FetchType.LAZY)
    @JsonIgnore
    private NewsEntity news;
}
