/** 
 * Project Name:mda 
 * File Name:DockerUtil.java 
 * Package Name:com.calabar.commons.utils 
 * Date:2017年9月25日下午2:17:56 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
*/

package com.calabar.commons.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.calabar.mda.entity.cluster.PlatClusterNodeEntity;
import com.calabar.mda.enums.PlatClusterNodeType;
import com.calabar.mda.service.cluster.PlatClusterNodeService;
import com.calabar.mda.service.http.HttpServiceApi;

/**
 * ClassName:DockerUtil <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2017年9月25日 下午2:17:56 <br/>
 * @author   Lanxg
 * @version  
 * @see 	 
 * 应用集群操作工具类生产命令
 */
public class DockerUtil {
    private static Log logger = LogFactory.getLog(DockerUtil.class);
    
    /**
     * <li>Description: spark 作业提交方法</li>
     * @param rmip  spark master
     * @param hdfs  hdfs://192.168.1.217:9000
     * @param clas  com.apache.spark.WordCount
     * @param mem cluster
     * @param executmem 512m
     * @param core 1
     * @param hdfspath hdfs://192.168.1.217:9000/data/spark.jar
     * @return 返回
     */
    public boolean submitSparkTask(HttpServiceApi httpApi,PlatClusterNodeService platClusterNodeService, String rmip, String hdfs, String clas,
            String mem, String executmem, String core, String hdfspath) {
        try {
            List<PlatClusterNodeEntity> platNodes = platClusterNodeService.findByType(PlatClusterNodeType.leader);
            String leaderIp = "";
            if (platNodes != null && platNodes.size() > 0) {
                PlatClusterNodeEntity platNode = platNodes.get(0);
                leaderIp = platNode.getNode().getIpArr(); //docker主节点IP    
            } else {
                return false;
            }
            String nodePort = AppConfigUtil.get("agent_server_port");
            JSONObject json = new JSONObject();
            json.put("rmip", rmip);
            json.put("hdfs", hdfs);
            json.put("clas", clas);
            json.put("mem", mem);
            json.put("executmem", executmem);
            json.put("core", core);
            json.put("hdfspath", hdfspath);
            String url = "http://" + leaderIp + ":" + nodePort + "/node/startSpark";
            Map<String, String> params = new HashMap<>();
            params.put("data", json.toJSONString());
            String result = httpApi.doPost(url, params);
//            String result = HttpUtil.instance().post(url, params);
            if (StringUtils.isBlank(result))
                return false;
            JSONObject resultObject = JSONObject.parseObject(result);
            if (1 != resultObject.getIntValue("flag")) {
                return false;
            }
        } catch (Exception e) {
            logger.info("spark 提交任务异常：" + e.getMessage());
            return false;
        }
        return true;
    }
    
    /**
     * 
     * <li>Description: storm 作业提交方法</li>
     * @param inputpath /home/input
     * @param filename storm-starter-1.1.1.jar
     * @param classpath org.apache.storm.starter.WordCountTopology
     * @param zkString \"aa\",\"bb5\"
     * @param nimString \"aa\",\"bb5\"
     * @return 返回
     */
    public boolean submitStormTask(HttpServiceApi httpApi,PlatClusterNodeService platClusterNodeService, String inputpath, String filename,
            String classpath, String zkString, String nimString, String hdfs, String hdfspath) {
        try {
            List<PlatClusterNodeEntity> platNodes = platClusterNodeService.findByType(PlatClusterNodeType.leader);
            String leaderIp = "";
            if (platNodes != null && platNodes.size() > 0) {
                PlatClusterNodeEntity platNode = platNodes.get(0);
                leaderIp = platNode.getNode().getIpArr(); //docker主节点IP    
            } else {
                return false;
            }
            String nodePort = AppConfigUtil.get("agent_server_port");
            JSONObject json = new JSONObject();
            json.put("inputpath", inputpath);
            json.put("filename", filename);
            json.put("classpath", classpath);
            json.put("zkString", zkString);
            json.put("nimString", nimString);
            json.put("hdfspath", hdfs);
            json.put("hdfsfilepath", hdfspath);
            //            leaderIp="192.168.1.231";
            String url = "http://" + leaderIp + ":" + nodePort + "/node/startStorm";
            Map<String, String> params = new HashMap<>();
            params.put("data", json.toJSONString());
            String result = httpApi.doPost(url, params);
//            String result = HttpUtil.instance().post(url, params);
            if (StringUtils.isBlank(result))
                return false;
            JSONObject resultObject = JSONObject.parseObject(result);
            if (1 != resultObject.getIntValue("flag")) {
                return false;
            }
        } catch (Exception e) {
            logger.info("storm 提交任务异常：" + e.getMessage());
            return false;
        }
        return true;
    }
    
    /**
     * 
     * <li>Description: mxnet 作业提交方法</li>
     * @param inputpath /home/input
     * @param filename storm-starter-1.1.1.jar
     * @param classpath org.apache.storm.starter.WordCountTopology
     * @param zkString \"aa\",\"bb5\"
     * @param nimString \"aa\",\"bb5\"
     * @return 返回
     */
    public boolean submitMxnetTask(HttpServiceApi httpApi,String masterid, String nodeIp, String pkgAddr, String id, String serverName,
            String hdfs, String ips, String filepath) {
        try {
            
            String nodePort = AppConfigUtil.get("agent_server_port");
            JSONObject json = new JSONObject();
            json.put("pkgAddr", pkgAddr);//hdfs://192.168.1.95:9000//HLSJFXZXT//tasks//7a513372-f8b3-4d6a-9bab-3bca9a54098d//
            json.put("id", id);
            json.put("serverName", serverName);
            json.put("hdfs", hdfs);//hdfs://192.168.1.95:9000
            json.put("ips", ips);//192.168.1.95;192.168.1.96;192.168.1.97
            json.put("filepath", filepath.replaceAll("/ProgramPackge/", ""));///ProgramPackge/mnist_test.py
            json.put("masterid", masterid);
            String url = "http://" + nodeIp + ":" + nodePort + "/node/startMxnet";
            Map<String, String> params = new HashMap<>();
            params.put("data", json.toJSONString());
            
            String result = httpApi.doPost(url, params);
//            String result = HttpUtil.instance().post(url, params);
            if (StringUtils.isBlank(result))
                return false;
            JSONObject resultObject = JSONObject.parseObject(result);
            if (1 != resultObject.getIntValue("flag")) {
                return false;
            }
        } catch (Exception e) {
            logger.info("storm 提交任务异常：" + e.getMessage());
            return false;
        }
        return true;
    }
}
