package com.example.WebNovelReviewSite.domain.novel.repository;

import com.example.WebNovelReviewSite.domain.novel.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    List<Collection> findByUser_UserId(Long userId);
}
