package com.qtiger.news.service.impl;

import com.qtiger.news.constant.UploadType;
import com.qtiger.news.entity.NewsEntity;
import com.qtiger.news.entity.Paragraph;
import com.qtiger.news.model.CreateNewsRequest;
import com.qtiger.news.model.CreateParagraphRequest;
import com.qtiger.news.repository.NewsRepository;
import com.qtiger.news.repository.ParagraphRepository;
import com.qtiger.news.service.NewsService;
import com.qtiger.news.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final ParagraphRepository paragraphRepository;

    @Override
    public Long createNews(CreateNewsRequest newsRequest) throws IOException {

        return newsRepository.save(mapToNewEntity(newsRequest)).getId();
    }

    @Override
    public Long updateNews(Long id) {
        return null;
    }

    @Override
    public Long deleteNews(Long id) {
        return null;
    }

    @Override
    public NewsEntity getNews(Long id) {
        return null;
    }

    private NewsEntity mapToNewEntity(CreateNewsRequest createNewsRequest) throws IOException {
        NewsEntity news = new NewsEntity();
        news.setCategory(createNewsRequest.getCategory());
        news.setTitle(createNewsRequest.getTitle());
        news.setContent(createNewsRequest.getContent());
        List<Paragraph> paragraphs = new ArrayList<>();
        Long latestParagraphId = paragraphRepository.findMaxId();
        for (int i = 0; i < createNewsRequest.getParagraphRequests().size(); i++) {
            Paragraph paragraph = (mapToParagraph(createNewsRequest.getParagraphRequests().get(i),
                    latestParagraphId == null ? (i + 1) : (latestParagraphId + i + 1)));
            paragraph.setNews(news);
            paragraphs.add(paragraph);
        }
        news.setParagraphs(paragraphs);
        Long latestNewsId = newsRepository.findMaxId();
        if (createNewsRequest.getImg().getSize() > 0) {
            String imgUrl = FileUtil.uploadFile(createNewsRequest.getImg(),  latestNewsId == null ? 1 : latestNewsId + 1,
                    UploadType.NEWS_IMAGE);
            news.setImgUrl(imgUrl);
        }
        return news;
    }

    private Paragraph mapToParagraph(CreateParagraphRequest paragraphRequest, Long fileId) throws IOException {
        Paragraph paragraph = new Paragraph();
        paragraph.setContent(paragraphRequest.getContent());
        paragraph.setTitle(paragraphRequest.getTitle());
        if (paragraphRequest.getImg().getSize() > 0) {
            String imgUrl = FileUtil.uploadFile(paragraphRequest.getImg(), fileId, UploadType.PARAGRAPH_IMAGE);
            paragraph.setImgUrl(imgUrl);
        }
        if (paragraphRequest.getVideo().getSize() > 0) {
            String videoUrl = FileUtil.uploadFile(paragraphRequest.getVideo(), fileId, UploadType.PARAGRAPH_VIDEO);
            paragraph.setVideoUrl(videoUrl);
        }
        return paragraph;
    }
}
