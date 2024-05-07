package com.wak.game.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wak.game.application.vo.roomVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();
    //C U
    public void saveData(String key, String hashkey, Object data) {
        redisTemplate.opsForHash().put(key, hashkey, data);

//        Map<String, roomVO> result = getData(key, roomVO.class);
//        log.info("2. all entirys: {}" , result);
    }
    public void saveList(String key, String hashKey, List<Object> list) {
        try {
            String listAsString = objectMapper.writeValueAsString(list);
            saveData(key, hashKey, listAsString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing list to JSON", e);
        }
    }

    //R
    public <T> Map<String, T> getData(String key, Class<T> classType) {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        Map<String, T> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            result.put((String) entry.getKey(), classType.cast(entry.getValue()));
        }
        return result;
    }
    public List<Object> getList(String key, String hashKey) {
        String listAsString = (String) redisTemplate.opsForHash().get(key, hashKey);
        if (listAsString != null) {
            try {
                return objectMapper.readValue(listAsString, new TypeReference<List<Object>>() {});
            } catch (Exception e) {
                throw new RuntimeException("Error deserializing JSON to list", e);
            }
        }
        return new ArrayList<>();
    }


    //D - 전체
    // 단일 키 삭제
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    // 여러 키 삭제
    public void deleteKey(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    //D - map 요소 하나
    public void deleteField(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }
}
