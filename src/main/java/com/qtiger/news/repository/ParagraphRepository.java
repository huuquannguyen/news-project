package com.qtiger.news.repository;

import com.qtiger.news.entity.Paragraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ParagraphRepository extends JpaRepository<Paragraph, Long> {

    @Query("select max(p.id) from Paragraph p")
    Long findMaxId();
}
