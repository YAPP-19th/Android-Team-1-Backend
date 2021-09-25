package com.example.delibuddy.service;

import com.example.delibuddy.domain.party.Party;
import com.example.delibuddy.domain.party.PartyRepository;
import com.example.delibuddy.web.dto.PartyResponseDto;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static com.example.delibuddy.util.GeoHelper.wktToGeometry;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PartyServiceTest {

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private PartyService partyService;

    private Party createParty() {
        Point point;
        try {
            point = (Point) wktToGeometry("POINT (1 1)");
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
    void getParty() {
        // Given: party 2개 생성
        Party party1 = createParty();
        Party party2 = createParty();

        // When: party service 로 쿼리
        PartyResponseDto partyDto = partyService.getParty(party1.getId());

        // Then: party1 의 Dto 를 얻는다.
        assertThat(new PartyResponseDto(party1)).isEqualTo(partyDto);
    }
}
