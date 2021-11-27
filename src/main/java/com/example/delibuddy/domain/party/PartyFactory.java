package com.example.delibuddy.domain.party;

import com.example.delibuddy.domain.category.CategoryRepository;
import com.example.delibuddy.domain.user.User;
import com.example.delibuddy.web.dto.PartyCreationRequestDto;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.delibuddy.util.GeoHelper.wktToGeometry;

@Service
public class PartyFactory {

    @Autowired
    private CategoryRepository categoryRepository;

    public Party createParty(PartyCreationRequestDto dto, User leader) {
        Point point;
        try {
            point = (Point) wktToGeometry(dto.getCoordinate());
        } catch (ParseException e) {
            throw new IllegalArgumentException("coordinate 값이 잘못되었습니다.");
        }

        return Party.builder()
            .title(dto.getTitle())
            .body(dto.getBody())
            .targetUserCount(dto.getTargetUserCount())
            .orderTime(dto.getOrderTime())
            .leader(leader)
            .coordinate(point)
            .category(categoryRepository.getById(dto.getCategoryId()))
        .build();

    }
}
