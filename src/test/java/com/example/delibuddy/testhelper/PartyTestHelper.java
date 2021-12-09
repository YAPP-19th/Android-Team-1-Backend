package com.example.delibuddy.testhelper;

import com.example.delibuddy.domain.party.Party;
import com.example.delibuddy.domain.party.PartyRepository;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;

import static com.example.delibuddy.util.GeoHelper.wktToGeometry;

public class PartyTestHelper {

    public static Party createPartyWithPointString(PartyRepository partyRepository, String pointString){
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

}
