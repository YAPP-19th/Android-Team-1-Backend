package com.example.delibuddy.util;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;

import static com.example.delibuddy.util.GeoHelper.wktToGeometry;
import static org.junit.jupiter.api.Assertions.*;

class GeoHelperTest {

    @Test
    public void point_wkt_로_geometry_를_생성하면_Point_반환() throws ParseException {
        Geometry geometry = wktToGeometry("POINT (2 5)");

        assertEquals("Point", geometry.getGeometryType());
        assertTrue(geometry instanceof Point);
    }

}
