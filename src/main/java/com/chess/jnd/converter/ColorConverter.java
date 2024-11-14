package com.chess.jnd.converter;

import com.chess.jnd.entity.Color;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class ColorConverter implements AttributeConverter<Color, String> {
    @Override
    public String convertToDatabaseColumn(Color color) {
        if (color == null) return null;

        return color.getColor();
    }

    @Override
    public Color convertToEntityAttribute(String color) {
        if (color == null) return null;

        return Stream.of(Color.values())
                .filter(c -> c.getColor().equals(color))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}