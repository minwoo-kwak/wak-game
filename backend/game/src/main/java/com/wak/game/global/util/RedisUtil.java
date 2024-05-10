package com.wak.game.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();
    //C U
    public void saveData(String key, String hashkey, Object data) {
        redisTemplate.opsForHash().put(key, hashkey, data);
    }
    public void saveList(String key, String hashKey, List<Object> list) {
        try {
            String listAsString = objectMapper.writeValueAsString(list);
            saveData(key, hashKey, listAsString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing list to JSON", e);
        }
    }

    public <T> Map<String, T> getData(String key, Class<T> classType) {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        Map<String, T> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            result.put((String) entry.getKey(), classType.cast(entry.getValue()));
        }
        return result;
    }
    public <T> List<T> getListData(String key, Class<T> classType) {
        List<Object> rawList = redisTemplate.opsForList().range(key, 0, -1);
        return rawList.stream().map(classType::cast).collect(Collectors.toList());
    }

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    public void deleteKey(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    public void deleteField(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }
}
