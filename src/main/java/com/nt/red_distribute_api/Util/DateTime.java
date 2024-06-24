package com.nt.red_distribute_api.Util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTime {

    public static final Timestamp getTimeStampNow(){
        Instant instant = Instant.now();
        return Timestamp.from(instant);
    }

    public static final FromToTimeStamp getTimeStampByPrefix(String prefix){
        FromToTimeStamp fromTo = new FromToTimeStamp();
        // Current LocalDateTime
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String startFormatted;
        String endFormatted;

        switch (prefix) {
            case "td":
                // Start LocalDateTime 
                startDateTime = currentDateTime.toLocalDate().atStartOfDay();

                // End LocalDateTime
                endDateTime = currentDateTime;
                
                startFormatted = startDateTime.format(formatter);
                endFormatted = endDateTime.format(formatter);
                System.out.println("startFormatted: "+startFormatted);
                System.out.println("endFormatted: "+endFormatted);
                fromTo.setFrom(startFormatted);
                fromTo.setTo(endFormatted);
                return fromTo;
            case "yd":
                // Start LocalDateTime 
                startDateTime = currentDateTime.minusDays(2).toLocalDate().atTime(LocalTime.MAX).plusSeconds(1);

                // End LocalDateTime
                endDateTime = currentDateTime.minusDays(1).toLocalDate().atStartOfDay();

                startFormatted = startDateTime.format(formatter);
                endFormatted = endDateTime.format(formatter);
                System.out.println("startFormatted: "+startFormatted);
                System.out.println("endFormatted: "+endFormatted);
                fromTo.setFrom(startFormatted);
                fromTo.setTo(endFormatted);
                return fromTo;
            case "7d":
                return fromTo;
            case "30d":
                return fromTo;
            default:
                return fromTo;
        } 


        
    }
}
