package com.ll.wisesaying.domain.wiseSaying.model.entity;

import com.ll.wisesaying.domain.wiseSaying.service.WiseSayingService;
import com.ll.wisesaying.global.constant.Message;

public class WiseSaying {

    private long id;
    private String content;
    private String author;

    public WiseSaying(long id, String content, String author) {
        this.id = id;
        this.content = content;
        this.author = author;
    }

    // object mapper는 클래스의 기본 생성자를 통해 빈 객체 생성 후 필드 매핑하므로 기본 생성자가 필요
    public WiseSaying() {}

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public void setContent(String content) { this.content = content; }

    public void setAuthor(String author) { this.author = author; }

}
