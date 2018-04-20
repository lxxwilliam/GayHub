/**
 * Project Name:mda
 * File Name:DataUtil.java
 * Package Name:com.calabar.commons.utils
 * Date:2017年12月27日上午9:26:19
 */
package com.calabar.commons.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * ClassName:DataUtil <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * @author Lanxg
 * Date:2017年12月27日上午9:26:19	 
 */
public class DataUtil {
    public static String setExcuteVal(String para,String type)
    {
       
        String paras[] = para.split(" ");
        if(paras.length>0)
        {
        for(String str:paras)
        {
            if("1".equals(type)){
                int index1 = str.indexOf("--executor-memory=");
                if(index1>-1){
                return str.replaceAll("--executor-memory=",""); 
                }
            }
            if("2".equals(type)){
                int index2 = str.indexOf("--executor-cores=");
                if(index2>-1){
                return str.replaceAll("--executor-cores=","");  
                }
            }
        }
        }
        if("1".equals(type)){
            return "512m"; 
        }else{
            return "1";  
        }
    }
    /**
     * 获取物理机IP
     * <li>Description: TODO</li>
     * @param reg
     * @param ips
     * @return
     */
    public static String phyIp(String reg, String ips) {
        String reslutip = "";
        if (!StringUtils.isBlank(reg) && !StringUtils.isBlank(ips)) {
            String ip[] = ips.split(",");
            String re[] = reg.split(",");
            lebalA: for (String r : re) {
                lebalB: for (String str : ip) {
                    String cip[] = str.split("\\.");
                    if (cip[2].equals(r)) {
                        reslutip = str;
                        break lebalA;
                    }
                }
            }
        }
        return reslutip;
    }
   
}
