package com.example.delibuddy.web.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;

import static com.example.delibuddy.util.GeoHelper.wktToGeometry;


@Data
@RequiredArgsConstructor
public class PartyEditRequestDto {
    // serializer 처럼 필드가 자동생성되면 얼마나 좋을까 ㅎㅎ
    // TODO: serializer 가 어떻게 필드를 자동생성하는지 알아보자.

    private final String title;
    private final String body;
    private final String coordinate;

    public Point getPoint() {
        try {
            return (Point) wktToGeometry(coordinate);
        } catch (ParseException e) {
            throw new IllegalArgumentException("coordinate 값이 잘못되었습니다.");
        }
    }

}
