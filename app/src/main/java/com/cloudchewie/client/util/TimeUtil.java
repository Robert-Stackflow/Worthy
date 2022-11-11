package com.cloudchewie.client.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtil {
    private static final TimeZone timeZone = TimeZone.getTimeZone("GMT+8");

    @NonNull
    public static com.haibin.calendarview.Calendar getCalendar(int year, int month, int day) {
        com.haibin.calendarview.Calendar calendar = new com.haibin.calendarview.Calendar();
        calendar.setDay(day);
        calendar.setMonth(month);
        calendar.setYear(year);
        return calendar;
    }

    @NonNull
    public static com.haibin.calendarview.Calendar getNowCalendar() {
        Calendar javaCalendar = Calendar.getInstance();
        com.haibin.calendarview.Calendar calendar = new com.haibin.calendarview.Calendar();
        calendar.setDay(javaCalendar.get(Calendar.DATE));
        calendar.setMonth(javaCalendar.get(Calendar.MONTH));
        calendar.setYear(javaCalendar.get(Calendar.YEAR));
        return calendar;
    }

    @NonNull
    public static Calendar getJavaCalendar(com.haibin.calendarview.Calendar calendar) {
        Calendar javaCalendar = Calendar.getInstance();
        javaCalendar.setTimeZone(timeZone);
        javaCalendar.set(calendar.getYear(), calendar.getMonth() - 1, calendar.getDay());
        return javaCalendar;
    }

    @NonNull
    public static Calendar getJavaCalendar(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(timeZone);
        calendar.set(year, month, day);
        return calendar;
    }

    @Nullable
    public static Calendar getJavaCalendar(@NonNull String value) {
        String[] strings = value.split("-");
        return value == null ? null : TimeUtil.getJavaCalendar(strings[0], strings[1], strings[2]);
    }

    @NonNull
    public static Calendar getJavaCalendar(String year, String month, String day) {
        return getJavaCalendar(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
    }

    @NonNull
    public static String calendarToString(@NonNull Calendar calendar) {
        return String.valueOf(calendar.get(Calendar.YEAR)) + '-' + (calendar.get(Calendar.MONTH)) + '-' + calendar.get(Calendar.DATE);
    }

    @NonNull
    public static com.haibin.calendarview.Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        com.haibin.calendarview.Calendar calendar = new com.haibin.calendarview.Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);
        calendar.setScheme(text);
        return calendar;
    }

    @NonNull
    public static com.haibin.calendarview.Calendar getSchemeCalendar(Calendar javaCalendar, int color, String text) {
        return getSchemeCalendar(javaCalendar.get(Calendar.YEAR), javaCalendar.get(Calendar.MONTH), javaCalendar.get(Calendar.DATE), color, text);
    }

    public static Calendar getCalendar() {
        Calendar cd = Calendar.getInstance();
        cd.setTimeZone(timeZone);
        cd.set(Calendar.MONTH, cd.get(Calendar.MONTH) + 1);
        return cd;
    }

    public static int getYear() {
        Calendar cd = Calendar.getInstance();
        cd.setTimeZone(timeZone);
        return cd.get(Calendar.YEAR);
    }

    public static int getYear(Date date) {
        Calendar cd = Calendar.getInstance();
        cd.setTimeZone(timeZone);
        cd.setTime(date);
        return cd.get(Calendar.YEAR);
    }

    public static int getMonth() {
        Calendar cd = Calendar.getInstance();
        cd.setTimeZone(timeZone);
        return cd.get(Calendar.MONTH) + 1;
    }

    public static int getMonth(Date date) {
        Calendar cd = Calendar.getInstance();
        cd.setTimeZone(timeZone);
        cd.setTime(date);
        return cd.get(Calendar.MONTH) + 1;
    }

    public static int getDay() {
        Calendar cd = Calendar.getInstance();
        cd.setTimeZone(timeZone);
        return cd.get(Calendar.DATE);
    }

    public static int getDay(Date date) {
        Calendar cd = Calendar.getInstance();
        cd.setTimeZone(timeZone);
        cd.setTime(date);
        return cd.get(Calendar.DATE);
    }

    public static int getDayOfWeek(Date date) {
        Calendar cd = Calendar.getInstance();
        cd.setTimeZone(timeZone);
        cd.setTime(date);
        return (cd.get(Calendar.DAY_OF_WEEK) + 5) % 7;
    }

    public static int getDayOfWeek() {
        Calendar cd = Calendar.getInstance();
        cd.setTimeZone(timeZone);
        return cd.get(Calendar.DAY_OF_WEEK);
    }

    public static int getHour() {
        Calendar cd = Calendar.getInstance();
        cd.setTimeZone(timeZone);
        return cd.get(Calendar.HOUR);
    }

    public static int getHour(Date date) {
        Calendar cd = Calendar.getInstance();
        cd.setTimeZone(timeZone);
        cd.setTime(date);
        return cd.get(Calendar.HOUR);
    }

    public static int getMinute() {
        Calendar cd = Calendar.getInstance();
        cd.setTimeZone(timeZone);
        return cd.get(Calendar.MINUTE);
    }

    public static int getMinute(Date date) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.setTimeZone(timeZone);
        return cd.get(Calendar.MINUTE);
    }

    @NonNull
    public static Date getStartOfDay(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(timeZone);
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    @NonNull
    public static Date getEndOfDay(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(timeZone);
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static int getIntervalDay(Date date1, Date date2) {
        try {
            long startTime, endTime;
            if (date1.before(date2)) {
                startTime = date1.getTime();
                endTime = date2.getTime();
            } else {
                startTime = date2.getTime();
                endTime = date1.getTime();
            }
            return (int) ((endTime - startTime) / (1000 * 60 * 60 * 24));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getIntervalMinute(Date date1, Date date2) {
        try {
            long startTime, endTime;
            startTime = date1.getTime();
            endTime = date2.getTime();
            return (int) ((endTime - startTime) / (1000 * 60));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
