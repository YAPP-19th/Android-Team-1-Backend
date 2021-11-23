package com.example.delibuddy.web.dto;

import com.example.delibuddy.domain.category.Category;
import com.example.delibuddy.domain.party.Party;
import com.example.delibuddy.domain.user.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;



@Data
@RequiredArgsConstructor // TODO 없애기
public class PartyResponseDto {
    private final long id;
    private final String title;
    private final String body;
    private final String coordinate;

    @JsonSerialize(using = CategorySerializer.class)
    private final CategoryResponseDto category;

    private final Integer targetUserCount;

    public PartyResponseDto(Party entity) {
        id = entity.getId();
        title = entity.getTitle();
        body = entity.getBody();
        coordinate = "POINT (" + entity.getCoordinate().getX() + " " + entity.getCoordinate().getY() + ")"; // TODO: coordinate 에 예시 넣기
        category = new CategoryResponseDto(entity.getCategory());
        targetUserCount = entity.getTargetUserCount();
    }
}

class CategorySerializer extends JsonSerializer<Category> {
    @Override
    public void serialize(Category value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());
        gen.writeStringField("name", value.getName());
        gen.writeEndObject();
    }
}