package com.qtiger.news.repository;

import com.qtiger.news.entity.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<NewsEntity, Long> {
    @Query("select max(n.id) from NewsEntity n")
    Long findMaxId();
}
