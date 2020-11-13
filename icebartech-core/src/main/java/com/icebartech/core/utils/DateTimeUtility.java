package com.icebartech.core.utils;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 日期时间工具类
 */
public class DateTimeUtility {

    /**
     * 计算当天当前时间剩余的秒数
     *
     * @return
     */
    public static long getLeavingTime() {
        return ChronoUnit.SECONDS.between(LocalDateTime.now(), LocalDate.now().plusDays(1).atStartOfDay());
    }

    /**
     * 返回今天/昨天文字描述
     *
     * @param date
     * @return
     */
    public static String parseDate(LocalDate date) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        String ret;
        if (today.equals(date)) {
            ret = "今天";
        } else if (today.minusDays(1).equals(date)) {
            ret = "昨天";
        } else {
            ret = date.format(fmt);
        }
        return ret;
    }

    /**
     * 转换YearMonth为LocalDate
     */
    public static LocalDate parseDateYearMonth(YearMonth yearMonth) {
        return LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
    }

    /**
     * 转换LocalDate为YearMonth
     */
    public static YearMonth parseYearMonth(LocalDate localDate) {
        return YearMonth.of(localDate.getYear(), localDate.getMonthValue());
    }


    /**
     * 获取上一个月一号
     *
     * @return
     */
    public static LocalDate getBeforeMonth() {
        //获得上个月今天的日期
        return parseDateYearMonth(YearMonth.now().plusMonths(-1));
    }

    /**
     * 获取日历控件显示的所有日期
     * 该月所有日期 包括该月第一周和最后一周在上下月的日期
     *
     * @param date
     * @return
     */
    public static List<LocalDate> getCalendarDays(LocalDate date) {
        List<LocalDate> days = new ArrayList<>();
        //得到该日期的月
        int month = date.getMonthValue();
        //获得该日期的日
        int today = date.getDayOfMonth();
        //将日期设置为该月的第一天
        date = date.minusDays(today - 1);
        DayOfWeek weekday = date.getDayOfWeek();
        // 得到该月第一天属于星期几
        int value = weekday.getValue();
        //将日期往前推至本月第一周的周一
        date = date.minusDays(value - 1);
        do {
            days.add(date);
            // 往后加一天
            date = date.plusDays(1);
            // 判断 如果到了下月的周一 则结束循环
        } while (!(date.getDayOfWeek().getValue() == 1 && date.getMonthValue() != month));
        return days;
    }

    /**
     * 推迟时间
     * @param data 是按
     * @param year 年
     * @return Date
     */
    public static Date delayTime(Date date,int year){
        Calendar curr = Calendar.getInstance();
        curr.setTime(date);
        curr.set(Calendar.YEAR,curr.get(Calendar.YEAR)+year);
        return curr.getTime();
    }


    public static Date delayTime(LocalDateTime date,int year){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = date.atZone(zoneId);
        Date date1 = Date.from(zdt.toInstant());
        return delayTime(date1,year);
    }

    public static void main(String[] args) {
        SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = sDateFormat.format(delayTime(new Date(),1));
        System.out.println(dateString);
    }


}