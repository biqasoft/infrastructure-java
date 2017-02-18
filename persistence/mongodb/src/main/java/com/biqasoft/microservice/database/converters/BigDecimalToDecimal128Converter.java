/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.microservice.database.converters;

import org.bson.types.Decimal128;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;

/**
 * Store {@link BigDecimal} in MongoDB as Decimal128 (require mongodb 3.4)
 * <p>
 * Created by Nikita Bakaev, ya@nbakaev.ru on 5/9/2016.
 * All Rights Reserved
 */
public class BigDecimalToDecimal128Converter implements Converter<BigDecimal, Decimal128> {
    @Override
    public Decimal128 convert(BigDecimal bigDecimal) {
        return new Decimal128(bigDecimal);
    }
}