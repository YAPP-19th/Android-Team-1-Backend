package com.example.delibuddy.web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;

import static com.example.delibuddy.util.GeoHelper.wktToGeometry;


@Data
@NoArgsConstructor
public class PartyEditRequestDto {
    // serializer 처럼 필드가 자동생성되면 얼마나 좋을까 ㅎㅎ
    // TODO: serializer 가 어떻게 필드를 자동생성하는지 알아보자.

    private String title;
    private String body;
    private String coordinate;

    @Builder
    public PartyEditRequestDto(String title, String body, String coordinate) {
        this.title = title;
        this.body = body;
        this.coordinate = coordinate;
    }

    public Point getPoint() {
        try {
            return (Point) wktToGeometry("POINT " + coordinate);
        } catch (ParseException e) {
            throw new IllegalArgumentException("coordinate 값이 잘못되었습니다.");
        }
    }

}
