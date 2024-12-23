package com.chess.jnd.converter;

import com.chess.jnd.entity.GameRedis;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

@WritingConverter
@Component
public class GameToBytesConverter implements Converter<GameRedis, byte[]> {

    private final Jackson2JsonRedisSerializer<GameRedis> serializer;

    private final ObjectMapper mapper;

    public GameToBytesConverter(ObjectMapper mapper) {
        this.mapper = mapper;

        serializer = new Jackson2JsonRedisSerializer<GameRedis>(GameRedis.class);

        serializer.setObjectMapper(mapper);
    }

    @Override
    public byte[] convert(GameRedis value) {
        return serializer.serialize(value);
    }
}
