package com.qtiger.news.service.impl;

import com.qtiger.news.constant.UploadType;
import com.qtiger.news.entity.NewsEntity;
import com.qtiger.news.entity.Paragraph;
import com.qtiger.news.exception.AppException;
import com.qtiger.news.model.CreateNewsRequest;
import com.qtiger.news.model.CreateParagraphRequest;
import com.qtiger.news.model.UpdateNewsRequest;
import com.qtiger.news.model.UpdateParagraphRequest;
import com.qtiger.news.repository.NewsRepository;
import com.qtiger.news.repository.ParagraphRepository;
import com.qtiger.news.service.NewsService;
import com.qtiger.news.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final ParagraphRepository paragraphRepository;


    @Value("${file-upload.news.image}")
    private String imageLocation;

    @Value("${file-upload.news.video}")
    private String videoLocation;

    @Override
    public Long createNews(CreateNewsRequest newsRequest) throws IOException, AppException {
        return newsRepository.save(mapToNewEntity(newsRequest)).getId();
    }

    @Override
    public Long updateNews(Long id, UpdateNewsRequest updateNewsRequest) throws AppException, IOException {
        NewsEntity news = newsRepository.findById(id).orElse(null);
        if (news != null) {
            if (Objects.nonNull(updateNewsRequest.getMainPage())) {
                news.setMainPage(updateNewsRequest.getMainPage());
            }
            if (!updateNewsRequest.getTitle().isBlank()) {
                news.setTitle(updateNewsRequest.getTitle());
            }
            if (!updateNewsRequest.getCategory().isBlank()) {
                news.setCategory(updateNewsRequest.getCategory());
            }
            if (!updateNewsRequest.getContent().isBlank()) {
                news.setContent(updateNewsRequest.getContent());
            }
            if (!updateNewsRequest.getUpdatedDateManual().isBlank()) {
                LocalDateTime localDateTime = LocalDateTime.parse(updateNewsRequest.getUpdatedDateManual());
                news.setUpdatedDateManual(Timestamp.valueOf(localDateTime));
            }
            if (Objects.nonNull(updateNewsRequest.getView())) {
                news.setView(updateNewsRequest.getView());
            }
            if (updateNewsRequest.getTag1() != null && !updateNewsRequest.getTag1().isBlank()) {
                news.setTag1(StringUtils.trimAllWhitespace(updateNewsRequest.getTag1().toLowerCase()));
            }
            if (updateNewsRequest.getTag2() != null && !updateNewsRequest.getTag2().isBlank()) {
                news.setTag2(StringUtils.trimAllWhitespace(updateNewsRequest.getTag2().toLowerCase()));
            }
            if (updateNewsRequest.getTag3() != null && !updateNewsRequest.getTag3().isBlank()) {
                news.setTag3(StringUtils.trimAllWhitespace(updateNewsRequest.getTag3().toLowerCase()));
            }
            if (updateNewsRequest.getImg().getSize() > 0) {
                news.setImgUrl(FileUtil.uploadFile(updateNewsRequest.getImg(), id, UploadType.NEWS_IMAGE, imageLocation));
            }
            if (updateNewsRequest.getUpdateParagraphRequests().size() > 0) {
                for (Paragraph paragraph: news.getParagraphs()) {
                    Optional<UpdateParagraphRequest> optional = updateNewsRequest.getUpdateParagraphRequests().stream()
                            .filter(p -> Objects.equals(p.getParaId(), paragraph.getId()))
                            .findFirst();
                    if (optional.isPresent()) {
                        mapUpdateParaToNewsPara(paragraph, optional.get());
                    } else {
                        news.getParagraphs().remove(paragraph);
                    }
                }
            }
            if (updateNewsRequest.getAddedParagraphRequests().size() > 0) {
                Long latestParagraphId = paragraphRepository.findMaxId();
                for (int i = 0; i < updateNewsRequest.getAddedParagraphRequests().size(); i++) {
                    Paragraph paragraph = mapToParagraph(updateNewsRequest.getAddedParagraphRequests().get(i),
                            latestParagraphId == null ? (i + 1) : (latestParagraphId + i + 1));
                    paragraph.setNews(news);
                    news.getParagraphs().add(paragraph);
                }
            }
            return newsRepository.save(news).getId();
        }
        return null;
    }

    @Override
    public void deleteNews(Long id) {
        NewsEntity news = newsRepository.findById(id).orElse(null);
        if (news == null) {
            log.error("News with id {} does not exist", id);
            return;
        }
        newsRepository.deleteById(id);
        FileUtil.deleteFile(news.getImgUrl());
        List<String> imageUrls = news.getParagraphs().stream().map(Paragraph::getImgUrl).collect(Collectors.toList());
        List<String> fileUrls = news.getParagraphs().stream().map(Paragraph::getVideoUrl).collect(Collectors.toList());
        fileUrls.addAll(imageUrls);
        for (String fileUrl: fileUrls) {
            FileUtil.deleteFile(fileUrl);
        }
    }

    @Override
    public NewsEntity getNews(Long id) {
        return newsRepository.findById(id).orElse(null);
    }

    @Override
    public List<NewsEntity> searchListNews(String type, String tag, String cateType, String keyword, int limit) {
        switch (type) {
            case "category" :
                return searchByCategory(cateType, keyword, limit);
            case "popular" :
                return searchPopular(keyword, limit);
            case "trending" :
                return searchTrending(keyword, limit);
            case "latest" :
                return searchLatest(keyword, limit);
            case "main-page":
                return searchMainPage();
            case "key":
                return searchByKeyTitle(keyword, limit);
            case "tag":
                return tag.isBlank() ? Collections.emptyList() : searchByTag(tag, keyword, limit);
            default:
                return Collections.emptyList();
        }
    }

    @Override
    public NewsEntity increaseView(Long id) {
        NewsEntity news = newsRepository.findById(id).orElse(null);
        if (Objects.nonNull(news)) {
            news.setView(news.getView() + 1);
            return newsRepository.save(news);
        }
        return null;
    }

    @Override
    public List<String> getHottestTag(int limit) {
        Pageable pageable = pagination(0, "view", 5);
        Page<NewsEntity> newsList = newsRepository.findAll(pageable);
        List<String> result = new ArrayList<>();
        while (result.size() < limit) {
            for (NewsEntity news : newsList) {
                if (news.getTag1() != null && !news.getTag1().isBlank()) {
                    result.add(news.getTag1());
                }
                if (news.getTag2() != null && !news.getTag2().isBlank()) {
                    result.add(news.getTag2());
                }
                if (news.getTag3() != null && !news.getTag3().isBlank()) {
                    result.add(news.getTag3());
                }
            }
            if (newsList.hasNext()) {
                pageable = pageable.next();
                newsList = newsRepository.findAll(pageable);
            } else {
                break;
            }
        }
        return result;
    }

    private List<NewsEntity> searchByTag(String tag, String keyword, int limit) {
        Pageable pageable = pagination(0, "updatedDateManual", limit);
        return searchWithKeyword(newsRepository.searchByTag(tag, pageable).toList(), keyword);
    }


    private List<NewsEntity> searchByKeyTitle(String keyword, int limit) {
        Pageable pageable = pagination(0, "updatedDateManual", limit);
        return newsRepository.searchKeywordAndTags(keyword, pageable).toList();
    }

    private List<NewsEntity> searchMainPage() {
        Pageable pageable = pagination(0, "updatedDateManual", 3);
        List<NewsEntity> newsList = newsRepository.findByMainPageIsTrue(pageable).toList();
        List<NewsEntity> result = new ArrayList<>(newsList);
        if (newsList.size() < 3) {
            List<NewsEntity> latestNewsList = searchLatest("", 3 - newsList.size());
            result.addAll(latestNewsList);
        }
        return result;
    }

    private List<NewsEntity> searchWithKeyword (List<NewsEntity> newsList, String keyword) {
        if (keyword.isEmpty()) {
            return newsList;
        }
        return newsList.stream()
                .filter(n -> n.getTitle().contains(keyword)).collect(Collectors.toList());
    }

    private List<NewsEntity> searchLatest (String keyword, int limit) {
        Pageable pageable = pagination(0, "updatedDateManual", limit);

        return searchWithKeyword(newsRepository.findAll(pageable).toList(), keyword);
    }

    private List<NewsEntity> searchTrending (String keyword, int limit) {
        List<NewsEntity> trending = searchMostViewByConstraintDate(limit, 1);
        List<NewsEntity> result = new ArrayList<>(trending);
        if (result.size() < limit) {
            List<Long> trendingIds = trending.stream().map(NewsEntity::getId).collect(Collectors.toList());
            Pageable pageable = pagination(0, "updatedDateManual", limit - result.size());
            List<NewsEntity> latest = newsRepository.findAllByIdIsNotIn(trendingIds, pageable).toList();
            result.addAll(latest);
        }
        return searchWithKeyword(result, keyword);
    }

    private List<NewsEntity> searchPopular (String keyword, int limit) {
        return searchWithKeyword(searchMostViewByConstraintDate(limit ,7), keyword);
    }

    private List<NewsEntity> searchMostViewByConstraintDate(int limit, int dayNumber) {
        Date today = new Date();
        long oneDay = 86400000L;
        long daysAgoMs = today.getTime() - oneDay * dayNumber;
        Date daysAgo = new Date(daysAgoMs);
        Pageable pageable = pagination(0, "view", limit);
        return newsRepository.findByConstraintDate(daysAgo, pageable).toList();
    }

    private List<NewsEntity> searchByCategory(String cateType, String keyword, int limit) {
        if (cateType.isEmpty()) {
            return Collections.emptyList();
        }
        Pageable pageable = pagination(0, "updatedDateManual", limit);
        List<NewsEntity> result = new ArrayList<>();
        if (cateType.equalsIgnoreCase("all")) {
            result.addAll(newsRepository.findByCategoryIgnoreCase("Business", pageable).toList());
            result.addAll(newsRepository.findByCategoryIgnoreCase("Technology", pageable).toList());
            result.addAll(newsRepository.findByCategoryIgnoreCase("Sport", pageable).toList());
            result.addAll(newsRepository.findByCategoryIgnoreCase("Entertainment", pageable).toList());
            return result;
        }
        return searchWithKeyword(newsRepository.findByCategoryIgnoreCase(cateType, pageable).toList(), keyword);
    }

    private Pageable pagination(Integer page, String order, Integer limit) {
        if (order == null) {
            return PageRequest.of(page, limit);
        } else {
            return PageRequest.of(page, limit, Sort.by(order).descending());
        }
    }

    private NewsEntity mapToNewEntity(CreateNewsRequest createNewsRequest) throws IOException, AppException {
        NewsEntity news = new NewsEntity();
        news.setCategory(createNewsRequest.getCategory());
        news.setTitle(createNewsRequest.getTitle());
        news.setMainPage(createNewsRequest.isMainPage());
        if (createNewsRequest.getTag1() != null && !createNewsRequest.getTag1().isBlank()) {
            news.setTag1(StringUtils.trimAllWhitespace(createNewsRequest.getTag1().toLowerCase()));
        }
        if (createNewsRequest.getTag2() != null && !createNewsRequest.getTag2().isBlank()) {
            news.setTag2(StringUtils.trimAllWhitespace(createNewsRequest.getTag2().toLowerCase()));
        }
        if (createNewsRequest.getTag3() != null && !createNewsRequest.getTag3().isBlank()) {
            news.setTag3(StringUtils.trimAllWhitespace(createNewsRequest.getTag3().toLowerCase()));
        }
        if (Objects.nonNull(createNewsRequest.getView())) {
            news.setView(createNewsRequest.getView());
        } else {
            news.setView(0);
        }
        if (!createNewsRequest.getUpdatedDateManual().isBlank()) {
            LocalDateTime localDateTime = LocalDateTime.parse(createNewsRequest.getUpdatedDateManual());
            news.setUpdatedDateManual(Timestamp.valueOf(localDateTime));
        } else {
            news.setUpdatedDateManual(new Date());
        }
        news.setContent(createNewsRequest.getContent());
        List<Paragraph> paragraphs = new ArrayList<>();
        Long latestParagraphId = paragraphRepository.findMaxId();
        for (int i = 0; i < createNewsRequest.getParagraphRequests().size(); i++) {
            Paragraph paragraph = mapToParagraph(createNewsRequest.getParagraphRequests().get(i),
                    latestParagraphId == null ? (i + 1) : (latestParagraphId + i + 1));
            paragraph.setNews(news);
            paragraphs.add(paragraph);
        }
        news.setParagraphs(paragraphs);
        Long latestNewsId = newsRepository.findMaxId();
        if (Objects.nonNull(createNewsRequest.getImg()) && createNewsRequest.getImg().getSize() > 0) {
            String imgUrl = FileUtil.uploadFile(createNewsRequest.getImg(),  latestNewsId == null ? 1 : latestNewsId + 1,
                    UploadType.NEWS_IMAGE, imageLocation);
            news.setImgUrl(imgUrl);
        }
        return news;
    }

    private Paragraph mapToParagraph(CreateParagraphRequest paragraphRequest, Long fileId) throws IOException, AppException {
        Paragraph paragraph = new Paragraph();
        paragraph.setContent(paragraphRequest.getContent());
        paragraph.setTitle(paragraphRequest.getTitle());
        if (paragraphRequest.getImg().getSize() > 0) {
            String imgUrl = FileUtil.uploadFile(paragraphRequest.getImg(), fileId, UploadType.PARAGRAPH_IMAGE, imageLocation);
            paragraph.setImgUrl(imgUrl);
        }
        if (paragraphRequest.getVideo().getSize() > 0) {
            String videoUrl = FileUtil.uploadFile(paragraphRequest.getVideo(), fileId, UploadType.PARAGRAPH_VIDEO, videoLocation);
            paragraph.setVideoUrl(videoUrl);
        }
        return paragraph;
    }

    private void mapUpdateParaToNewsPara(Paragraph paragraph, UpdateParagraphRequest updateParagraphRequest) throws AppException, IOException {
        if (!updateParagraphRequest.getTitle().isBlank()) {
            paragraph.setTitle(updateParagraphRequest.getTitle());
        }
        if (!updateParagraphRequest.getContent().isBlank()) {
            paragraph.setContent(updateParagraphRequest.getContent());
        }
        if (updateParagraphRequest.getImg().getSize() > 0) {
            String imgUrl = FileUtil.uploadFile(updateParagraphRequest.getImg(), paragraph.getId(), UploadType.PARAGRAPH_IMAGE, imageLocation);
            paragraph.setImgUrl(imgUrl);
        }
        if (updateParagraphRequest.getVideo().getSize() > 0) {
            String videoUrl = FileUtil.uploadFile(updateParagraphRequest.getVideo(), paragraph.getId(), UploadType.PARAGRAPH_VIDEO, videoLocation);
            paragraph.setVideoUrl(videoUrl);
        }
    }
}
