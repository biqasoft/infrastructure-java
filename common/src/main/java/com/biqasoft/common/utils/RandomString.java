/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.common.utils;

import java.security.SecureRandom;

/**
 * Generate random string. Can be used to generate secured random string for password
 */
public class RandomString {

    // only english chars
    private static final char[] englishSymbols;

    // english chars and special chars
    private static final char[] symbolsWithSpecial;

    // english chars and special chars and special chars which should be escaped in URL, frontend etc
    private static final char[] symbolsWithSpecialAll;

    // generate password strategy
    private final Strategy strategy;

    // random generator
    private final static SecureRandom random = new SecureRandom();

    private final int length;

    static {
        StringBuilder tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ++ch)
            tmp.append(ch);
        for (char ch = 'a'; ch <= 'z'; ++ch)
            tmp.append(ch);
        englishSymbols = tmp.toString().toCharArray();

        StringBuilder tmpWithSpecial = new StringBuilder();
        tmpWithSpecial.append(englishSymbols);
        tmpWithSpecial.append("!");
        tmpWithSpecial.append("@");
        tmpWithSpecial.append("$");
        tmpWithSpecial.append("%");
        tmpWithSpecial.append("^");
        tmpWithSpecial.append("*");
        tmpWithSpecial.append("(");
        tmpWithSpecial.append(")");
        tmpWithSpecial.append("-");
        tmpWithSpecial.append("+");
        tmpWithSpecial.append("=");
        tmpWithSpecial.append("[");
        tmpWithSpecial.append("]");
        tmpWithSpecial.append("<");
        tmpWithSpecial.append(">");
        tmpWithSpecial.append(",");
        tmpWithSpecial.append(".");
        tmpWithSpecial.append("/");
        tmpWithSpecial.append("?");
        tmpWithSpecial.append(";");

        StringBuilder tmpWithSpecialAll = new StringBuilder(tmpWithSpecial);

        tmpWithSpecialAll.append("{");
        tmpWithSpecialAll.append("}");
        tmpWithSpecialAll.append(":");
        tmpWithSpecialAll.append("\"");
        tmpWithSpecialAll.append("'");
        tmpWithSpecialAll.append("&");
        tmpWithSpecialAll.append("#");

        symbolsWithSpecial = tmpWithSpecial.toString().toCharArray();
        symbolsWithSpecialAll = tmpWithSpecialAll.toString().toCharArray();
    }

    public RandomString(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("length < 1: " + length);
        }
        this.length = length;
        this.strategy = Strategy.ONLY_ENGLISH_CHARS;
    }

    public RandomString(int length, Strategy strategy) {
        if (length < 1) {
            throw new IllegalArgumentException("length < 1: " + length);
        }
        this.length = length;
        this.strategy = strategy;
    }

    public String nextString() {

        // buffer for temp string result
        char[] buf = new char[length];

        for (int idx = 0; idx < buf.length; ++idx) {
            switch (strategy) {
                case ONLY_ENGLISH_CHARS:
                    buf[idx] = englishSymbols[random.nextInt(englishSymbols.length)];
                    break;

                case ENGLISH_CHARS_WITH_SPECIAL_CHARS:
                    buf[idx] = symbolsWithSpecial[random.nextInt(symbolsWithSpecial.length)];
                    break;

                case ENGLISH_CHARS_WITH_SPECIAL_CHARS_ALL:
                    buf[idx] = symbolsWithSpecialAll[random.nextInt(symbolsWithSpecialAll.length)];
                    break;
            }
        }
        return new String(buf);
    }

    /**
     * Created by Nikita Bakaev, ya@nbakaev.ru on 5/26/2016.
     * All Rights Reserved
     */
    public enum Strategy {
        ONLY_ENGLISH_CHARS,
        ENGLISH_CHARS_WITH_SPECIAL_CHARS,
        ENGLISH_CHARS_WITH_SPECIAL_CHARS_ALL
    }
}