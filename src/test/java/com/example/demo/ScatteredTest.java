package com.example.demo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class ScatteredTest {
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

}
