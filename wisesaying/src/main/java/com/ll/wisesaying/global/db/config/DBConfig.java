package com.ll.wisesaying.global.db.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DBConfig {

    private static final String DB_PATH = "db/wiseSaying/";
    public static final Path LAST_ID_FILE = Paths.get(DB_PATH, "lastId.txt");

    public static Path getWiseSayingFilePath(long id) {
        return Paths.get(DB_PATH, id + ".json");
    }

}
