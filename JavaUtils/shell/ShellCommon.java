/**
 * Project Name:calabar-command
 * File Name:ShellCommon.java
 * Package Name:com.calabar.commons.utils
 * Date:2017年11月18日下午3:36:56
 */
package com.calabar.commons.utils.shell;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.calabar.commons.utils.DockerUtil;

/**
 * ClassName:ShellCommon <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * @author   Lanxg 	 
 */
public class ShellCommon {
    private static Log logger = LogFactory.getLog(ShellCommon.class);
    /**
     * 
     * <li>Description: 执行SH文件</li>
     * @param sh  Process p=Runtime.getRuntime().exec(“/etc/a.sh”)
     * @return
     */
    public static String executeSh(String sh)
    {
        try {
            logger.info("shell:"+sh);
            new ExcuteThread(sh).start();
        } catch (Exception e) {
            e.printStackTrace();  
            return e.getMessage();
        }
        return "SUCCESS";
    }
}

