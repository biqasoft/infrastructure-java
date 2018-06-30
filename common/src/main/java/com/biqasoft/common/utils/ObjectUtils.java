package com.biqasoft.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ObjectUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T clone(Object e, Class<T> t) {
        try {
            String s = objectMapper.writeValueAsString(e);
            return objectMapper.readValue(s, t);
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }
    }

    public static <T> T clone(Object e, TypeReference t) {
        try {
            String s = objectMapper.writeValueAsString(e);
            return objectMapper.readValue(s, t);
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }
    }

}
