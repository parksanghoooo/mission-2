package com.ll.wisesaying.domain.wiseSaying.controller;

import com.ll.wisesaying.domain.wiseSaying.model.entity.WiseSaying;
import com.ll.wisesaying.domain.wiseSaying.service.WiseSayingService;
import com.ll.wisesaying.global.constant.ErrorMessage;
import com.ll.wisesaying.global.constant.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

    public void getAllWiseSayings() {
        List<WiseSaying> allWiseSayings = service.findAll();

        System.out.println(Message.LIST_HEADER);
        for (WiseSaying wiseSaying : allWiseSayings) {
            System.out.printf(Message.LIST_ROW_FORMAT, wiseSaying.getId(), wiseSaying.getAuthor(), wiseSaying.getContent());
        }
    }

    public void delete(long id) {
        Optional<WiseSaying> optionalWiseSaying = service.findById(id);
        if (optionalWiseSaying.isEmpty()) {
            System.out.printf(ErrorMessage.NOT_EXIST_WISE_SAYING, id);
            return;
        }

        service.deleteById(optionalWiseSaying.get());
        System.out.printf(Message.DELETE_SUCCESS, id);
    }

    public void update(long id) throws IOException {
        Optional<WiseSaying> optionalWiseSaying = service.findById(id);
        if (optionalWiseSaying.isEmpty()) {
            System.out.printf(ErrorMessage.NOT_EXIST_WISE_SAYING, id);
            return;
        }

        WiseSaying wiseSaying = optionalWiseSaying.get();

        System.out.printf(Message.BEFORE_CONTENT, wiseSaying.getContent());
        System.out.print(Message.INPUT_CONTENT);
        String newContent = br.readLine().trim();

        System.out.printf(Message.BEFORE_AUTHOR, wiseSaying.getAuthor());
        System.out.print(Message.INPUT_AUTHOR);
        String newAuthor = br.readLine().trim();

        service.update(wiseSaying, newContent, newAuthor);
    }

}
