package com.tangerineteam.gyoolworksap.dao;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisTokenDao {

    private final RedisTemplate<String,String> redisTemplate;
    private final ValueOperations<String,String> values;

    public RedisTokenDao(RedisTemplate<String,String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.values = redisTemplate.opsForValue();
    }

    //데이터 저장
    public void setValue(String key,String value){
        values.set(key,value);
    }

    //만료기간 있는 데이터 저장
    public void setValue(String key, String value, Duration expireTime){
        values.set(key,value,expireTime);
    }

    //데이터 조회
    public String getValue(String key){
        return values.get(key);
    }

    //데이터 삭제
    public void delete(String key){
        redisTemplate.delete(key);
    }
}
