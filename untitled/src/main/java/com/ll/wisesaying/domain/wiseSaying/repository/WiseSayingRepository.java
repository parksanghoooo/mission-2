package com.ll.wisesaying.domain.wiseSaying.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ll.wisesaying.domain.wiseSaying.model.entity.WiseSaying;
import com.ll.wisesaying.global.constant.ErrorMessage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

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

        save(wiseSaying);
        saveLastId(id);

        return wiseSaying;
    }

    public List<WiseSaying> findAll() {
        List<WiseSaying> descWiseSayings = new ArrayList<>(wiseSayings);
        descWiseSayings.sort((a, b) -> Long.compare(b.getId(), a.getId()));
        return descWiseSayings;
    }

    public Optional<WiseSaying> findById(long id) {
        List<WiseSaying> allWiseSayings = new ArrayList<>(wiseSayings);

        for (WiseSaying wiseSaying : allWiseSayings) {
            if (wiseSaying.getId() == id)
                return Optional.of(wiseSaying);
        }

        return Optional.empty();
    }

    public boolean deleteById(long id) {
        Optional<WiseSaying> optional = findById(id);

        if (optional.isEmpty()) {
            return false;
        }

        wiseSayings.remove(optional.get());

        try {
            Files.deleteIfExists(getWiseSayingFilePath(id));
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.FAIL_TO_DELETE_FILE, e);
        }

        return true;
    }

    private void save(WiseSaying wiseSaying) {
        try {
            // 개별 파일 저장
            objectMapper.writeValue(getWiseSayingFilePath(wiseSaying.getId()).toFile(), wiseSaying);

            // 전체 리스트를 기반으로 data.json 갱신
            saveAll();
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.FAIL_TO_CREATE_FILE, e);
        }
    }

    public void saveAll() {
        try {
            Path dataJsonPath = getWiseSayingFilePath(0).getParent().resolve("data.json");
            objectMapper.writeValue(dataJsonPath.toFile(), wiseSayings);
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
        Path dir = getWiseSayingFilePath(0).getParent();
        Path dataJsonPath = dir.resolve("data.json");

        wiseSayings.clear();

        File[] jsonFiles = dir.toFile().listFiles((d, name) -> name.matches("\\d+\\.json"));
        if (jsonFiles == null || jsonFiles.length == 0) {
            // 아무런 명언 파일도 없다면 빈 리스트로 유지 + data.json 덮어쓰기
            try {
                objectMapper.writeValue(dataJsonPath.toFile(), wiseSayings);
            } catch (IOException e) {
                throw new RuntimeException(ErrorMessage.FAIL_TO_CREATE_FILE, e);
            }
            return;
        }

        List<WiseSaying> loadedList = new ArrayList<>();
        for (File file : jsonFiles) {
            try {
                WiseSaying ws = objectMapper.readValue(file, WiseSaying.class);
                loadedList.add(ws);
            } catch (IOException e) {
                throw new RuntimeException(ErrorMessage.FAIL_TO_LOAD_FILE, e);
            }
        }

        wiseSayings.addAll(loadedList);

        // 항상 data.json 최신화
        try {
            objectMapper.writeValue(dataJsonPath.toFile(), loadedList);
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.FAIL_TO_CREATE_FILE, e);
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
