package com.ll.wisesaying.domain.wiseSaying.controller;

import com.ll.wisesaying.domain.wiseSaying.model.entity.WiseSaying;
import com.ll.wisesaying.domain.wiseSaying.service.WiseSayingService;
import com.ll.wisesaying.global.constant.Message;

import java.io.BufferedReader;

public class WiseSayingController {

    private final WiseSayingService service;
    private final BufferedReader br;

    public WiseSayingController(BufferedReader br) {
        this.service = new WiseSayingService();
        this.br = br;
    }

    public void create(String content, String author) {
        WiseSaying saved = service.create(content, author);
        System.out.printf(Message.REGISTER_SUCCESS, saved.getId());
    }

}
