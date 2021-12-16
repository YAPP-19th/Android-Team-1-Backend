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

    // categoryRepository 가 필요하기 때문에 dto 에서 수행하지 못하고 Factory 를 만들었다.
    // 그러나 나중에 필드 추가 할 때 누락하기가 쉬운 것 같다.
    // python 처럼 자유롭게 dto 에서 model object 를 쿼리할 수 있는게 유지보수 측면에서 (누락을 하지 않는다는 측면에서) 더 좋지 않을까? 2021-12-16 SW

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
            .placeName(dto.getPlaceName())
            .placeNameDetail(dto.getPlaceNameDetail())
            .openKakaoUrl(dto.getOpenKakaoUrl())
            .targetUserCount(dto.getTargetUserCount())
            .orderTime(dto.getOrderTime())
            .leader(leader)
            .coordinate(point)
            .category(categoryRepository.getById(dto.getCategoryId()))
        .build();

    }
}
