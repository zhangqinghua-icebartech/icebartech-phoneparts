package com.icebartech.core.utils.dozerConverter;

import org.dozer.DozerConverter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author Anler
 * @Date 2019/2/16 21:20
 * @Description
 */
public class LocalDateTimeToDateDozerConverter extends DozerConverter<LocalDateTime, Date> {
    public LocalDateTimeToDateDozerConverter() {
        super(LocalDateTime.class, Date.class);
    }

    @Override
    public LocalDateTime convertFrom(Date source, LocalDateTime destination) {
        if (null == source) {
            return null;
        }
        return LocalDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
    }

    @Override
    public Date convertTo(LocalDateTime source, Date destination) {
        if (null == source) {
            return null;
        }
        return Date.from(source.atZone(ZoneId.systemDefault()).toInstant());
    }
}
