package com.wak.game.global.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveData(String key, String hashkey, Object data) {
        redisTemplate.opsForHash().put(key, hashkey, data);
    }

    public <T> Map<String, T> getData(String key, Class<T> classType) {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        Map<String, T> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            result.put((String) entry.getKey(), classType.cast(entry.getValue()));
        }
        return result;
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
