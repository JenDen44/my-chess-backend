package com.chess.jnd.converter;

import com.chess.jnd.entity.GameStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class GameStatusConverter implements AttributeConverter<GameStatus, String> {

        @Override
        public String convertToDatabaseColumn(GameStatus gameStatus) {
            if (gameStatus == null) return null;

            return gameStatus.getStatus();
        }

        @Override
        public GameStatus convertToEntityAttribute(String gameStatus) {
            if (gameStatus == null) return null;

            return Stream.of(GameStatus.values())
                    .filter(c -> c.getStatus().equals(gameStatus))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
}