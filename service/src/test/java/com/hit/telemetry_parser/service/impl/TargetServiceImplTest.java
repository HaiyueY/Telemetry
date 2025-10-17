package com.hit.telemetry_parser.service.impl;

import com.hit.telemetry_parser.common.OrekitConstants;
import com.hit.telemetry_parser.config.OrekitConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.orekit.bodies.GeodeticPoint;

import java.util.ArrayList;
import java.util.List;

import static org.hipparchus.util.FastMath.PI;
import static org.hipparchus.util.FastMath.cos;

class TargetServiceImplTest {

    @BeforeAll
    static void init() {
        OrekitConfig.init();
    }

    @Test
    void getOctagonVertices() {
        // 入参
        double latitude = 39.9109;
        double longitude = 116.3823;
        double radius = 15537.7083;

        List<GeodeticPoint> vertices = new ArrayList<>();
        // 45 degrees in radians
        double angleIncrement = PI / 4;
        double offset = radius / cos(PI / 8);

        for (int i = 0; i < 8; i++) {
            double angle = i * angleIncrement;
            double latOffset = offset / OrekitConstants.EARTH_SHAPE.getEquatorialRadius() * Math.cos(angle);
            double lonOffset = offset / (OrekitConstants.EARTH_SHAPE.getEquatorialRadius()
                    * Math.cos(Math.toRadians(latitude))) * Math.sin(angle);

            vertices.add(new GeodeticPoint(
                    Math.toRadians(latitude + Math.toDegrees(latOffset)),
                    Math.toRadians(longitude + Math.toDegrees(lonOffset)),
                    0)); // Altitude set to 0
        }

        System.out.println(vertices);
    }
}