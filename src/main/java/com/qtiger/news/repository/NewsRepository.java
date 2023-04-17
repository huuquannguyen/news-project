package com.qtiger.news.repository;

import com.qtiger.news.entity.NewsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface NewsRepository extends JpaRepository<NewsEntity, Long> {
    @Query("select max(n.id) from NewsEntity n")
    Long findMaxId();

    Page<NewsEntity> findByCategoryIgnoreCase(String category, Pageable pageable);

    @Query("from NewsEntity n where n.updatedDate > :#{#constraintDate}")
    Page<NewsEntity> findByConstraintDate(@Param("constraintDate") Date constraintDate, Pageable pageable);

    Page<NewsEntity> findByMainPageIsTrue(Pageable pageable);

    Page<NewsEntity> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
}
