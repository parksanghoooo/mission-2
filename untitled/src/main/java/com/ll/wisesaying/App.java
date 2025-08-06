package com.ll.wisesaying;

import com.ll.wisesaying.global.constant.Command;
import com.ll.wisesaying.global.constant.Message;

import java.io.*;

public class App {

    private final BufferedReader br;

    public App() {
        this.br = new BufferedReader(new InputStreamReader(System.in));
    }

    public void run() throws IOException {

        System.out.println(Message.APP_TITLE);

        while (true) {

            System.out.print(Message.INPUT);
            String cmd = br.readLine().trim();

            if (cmd.equals(Command.QUIT)) break;

        }

    }

}
