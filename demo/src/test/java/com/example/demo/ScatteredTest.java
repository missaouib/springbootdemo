package com.example.demo;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class ScatteredTest {
    public static void main(String[] args) {
        dateTime();
    }

    /**
     * 获取第二天零点时间戳
     *
     * @return
     */
    public long getExpireTimeStamp() {
        LocalDate localDate = LocalDate.now();
        localDate = localDate.plusDays(1);
        LocalDateTime dateTime = LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), 0, 0, 0);
        long timestamp = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return timestamp;
    }

    public void timeStamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(formatter.format(LocalDateTime.now()));
        System.out.println(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
    }

    public Long getTime() {
        Long now = new Date().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.DAY_OF_MONTH, 5);
        calendar.set(Calendar.HOUR, 14);
        calendar.set(Calendar.MINUTE, 23);
        calendar.set(Calendar.SECOND, 6);
        calendar.set(Calendar.MILLISECOND, 138);
        return calendar.getTimeInMillis();
    }

    public static void dateTime() {
        //获取今天的日期
        LocalDate localData = LocalDate.now();// 2021-06-01
        System.out.println(localData);
        //使用年月日构造一个日期
        LocalDate valentineDay = LocalDate.of(2021, 6, 1); // 月份和日期是从1开始

        // 指定对象，获取年、月、日、星期几
        int year = localData.getYear();
        Month month = localData.getMonth();
        int day = localData.getDayOfMonth();
        DayOfWeek dayOfWeek = localData.getDayOfWeek();


        //初始化时间对象
        LocalTime localTime = LocalTime.now();// 14:46:57.441581400
        System.out.println(localTime);
        //使用时分秒构造一个对象
        LocalTime localTime1 = LocalTime.of(12, 0, 0);


        //LocalDateTime
        //获取日期+时间，年月日+时分秒，含义等于LocalDate+LocalTime

        //创建时间对象
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);
        //使用LocalDate结合LocalTime构造时间对象
        LocalDateTime localDateTime1 = LocalDateTime.of(localData, localTime);
        System.out.println(localDateTime1);//2021-06-01T14:56:06.306716500
        LocalDateTime localDateTime2 = localData.atTime(localTime);
        System.out.println(localDateTime2);//2021-06-01T14:56:06.306716500
        LocalDateTime localDateTime3 = localTime.atDate(localData);
        System.out.println(localDateTime3);//2021-06-01T14:56:06.306716500

        LocalDateTime localDateTime4 = LocalDateTime.of(2021, 6, 1, 0, 0, 0);
        System.out.println(localDateTime4);//2021-06-01T00:00
    }

    public static long getTodayStartTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MIN).toEpochSecond(ZoneOffset.of("+8"));
    }

    public static long getTodayEndTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MAX).toEpochSecond(ZoneOffset.of("+8"));
    }

    // 使用日期和时间构造日期时间对象->修改日期->指定时区转换成时间戳
    public static long getYesterdayStartTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(1).toEpochSecond(ZoneOffset.of("+8"));
    }

    // 使用时间戳构造日期时间对象->修改日期->指定时区转换成时间戳
    public static long getDayStartTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(1).toEpochSecond(ZoneOffset.of("+8"));
    }

}
