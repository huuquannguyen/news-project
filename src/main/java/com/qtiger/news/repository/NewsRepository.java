package com.qtiger.news.repository;

import com.qtiger.news.entity.NewsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<NewsEntity, Long> {
    @Query("select max(n.id) from NewsEntity n")
    Long findMaxId();

    Page<NewsEntity> findByCategoryIgnoreCase(String category, Pageable pageable);

    @Query("from NewsEntity n where n.updatedDate > :#{#constraintDate}")
    Page<NewsEntity> findByConstraintDate(@Param("constraintDate") Date constraintDate, Pageable pageable);

    Page<NewsEntity> findByMainPageIsTrue(Pageable pageable);

    @Query("from NewsEntity n where lower(n.title) like lower(concat('%', :keyword, '%')) or n.tag1 = :keyword or n.tag2 = :keyword or n.tag3 = :keyword ")
    Page<NewsEntity> searchKeywordAndTags(@Param("keyword") String keyword, Pageable pageable);

    @Query("from NewsEntity n where n.tag1 = :tag or n.tag2 = :tag or n.tag3 = :tag")
    Page<NewsEntity> searchByTag (@Param("tag") String tag, Pageable pageable);

    Page<NewsEntity> findAllByIdIsNotIn(List<Long> ids, Pageable pageable);
}
