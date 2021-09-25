package com.example.delibuddy.web.dto;

import com.example.delibuddy.domain.party.Party;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;

import static com.example.delibuddy.util.GeoHelper.wktToGeometry;

@Data
@RequiredArgsConstructor
public class PartyCreationRequestDto {
    private final String title;
    private final String body;
    private final String coordinate;

    public Party toEntity() {
        Point point;
        try {
            point = (Point) wktToGeometry("POINT " + coordinate);
        } catch (ParseException e) {
            throw new IllegalArgumentException("coordinate 값이 잘못되었습니다.");
        }

        return Party.builder().title(title).body(body).coordinate(point).build();
    }
}
