package com.ll.wisesaying;

import com.ll.wisesaying.domain.wiseSaying.controller.WiseSayingController;
import com.ll.wisesaying.global.constant.Command;
import com.ll.wisesaying.global.constant.Message;

import java.io.*;

public class App {

    private final BufferedReader br;
    private final WiseSayingController controller;

    public App() {
        this.br = new BufferedReader(new InputStreamReader(System.in));
        this.controller = new WiseSayingController(br);
    }

    public void run() throws IOException {

        System.out.println(Message.APP_TITLE);

        while (true) {

            System.out.print(Message.INPUT);
            String cmd = br.readLine().trim();

            // 종료
            if (cmd.equals(Command.QUIT)) break;
            // 등록
            else if (cmd.equals(Command.REGISTER)) {
                System.out.print(Message.INPUT_CONTENT);
                String content = br.readLine().trim();

                System.out.print(Message.INPUT_AUTHOR);
                String author = br.readLine().trim();

                controller.create(content, author);
            }
            // 목록
            else if (cmd.equals(Command.LIST)) {
                controller.getAllWiseSayings();
            }
            // 삭제
            else if (cmd.startsWith(Command.DELETE)) {
                long id = extractId(cmd);

                controller.delete(id);
            }
            // 수정
            else if (cmd.startsWith(Command.UPDATE)) {
                long id = extractId(cmd);

                controller.update(id);
            }

        }

    }

    private long extractId(String cmd) {
        int qIdx = cmd.indexOf('?');
        if (qIdx == -1 || qIdx == cmd.length() - 1) return -1;

        String queryPart = cmd.substring(qIdx + 1); // "id=1"
        int eqIdx = queryPart.indexOf('=');
        if (eqIdx == -1) return -1;

        String key = queryPart.substring(0, eqIdx); // "id"
        String value = queryPart.substring(eqIdx + 1); // "1"

        if (!key.equals("id")) return -1;

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}
