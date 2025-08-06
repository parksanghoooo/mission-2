package com.ll.wisesaying.domain.wiseSaying.service;

import com.ll.wisesaying.domain.wiseSaying.model.entity.WiseSaying;
import com.ll.wisesaying.domain.wiseSaying.repository.WiseSayingRepository;
import com.ll.wisesaying.global.constant.ErrorMessage;

import java.util.List;
import java.util.Optional;

public class WiseSayingService {

    private final WiseSayingRepository repository;

    public WiseSayingService() {
        this.repository = new WiseSayingRepository();
    }

    public WiseSaying create(String content, String author) {
        return repository.create(content, author);
    }

    public List<WiseSaying> findAll() {
        return repository.findAll();
    }

    public void deleteById(WiseSaying wiseSaying) {
        repository.deleteById(wiseSaying);
    }

    public Optional<WiseSaying> findById(long id) {
        return repository.findById(id);
    }

    public void update(WiseSaying wiseSaying, String newContent, String newAuthor) {
        wiseSaying.setContent(newContent);
        wiseSaying.setAuthor(newAuthor);
        repository.update(wiseSaying);
    }

    public void build() {
        repository.build();
    }

}
