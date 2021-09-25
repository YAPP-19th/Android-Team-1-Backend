package com.example.delibuddy.domain.party;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.delibuddy.util.GeoHelper.wktToGeometry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PartyRepositoryTest {

    @Autowired
    PartyRepository partyRepository;

    private Party createPartyWithPointString(String pointString){
        Point point;
        try {
            point = (Point) wktToGeometry(pointString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        Party party = Party.builder()
                .coordinate(point)
                .title("test title")
                .body("")
                .build();
        partyRepository.save(party);
        return party;
    }

    @Test
    public void 파티_등록() {
        // Given
        String pointString = "POINT (127.02558 37.3016)";
        createPartyWithPointString(pointString);

        // When
        List<Party> partyList = partyRepository.findAll();

        // Then
        Party party = partyList.get(0);
        assertEquals(pointString, party.getCoordinate().toString());
    }

    @Test
    public void findPartiesNear_작동_테스트() {
        Party party1 = createPartyWithPointString("POINT (1 1)");
        createPartyWithPointString("POINT (1 2)");
        createPartyWithPointString("POINT (3 4)");
        createPartyWithPointString("POINT (5 6)");

        List<Party> parties = partyRepository.findPartiesNear("POINT (1 1)", 2000);
        assertEquals(parties.size(), 1);
        assertEquals(parties.get(0).getId(), party1.getId());
    }

    @Test
    public void findPartiesInGeom_작동_테스트() {
        Party party1 = createPartyWithPointString("POINT (1 1)");
        Party party2 = createPartyWithPointString("POINT (1 2)");
        createPartyWithPointString("POINT (3 4)");
        createPartyWithPointString("POINT (5 6)");

        List<Party> parties = partyRepository.findPartiesInGeom("Polygon((0 0, 0 3, 3 3, 3 0, 0 0))");
        assertThat(parties).containsOnly(party1, party2);
    }

    @Test
    public void 선릉역_주변의_2km_를_탐색하면_선릉역과_역삼역이_나온다() {
        // Given: 선릉 역삼 마두
        String 선릉_point = "POINT (127.048995 37.504506)";
        Party 선릉 = createPartyWithPointString(선릉_point);
        Party 역삼 = createPartyWithPointString("POINT (127.036420 37.500613)");
        Party 마두 = createPartyWithPointString("POINT (126.777748 37.651965)");
        partyRepository.saveAll(Arrays.asList(선릉, 역삼, 마두));

        // When: 선릉 근처 2km 를 쿼리
        List<Party> parties = partyRepository.findPartiesNear(선릉_point, 2000);

        // Then: 선릉역과 마두역이 결과로 나온다.
        List<Long> ids = parties.stream().map(Party::getId).collect(Collectors.toList());
        assertEquals(ids.size(), 2);
        assertTrue(ids.contains(선릉.getId()));
        assertTrue(ids.contains(역삼.getId()));
    }

}
