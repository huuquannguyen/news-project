package com.qtiger.news.service;

import com.qtiger.news.entity.NewsEntity;
import com.qtiger.news.exception.AppException;
import com.qtiger.news.model.CreateNewsRequest;
import com.qtiger.news.model.UpdateNewsRequest;

import java.io.IOException;
import java.util.List;

public interface NewsService {
    Long createNews(CreateNewsRequest newsRequest) throws IOException, AppException;

    Long updateNews(Long id, UpdateNewsRequest updateNewsRequest) throws AppException, IOException;

    void deleteNews(Long id);

    NewsEntity getNews(Long id);

    List<NewsEntity> searchListNews(String type, String tag, String cateType, String keyword, int limit);

    NewsEntity increaseView(Long id);

    List<String> getHottestTag(int limit);
}
