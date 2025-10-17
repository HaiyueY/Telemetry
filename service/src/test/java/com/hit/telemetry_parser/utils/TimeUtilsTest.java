package com.hit.telemetry_parser.utils;

import com.hit.telemetry_parser.config.OrekitConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

class TimeUtilsTest {

    @BeforeAll
    static void init() {
        OrekitConfig.init();
    }

    @Test
    void getUTCTomorrow() {
        Calendar tomorrow = TimeUtils.getUTCTomorrow();
        Calendar tomorrowUTC = TimeUtils.getTomorrow();
        System.out.println(tomorrow.getTime());
        System.out.println(tomorrowUTC.getTime());
        System.out.println(TimeUtils.getISO8601String(TimeUtils.calendarToDate(tomorrow)));
        System.out.println(TimeUtils.getISO8601String(TimeUtils.calendarToDate(tomorrowUTC)));
    }

    @Test
    void getUTCTheDayAfter() {
        Calendar tomorrow = TimeUtils.getUTCTomorrow();
        System.out.println(tomorrow.getTime());
        System.out.println(TimeUtils.getISO8601String(TimeUtils.calendarToDate(tomorrow)));
    }
}