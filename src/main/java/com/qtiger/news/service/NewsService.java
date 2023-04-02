package com.qtiger.news.service;

import com.qtiger.news.entity.NewsEntity;
import com.qtiger.news.model.CreateNewsRequest;

import java.io.IOException;

public interface NewsService {
    Long createNews(CreateNewsRequest newsRequest) throws IOException;

    Long updateNews(Long id);

    Long deleteNews(Long id);

    NewsEntity getNews(Long id);
}
