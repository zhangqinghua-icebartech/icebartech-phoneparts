package com.icebartech.core.utils.dozerConverter;

import org.dozer.DozerConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author Anler
 * @Date 2019/2/16 21:20
 * @Description
 */
public class LocalTimeToDateDozerConverter extends DozerConverter<LocalTime, Date> {
    public LocalTimeToDateDozerConverter() {
        super(LocalTime.class, Date.class);
    }

    @Override
    public LocalTime convertFrom(Date source, LocalTime destination) {
        if (null == source) {
            return null;
        }
        return LocalDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault()).toLocalTime();
    }

    @Override
    public Date convertTo(LocalTime source, Date destination) {
        if (null == source) {
            return null;
        }
        return Date.from(LocalDateTime.of(LocalDate.MIN, source).atZone(ZoneId.systemDefault()).toInstant());
    }
}
