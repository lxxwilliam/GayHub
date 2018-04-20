package com.calabar.commons.utils;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * * <p>
 * * <li>Description: TODO</li>
 * * <li>@author: TODO</li>
 * * <li>@date 2017-10-12 15:21:16</li>
 * 
 */
public class FreeMarkerUtil {
    
    /**
     * <li>Description: TODO </li>
     *
     * @param name TODO  
     * @param params TODO  
     * @return TODO
     */
    public static String processTemplate(String name, Map<String, Object> params) {
        
        Configuration freeMarkerCfg = new Configuration();
        Template template = null;
        freeMarkerCfg.setObjectWrapper(new DefaultObjectWrapper());
        
        freeMarkerCfg.setClassLoaderForTemplateLoading(Thread.currentThread().getContextClassLoader(), "template");
        try {
            template = freeMarkerCfg.getTemplate(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        OutputStreamWriter writer = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            writer = new OutputStreamWriter(byteArrayOutputStream, "UTF-8");
            template.process(params, writer);
            writer.flush();
            
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        }
        if (writer != null) {
            return new String(byteArrayOutputStream.toByteArray());
        } else
            return null;
    }
    
    /**
     * <li>@Description: TODO</li>
     * *
     * @param s the s
     */
    public static void main(String[] s) {
        
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("title", "test");
        params.put("xData", "[1,2]");
        params.put("yData", "[2,3]");
        
        String centent = processTemplate("drawBar.template", params);
        System.out.println(centent);
        
    }
}
