package com.hit.telemetry_parser.utils;

import com.hit.telemetry_parser.config.OrekitConfig;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.time.UTCScale;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间工具类
 *
 * @author Yang_Haiyue
 * @author 卫星技术研究所_哈尔滨工业大学
 * @since 2023/4/24 14:37
 */
public class TimeUtils {

    public static Calendar getUTCToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(ZoneId.of("UTC")));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, 0);
        return calendar;
    }

    public static Calendar getUTCTomorrow() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(ZoneId.of("UTC")));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, 1);
        return calendar;
    }

    public static Calendar getUTCTheDayAfter() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(ZoneId.of("UTC")));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, 2);
        return calendar;
    }

    public static Calendar getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(ZoneId.of("GMT+8")));
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, -1);
        return calendar;
    }

    public static Calendar getToday() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("GMT+8")));
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, 0);
        return calendar;
    }

    public static Calendar getTomorrow() {
        Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone(ZoneId.of("GMT+8")));
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, 1);
        return calendar;
    }

    public static Calendar getTheDayAfter() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("GMT+8")));
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, 2);
        return calendar;
    }

    public static AbsoluteDate calendarToAbsDate(Calendar calendar) {
        return new AbsoluteDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DATE), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND), TimeScalesFactory.getUTC());
    }

    public static AbsoluteDate dateToAbsDate(Date date) {
        return calendarToAbsDate(dateToCalendar(date)).shiftedBy(-8 * 60 * 60);
    }

    public static Date calendarToDate(Calendar calendar) {
        return Date.from(calendar.toInstant());
    }

    public static Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(ZoneId.of("GMT+8")));
        calendar.setTime(date);
        return calendar;
    }

    public static void main(String[] args) {
        OrekitConfig.init();
        AbsoluteDate absoluteDate = new AbsoluteDate(2024, 10, 12, 0, 0, 0., TimeScalesFactory.getUTC());
        Date date = absoluteDate.toDate(TimeScalesFactory.getUTC());
        AbsoluteDate absoluteDate1 = dateToAbsDate(date);
        String stringRfc3339 = absoluteDate1.toStringRfc3339(TimeScalesFactory.getUTC());
    }

    public static String getISO8601String(Date date) {
        return TimeUtils.dateToAbsDate(date).toStringRfc3339(TimeScalesFactory.getUTC());
    }

    public static String getISO8601String(AbsoluteDate date) {
        return date.toStringRfc3339(TimeScalesFactory.getUTC());
    }

    public static AbsoluteDate getDateFromMillis(Long unixTimestampMillis, UTCScale timeScale) {
        Instant instant = Instant.ofEpochMilli(unixTimestampMillis);
        return new AbsoluteDate(instant, timeScale);
    }
}
