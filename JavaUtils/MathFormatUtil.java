package com.calabar.commons.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 
 * <p/>
 * <li>Description:TODO</li>
 * <li>@author: Administrator</li>
 * <li>Date: 2017年9月8日 下午4:07:06</li>
 */
public class MathFormatUtil {
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param f 数据
     * @return 格式化数据
     */
    public static double format(double f) {
        return format(f, 1);
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param f 数据
     * @param num 保留小数位数
     * @return 格式化数据
     */
    public static double format(double f, int num) {
        BigDecimal bg = new BigDecimal(f);
        double f1 = bg.setScale(num, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        double f = 111231.5585;
        System.out.println(format(f));
        
        System.out.println(format(88.00 / 500, 2));
        
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数    
        System.out.println(df.format((double) 88.00 / 500));
        
    }
}
