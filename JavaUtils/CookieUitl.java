package com.calabar.commons.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

/**
 * 
 * <p/>
 * <li>Description:TODO</li>
 * <li>@author: Administrator</li>
 * <li>Date: 2017年9月8日 下午3:28:04</li>
 */
public class CookieUitl {
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param name 名称
     * @param value 值
     * @param maxAge 最大时间
     * @param context 上下文
     */
    public static void addCookie(String name, String value, int maxAge, PageContext context) {
        HttpServletResponse response = (HttpServletResponse) context.getResponse();
        HttpServletRequest req = (HttpServletRequest) context.getRequest();
        addCookie(name, value, maxAge, req, response);
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param name 名称
     * @param value 值
     * @param context 上下文
     */
    public static void addCookie(String name, String value, PageContext context) {
        HttpServletResponse response = (HttpServletResponse) context.getResponse();
        HttpServletRequest req = (HttpServletRequest) context.getRequest();
        addCookie(name, value, -1, req, response);
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param name 名称
     * @param value 值
     * @param req 请求对象
     * @param response 返回对象
     */
    public static void addCookie(String name, String value, HttpServletRequest req, HttpServletResponse response) {
        addCookie(name, value, -1, req, response);
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param name 名称 
     * @param value 值
     * @param maxAge 以秒为单位,0为立即失效，负数库关闭 浏览器失效
     * @param req 请求对象
     * @param response 返回对象
     */
    public static void addCookie(String name, String value, int maxAge, HttpServletRequest req,
            HttpServletResponse response) {
        if (response == null)
            return;
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(req.getContextPath());
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param name 名称
     * @param context 上下文
     */
    public static void delCookie(String name, PageContext context) {
        HttpServletResponse response = (HttpServletResponse) context.getResponse();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        delCookie(name, request, response);
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param name 名称
     * @param request 请求对象
     * @param response 返回对象
     */
    public static void delCookie(String name, HttpServletRequest request, HttpServletResponse response) {
        addCookie(name, "", 0, request, response);
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param name 名称
     * @param context 上下文
     * @return 值
     */
    public static String getValueByName(String name, PageContext context) {
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        String str = getValueByName(name, request);
        return str;
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param name 名称
     * @param request 请求对象
     * @return 值
     */
    public static String getValueByName(String name, HttpServletRequest request) {
        if (request == null)
            return "";
        Cookie[] cookies = request.getCookies();
        Cookie sCookie = null;
        String svalue = null;
        String sname = null;
        
        if (cookies == null)
            return null;
        for (int i = 0; i < cookies.length; i++) {
            sCookie = cookies[i];
            sname = sCookie.getName();
            if (!sname.equals(name))
                continue;
            svalue = sCookie.getValue();
            break;
        }
        
        return svalue;
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param name 名称
     * @param context 上下文
     * @return 是否存在的布尔值
     */
    public static boolean isExistByName(String name, PageContext context) {
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        return isExistByName(name, request);
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param name 名称
     * @param request 请求对象
     * @return 是否存在的布尔值
     */
    public static boolean isExistByName(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Cookie sCookie = null;
        
        String sname = null;
        boolean isExist = false;
        if (cookies == null)
            return false;
        for (int i = 0; i < cookies.length; i++) {
            sCookie = cookies[i];
            sname = sCookie.getName();
            if (!sname.equals(name))
                continue;
            isExist = true;
            break;
        }
        
        return isExist;
    }
}