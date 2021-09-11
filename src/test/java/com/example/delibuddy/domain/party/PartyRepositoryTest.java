package com.example.delibuddy.domain.party;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PartyRepositoryTest {

    @Autowired
    PartyRepository partyRepository;

    @Test
    public void party_등록() {
        // Given
        Double x = 127.02558;
        Double y = 37.30160;
        Point coordinate = new Point(x, y);
        partyRepository.save(
            Party.builder()
                    .coordinate(coordinate)
                    .title("")
                    .body("")
                    .build()
        );

        // When
        List<Party> partyList = partyRepository.findAll();

        // Then
        Party party = partyList.get(0);
        assertEquals(x, party.getCoordinate().getX());
        assertEquals(y, party.getCoordinate().getY());
    }

    @Test
    public void 선릉역_주변의_2km_를_탐색하면_역삼역과_상섬역이_나온다() {

    }

}
