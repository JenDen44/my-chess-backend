package com.chess.jnd.converter;

import com.chess.jnd.entity.figures.ShortFigureName;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class ShortFigureNameConverter implements AttributeConverter<ShortFigureName, Character> {
    @Override
    public Character convertToDatabaseColumn(ShortFigureName shortFigureName) {
        if (shortFigureName == null) return null;

        return shortFigureName.getShortName();
    }

    @Override
    public ShortFigureName convertToEntityAttribute(Character shortName) {
        if (shortName == null) return null;

        return Stream.of(ShortFigureName.values())
                .filter(c -> c.getShortName().equals(shortName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}