package com.intuit.comparisonapi.utils;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

public class MapperUtil {

    private MapperUtil(){}

    public static void mapProperties(Object target, Object source) {
        try {
            BeanUtils.copyProperties(target,source);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
