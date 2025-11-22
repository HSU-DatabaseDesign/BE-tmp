package com.example.WebNovelReviewSite.domain.novel.service;

import com.example.WebNovelReviewSite.domain.novel.dto.CollectionRequestDTO;
import com.example.WebNovelReviewSite.domain.novel.dto.CollectionResponseDTO;
import com.example.WebNovelReviewSite.domain.novel.entity.Collection;
import com.example.WebNovelReviewSite.domain.novel.entity.CollectedNovel;
import com.example.WebNovelReviewSite.domain.novel.entity.CollectedNovelId;
import com.example.WebNovelReviewSite.domain.novel.entity.Novel;
import com.example.WebNovelReviewSite.domain.novel.repository.CollectedNovelRepository;
import com.example.WebNovelReviewSite.domain.novel.repository.CollectionRepository;
import com.example.WebNovelReviewSite.domain.novel.repository.NovelRepository;
import com.example.WebNovelReviewSite.domain.user.entity.User;
import com.example.WebNovelReviewSite.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;
    private final NovelRepository novelRepository;
    private final CollectedNovelRepository collectedNovelRepository;

    @Transactional
    public Long createCollection(CollectionRequestDTO.CreateDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Collection collection = Collection.builder()
                .user(user)
                .collectionName(request.getCollectionName())
                .content(request.getContent())
                .collectedNovels(new ArrayList<>())
                .build();

        Collection saved = collectionRepository.save(collection);
        return saved.getCollectionId();
    }

    @Transactional
    public void updateCollection(Long collectionId, CollectionRequestDTO.UpdateDto request) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 컬렉션입니다."));

        if (request.getCollectionName() != null)
            collection.setCollectionName(request.getCollectionName());
        if (request.getContent() != null)
            collection.setContent(request.getContent());
    }

    @Transactional
    public void deleteCollection(Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 컬렉션입니다."));
        collectionRepository.delete(collection);
    }

    @Transactional
    public void addNovelToCollection(Long collectionId, Long novelId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 컬렉션입니다."));

        Novel novel = novelRepository.findById(novelId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 작품입니다."));

        CollectedNovelId id = new CollectedNovelId(collectionId, novelId);
        CollectedNovel collectedNovel = new CollectedNovel(id, collection, novel);
        collectedNovelRepository.save(collectedNovel);
    }

    @Transactional
    public void removeNovelFromCollection(Long collectionId, Long novelId) {
        CollectedNovelId id = new CollectedNovelId(collectionId, novelId);
        collectedNovelRepository.deleteById(id);
    }

    public List<CollectionResponseDTO.CollectionDetailDto> getCollectionsByUser(Long userId) {
        return collectionRepository.findByUser_UserId(userId).stream()
                .map(collection -> CollectionResponseDTO.CollectionDetailDto.builder()
                        .collectionId(collection.getCollectionId())
                        .userId(collection.getUser().getUserId())
                        .userName(collection.getUser().getName())
                        .collectionName(collection.getCollectionName())
                        .content(collection.getContent())
                        .novelCount(collection.getCollectedNovels().size())
                        .build())
                .collect(Collectors.toList());
    }

    public CollectionResponseDTO.CollectionDetailDto getCollection(Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 컬렉션입니다."));

        return CollectionResponseDTO.CollectionDetailDto.builder()
                .collectionId(collection.getCollectionId())
                .userId(collection.getUser().getUserId())
                .userName(collection.getUser().getName())
                .collectionName(collection.getCollectionName())
                .content(collection.getContent())
                .novelCount(collection.getCollectedNovels().size())
                .build();
    }
}
