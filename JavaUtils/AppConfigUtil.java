package com.calabar.commons.utils;

import java.util.Properties;

/**
 * 
 * <p/>
 * <li>Description:TODO</li>
 * <li>@author: Administrator</li>
 * <li>Date: 2017年9月8日 下午3:00:11</li>
 */
public class AppConfigUtil {
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param propertyKey 配置参数关键字
     * @return 配置参数
     */
    public static String get(String propertyKey) {
        Properties properties = (Properties) AppUtil.getBean("configproperties");
        return properties.getProperty(propertyKey);
    }
}