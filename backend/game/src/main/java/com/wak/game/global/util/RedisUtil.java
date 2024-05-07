package com.wak.game.global.util;

import com.wak.game.application.vo.roomVO;
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

    //C U
    public void saveData(String key, String hashkey, Object data) {
        redisTemplate.opsForHash().put(key, hashkey, data);

        Map<String, roomVO> result = getData(key, roomVO.class);
        log.info("2. all entirys: {}" , result);
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
