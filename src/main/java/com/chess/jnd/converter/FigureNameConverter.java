package com.chess.jnd.converter;

import com.chess.jnd.entity.figures.FigureName;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class FigureNameConverter implements AttributeConverter<FigureName, String>  {

    @Override
    public String convertToDatabaseColumn(FigureName figureName) {
        if (figureName == null) return null;

        return figureName.getName();
    }

    @Override
    public FigureName convertToEntityAttribute(String figureName) {
        if (figureName == null) return null;

        return Stream.of(FigureName.values())
                .filter(c -> c.getName().equals(figureName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}