package com.ll.wisesaying.domain.wiseSaying.controller;

import com.ll.wisesaying.domain.wiseSaying.model.entity.WiseSaying;
import com.ll.wisesaying.domain.wiseSaying.service.WiseSayingService;
import com.ll.wisesaying.global.constant.ErrorMessage;
import com.ll.wisesaying.global.constant.Message;

import java.io.BufferedReader;
import java.util.List;

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
        if (service.isExistById(id))
            System.out.printf(ErrorMessage.NOT_EXIST_WISE_SAYING, id);

        boolean result = service.deleteById(id);
        if (result)
            System.out.printf(Message.DELETE_SUCCESS, id);
        else
            System.out.printf(ErrorMessage.NOT_EXIST_WISE_SAYING, id);
    }

}
