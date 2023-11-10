package com.deeeelete.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeTools {
    public TimeTools() {
    }

    public static String transformDateFormat(Date dateTime, String dateFormat) throws ParseException {
        if (dateTime == null) {
            dateTime = new Date();
        }

        if (dateFormat == null) {
            dateFormat = "yyyy-MM-dd HH:mm:ss";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String returnStr = sdf.format(dateTime);
        sdf.parse(returnStr);
        return returnStr;
    }

    public static String transformDateFormatStringToString(String dateTime, String dateFormat) throws ParseException {
        if (StringUtil.isEmpty(dateTime)) {
            dateTime = transformDateFormat(new Date(), dateFormat);
        }

        if (dateFormat == null) {
            dateFormat = "yyyy-MM-dd HH:mm:ss";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(dateTime);
    }

    public static Date transformDateFormatStringToDate(String dateTime, String dateFormat) throws ParseException {
        if (dateFormat == null) {
            dateFormat = "yyyy-MM-dd HH:mm:ss";
        }

        if (StringUtil.isEmpty(dateTime)) {
            dateTime = transformDateFormat(new Date(), dateFormat);
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.parse(dateTime);
    }

    public static Date transformDateFormatStringToDateUS(String dateTime, String dateFormat) throws ParseException {
        if (dateFormat == null) {
            dateFormat = "EEE MMM dd HH:mm:ss zzz yyyy";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        return sdf.parse(dateTime);
    }

    public static Date transformDateFormatDateToDate(Date dateTime, String dateFormat) throws ParseException {
        if (dateFormat == null) {
            dateFormat = "yyyy-MM-dd HH:mm:ss";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.parse(transformDateFormat(dateTime, dateFormat));
    }

    public static Calendar newCalendar(Date dateTime) {
        Calendar cal = Calendar.getInstance();
        if (dateTime != null) {
            cal.setTime(dateTime);
        }

        return cal;
    }

    public static Date getDateByMoreDay(Date date, Integer day) {
        Calendar cal = newCalendar(date);
        cal.add(5, day);
        date = cal.getTime();
        return date;
    }

    public static Date getDateByMoreMinute(Date date, Integer minute) {
        Calendar cal = newCalendar(date);
        cal.add(12, minute);
        date = cal.getTime();
        return date;
    }

    public static Date getDateByMorehour(Date date, Integer hour) {
        Calendar cal = newCalendar(date);
        cal.add(10, hour);
        date = cal.getTime();
        return date;
    }

    public static String getCron(Date date) {
        String dateFormat = "ss mm HH dd MM ? yyyy";

        try {
            return transformDateFormat(date, dateFormat);
        } catch (ParseException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static Date getEndOfDay(Date date) {
        if (date == null) {
            date = new Date();
        }

        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getStartOfDay(Date date) {
        if (date == null) {
            date = new Date();
        }

        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(1, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    public static Date getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(1, year);
        calendar.roll(6, -1);
        Date currYearLast = calendar.getTime();
        return currYearLast;
    }

    public static Integer getYear(Date date) {
        return getValueByDate(date, 1);
    }

    public static Integer getValueByDate(Date date, int field) {
        if (date == null) {
            date = new Date();
        }

        Calendar cal = newCalendar(date);
        return cal.get(field);
    }

    public static String stampToDate(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time = new Long(s);
        Date date = new Date(time);
        String res = simpleDateFormat.format(date);
        return res;
    }

    public static int getWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(7) - 1;
        if (w == 0) {
            w = 7;
        }

        return w;
    }
}
