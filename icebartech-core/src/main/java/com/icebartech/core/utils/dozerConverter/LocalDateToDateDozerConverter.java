package com.icebartech.core.utils.dozerConverter;

import org.dozer.DozerConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author Anler
 * @Date 2019/2/16 21:20
 * @Description
 */
public class LocalDateToDateDozerConverter extends DozerConverter<LocalDate, Date> {
    public LocalDateToDateDozerConverter() {
        super(LocalDate.class, Date.class);
    }

    @Override
    public LocalDate convertFrom(Date source, LocalDate destination) {
        if (null == source) {
            return null;
        }
        return LocalDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault()).toLocalDate();
    }

    @Override
    public Date convertTo(LocalDate source, Date destination) {
        if (null == source) {
            return null;
        }
        return Date.from(source.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}
