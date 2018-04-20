package com.calabar.commons.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * <p/>
 * <li>Description:TODO</li>
 * <li>@author: Administrator</li>
 * <li>Date: 2017年9月8日 下午4:36:32</li>
 */
public class TaskUtil {
    
    /**
     * 
     */
    private static Log logger = LogFactory.getLog(TaskUtil.class);
    
    /**
     * 
     * key: 物理机IP
     * list: 物理机上的执行任务列表
     */
    public static Map<String, List<JSONObject>> allTask = new HashMap<String, List<JSONObject>>();
    
    /**
     * @return 任务列表
     */
    public static Map<String, List<JSONObject>> getAllTask() {
        return allTask;
    }
    
    public static void setAllTask(Map<String, List<JSONObject>> allTask) {
        TaskUtil.allTask = allTask;
    }
    
    /**
     * 
     * <li>Description: 将队列中的物理机任务移除</li>
     *
     * @param nodeIp 物理机IP
     */
    public static void removeTask(String nodeIp) {
        Iterator<String> iterator = allTask.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            if (nodeIp.equals(key)) {
                iterator.remove();
                allTask.remove(key);
                logger.debug("移除物理机ip=" + nodeIp + "-----成功从allTask队列中移除");
            }
        }
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        String taskid = "1234567";
        //生成文件路径
        String path = "D:\\apache-tomcat-8.0.33\\webapps\\mda\\attachFiles\\task_temp\\";
        String filenameTemp = path + taskid + ".sh"; //文件路径+名称+文件类型
        File file = new File(filenameTemp);
        try {
            //如果文件不存在，则创建新的文件
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 向临时文件中输入内容
        List<String> content = new ArrayList<>();
        content.add("#!/bin/sh");
        content.add("cur_dir=$(dirname $0)");
        content.add("cd $cur_dir");
        content.add("sh docker-install.sh");
        try {
            FileUtil.writeFileContent(filenameTemp, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 删除文件
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }
    
}