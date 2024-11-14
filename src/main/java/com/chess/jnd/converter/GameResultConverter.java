package com.chess.jnd.converter;

import com.chess.jnd.entity.GameResult;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class GameResultConverter implements AttributeConverter<GameResult, String> {

        @Override
        public String convertToDatabaseColumn(GameResult gameResult) {
            if (gameResult == null) return null;

            return gameResult.getResult();
        }

        @Override
        public GameResult convertToEntityAttribute(String gameResult) {
            if (gameResult == null) return null;

            return Stream.of(GameResult.values())
                    .filter(c -> c.getResult().equals(gameResult))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
}