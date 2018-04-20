package com.calabar.commons.utils;

import java.util.Properties;

public class ConfigUtil {
    public static String get(String propertyKey) {
        Properties properties = (Properties) AppUtil.getBean("configproperties");
        return properties.getProperty(propertyKey);
    }
}