package com.cloudchewie.client.database;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import com.cloudchewie.client.util.TimeUtil;

import java.util.Calendar;
import java.util.Date;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Calendar fromCalendar(String value) {
        return TimeUtil.getJavaCalendar(value);
    }

    @NonNull
    @TypeConverter
    public static String calendarTo(Calendar date) {
        return TimeUtil.calendarToString(date);
    }
}
