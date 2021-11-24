package com.example.delibuddy.web.dto;

import com.example.delibuddy.domain.party.Party;
import com.example.delibuddy.domain.user.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;

import static com.example.delibuddy.util.GeoHelper.wktToGeometry;

@Data
@NoArgsConstructor
public class PartyCreationRequestDto {
    private String title;
    private String body;
    private String coordinate;

    @Builder
    public PartyCreationRequestDto(String title, String body, String coordinate) {
        this.title = title;
        this.body = body;
        this.coordinate = coordinate;
    }

    public Party toEntity(User leader) {
        Point point;
        try {
            point = (Point) wktToGeometry("POINT " + coordinate);
        } catch (ParseException e) {
            throw new IllegalArgumentException("coordinate 값이 잘못되었습니다.");
        }

        return Party.builder().title(title).body(body).leader(leader).coordinate(point).build();
    }
}
