package com.chess.jnd.converter;

import com.chess.jnd.entity.GameRedis;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

@ReadingConverter
@Component
public class BytesToGameConverter implements Converter<byte[], GameRedis> {

    private final Jackson2JsonRedisSerializer<GameRedis> serializer;

    public BytesToGameConverter() {
        serializer = new Jackson2JsonRedisSerializer<GameRedis>(GameRedis.class);
        ObjectMapper mapper = new ObjectMapper();

        serializer.setObjectMapper(mapper);
    }

    @Override
    public GameRedis convert(byte[] value) {
        return serializer.deserialize(value);
    }
}
