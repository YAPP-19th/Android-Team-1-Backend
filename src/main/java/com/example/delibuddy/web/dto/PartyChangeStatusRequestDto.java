package com.example.delibuddy.web.dto;

import com.example.delibuddy.domain.party.PartyStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;

import static com.example.delibuddy.util.GeoHelper.wktToGeometry;


@Data
@RequiredArgsConstructor
public class PartyChangeStatusRequestDto {
    private final String status;
}
