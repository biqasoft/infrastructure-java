/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.persistence.base;

import com.biqasoft.entity.constants.DATE_CONSTS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.DayOfWeek;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * Parse internal representation of Date constants used in API
 * see constants {@code {@link DATE_CONSTS}}
 * see constants used as string {@code {@link com.biqasoft.entity.system.BaseClass }}
 * see usage this service example {@code {@link BiqaObjectFilterService }}
 */
@Service
public class DateService {

    private int defaultTimeZoneOffset = 0;

    public DateService(@Value("${biqa.time.default.zone.offset:0}") Integer defaultTimeZoneOffset) {
        this.defaultTimeZoneOffset = defaultTimeZoneOffset;
    }

    public Date parseDateExpression(String dateExp) {
        if (StringUtils.isEmpty(dateExp)) return null;

        ZonedDateTime nowUTC = ZonedDateTime.now(ZoneOffset.UTC);
        nowUTC.plusHours(defaultTimeZoneOffset);

        switch (dateExp) {
            case DATE_CONSTS.CURRENT_DAY_START:
                return new Date(nowUTC.withHour(0).withMinute(0).withSecond(0).toEpochSecond());

            case DATE_CONSTS.CURRENT_DAY_END:
                return new Date(nowUTC.withHour(23).withMinute(59).withSecond(59).toEpochSecond());

            case DATE_CONSTS.TOMORROW_DAY_START:
                return new Date(nowUTC.plusDays(1).withHour(0).withMinute(0).withSecond(0).toEpochSecond());

            case DATE_CONSTS.TOMORROW_DAY_END:
                return new Date(nowUTC.plusDays(1).withHour(23).withMinute(59).withSecond(59).toEpochSecond());

            case DATE_CONSTS.CURRENT_MONTH_START:
                return new Date(nowUTC.with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0).toEpochSecond());

            case DATE_CONSTS.CURRENT_MONTH_END:
                return new Date(nowUTC.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59).toEpochSecond());

            case DATE_CONSTS.CURRENT_WEEK_START:
//                TODO: internalization: week start/end
                return new Date(nowUTC.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).withHour(0).withMinute(0).withSecond(0).toEpochSecond());

            case DATE_CONSTS.CURRENT_WEEK_END:
//                TODO: internalization: week start/end
                return new Date(nowUTC.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).withHour(0).withMinute(0).withSecond(0).toEpochSecond());

            default:
                return new Date(ZonedDateTime.parse(dateExp).toEpochSecond());

        }
    }

}
