package com.ll.wisesaying.domain.wiseSaying.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ll.wisesaying.domain.wiseSaying.model.entity.WiseSaying;
import com.ll.wisesaying.global.constant.ErrorMessage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.ll.wisesaying.global.db.config.DBConfig.LAST_ID_FILE;
import static com.ll.wisesaying.global.db.config.DBConfig.getWiseSayingFilePath;
import static com.ll.wisesaying.global.db.util.JsonUtil.objectMapper;

public class WiseSayingRepository {

    private final List<WiseSaying> wiseSayings = new ArrayList<>();
    private long lastId;

    public WiseSayingRepository() {
        // 저장된 정보 가져오기
        loadLastId();
        loadAll();
    }

    public WiseSaying create(String content, String author) {
        long id = ++lastId;
        WiseSaying wiseSaying = new WiseSaying(id, content, author);
        wiseSayings.add(wiseSaying);

        saveWiseSaying(wiseSaying);
        saveLastId(id);

        return wiseSaying;
    }

    public List<WiseSaying> findAll() {
        List<WiseSaying> descWiseSayings = new ArrayList<>(wiseSayings); // 데이터 복제 (안전 복사)
        descWiseSayings.sort((a, b) -> Long.compare(b.getId(), a.getId()));
        return descWiseSayings;
    }

    private void saveWiseSaying(WiseSaying wiseSaying) {
        try {
            objectMapper.writeValue(getWiseSayingFilePath(wiseSaying.getId()).toFile(), wiseSaying);
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.FAIL_TO_CREATE_FILE, e);
        }
    }

    private void saveLastId(long lastId) {
        try {
            Files.writeString(LAST_ID_FILE, String.valueOf(lastId));
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.FAIL_TO_CREATE_FILE, e);
        }
    }

    private void loadAll() {
        wiseSayings.clear(); // 항상 초기화
        try {
            Files.createDirectories(getWiseSayingFilePath(0).getParent());
            Path dir = getWiseSayingFilePath(0).getParent();
            Path dataJsonPath = dir.resolve("data.json");

            if (Files.exists(dataJsonPath)) {
                // 1. data.json 로드
                List<WiseSaying> list = objectMapper.readValue(
                        dataJsonPath.toFile(),
                        new TypeReference<>() {}
                );
                wiseSayings.addAll(list);
                return;
            }

            // 2. data.json 없고, {id}.json 파일들만 존재하면
            File[] jsonFiles = dir.toFile().listFiles((d, name) ->
                    name.matches("\\d+\\.json")
            );

            if (jsonFiles != null && jsonFiles.length > 0) {
                List<WiseSaying> loadedList = new ArrayList<>();
                for (File file : jsonFiles) {
                    try {
                        WiseSaying ws = objectMapper.readValue(file, WiseSaying.class);
                        loadedList.add(ws);
                    } catch (IOException e) {
                        System.err.printf(ErrorMessage.FAIL_TO_LOAD_FILE, file.getName());
                    }
                }

                wiseSayings.addAll(loadedList);

                // data.json 생성
                objectMapper.writeValue(dataJsonPath.toFile(), loadedList);
            }

            // 3. 아무것도 없으면 빈 리스트
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.FAIL_TO_LOAD_FILE, e);
        }
    }

    private void loadLastId() {
        if (!Files.exists(LAST_ID_FILE)) {
            lastId = 0;
            return;
        }

        try {
            String content = Files.readString(LAST_ID_FILE).trim();
            lastId = Long.parseLong(content);
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException(ErrorMessage.FAIL_TO_LOAD_FILE, e);
        }
    }

}
