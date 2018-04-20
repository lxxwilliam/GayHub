package com.calabar.commons.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;

/**
 * 
 * <p/>
 * <li>Description:TODO</li>
 * <li>@author: Administrator</li>
 * <li>Date: 2017年9月8日 下午4:25:21</li>
 */
public class ShellUtil {
    
    /**
     * 
     */
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * 
     */
    public static final int DEFAULT_SSH_PORT = 22;
    
    /**
     * 
     */
    private String ipAddress;
    
    /**
     * 
     */
    private String username;
    
    /**
     * 
     */
    private String password;
    
    /**
     * 
     */
    private int sshport;
    
    /**
     * 
     */
    private List<String> stdout;
    
    /**
     * 
     * <li> ShellUtil的构造函数. </li>
     * 
     * @param ipAddress ip地址
     * @param username 用户名
     * @param password 密码
     * @param sshport 端口
     */
    public ShellUtil(String ipAddress, String username, String password, int sshport) {
        this.ipAddress = ipAddress;
        this.username = username;
        this.password = password;
        this.sshport = sshport;
        stdout = new ArrayList<String>();
    }
    
    /**
     * 
     * <li> ShellUtil的构造函数. </li>
     * 
     * @param ipAddress ip地址
     * @param username 用户名
     * @param password 密码
     */
    public ShellUtil(String ipAddress, String username, String password) {
        this.ipAddress = ipAddress;
        this.username = username;
        this.password = password;
        this.sshport = DEFAULT_SSH_PORT;
        stdout = new ArrayList<String>();
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param command 执行指令
     * @return 状态值
     */
    public int execute(final String command) {
        int returnCode = 0;
        JSch jsch = new JSch();
        MyUserInfo userInfo = new MyUserInfo();
        try {
            // Create and connect session.
            Session session = jsch.getSession(username, ipAddress, sshport);
            session.setPassword(password);
            session.setUserInfo(userInfo);
            session.connect();
            
            // Create and connect channel.
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            
            channel.setInputStream(null);
            BufferedReader input = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            
            channel.connect();
//            System.out.println("The remote command is: " + command);
            
            // Get the output of remote command.
            String line;
            while ((line = input.readLine()) != null) {
                stdout.add(line);
            }
            input.close();
            
            // Get the return code only after the channel is closed.
            if (channel.isClosed()) {
                returnCode = channel.getExitStatus();
            }
            
            // Disconnect the channel and session.
            channel.disconnect();
            session.disconnect();
        } catch (JSchException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException();
        }
        return returnCode;
    }
    
    /**
     * 上传文件/目录到目标机器
     * 
     * @param src
     *            本地文件名
     * @param dst
     *            目标目录（只能是目录）
     * @return 状态值
     */
    public int upFile(final String src, final String dst) {
        int returnCode = 0;
        JSch jsch = new JSch();
        MyUserInfo userInfo = new MyUserInfo();
        try {
            // Create and connect session.
            Session session = jsch.getSession(username, ipAddress, sshport);
            session.setPassword(password);
            session.setUserInfo(userInfo);
            session.connect();
            
            // Create and connect channel.
            Channel channel = session.openChannel("sftp");
            ChannelSftp sftp = (ChannelSftp) channel;
            
            sftp.connect();
            copyFile(sftp, src, dst);
            // sftp.put(src, dst, ChannelSftp.OVERWRITE);
            /*
             * sftp.setInputStream(null); BufferedReader input = new
             * BufferedReader(new InputStreamReader(sftp.getInputStream()));
             * 
             * // Get the output of remote command. String line; while ((line =
             * input.readLine()) != null) { stdout.add(line); } input.close();
             */
            sftp.quit();
            // Get the return code only after the channel is closed.
            if (sftp.isClosed()) {
                returnCode = sftp.getExitStatus();
            }
            // Disconnect the channel and session.
            session.disconnect();
        } catch (JSchException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return returnCode;
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param sftp ftp对象
     * @param src 原路径
     * @param dst 目标路径
     */
    private void copyFile(ChannelSftp sftp, String src, String dst) {
        try {
            sftp.cd(dst);
            sftp.chmod(Integer.parseInt("777", 8), dst);
        } catch (SftpException e) {
            try {
                sftp.mkdir(dst);
//                logger.debug("创建目录成功.." + dst);
            } catch (SftpException e1) {
                logger.error(e.getMessage(), e);
            }
        }
        File srcfile = new File(src);
        if (srcfile.isDirectory()) {
            String dstname = dst + "/" + srcfile.getName();
            try {
                sftp.cd(dstname);
                sftp.chmod(Integer.parseInt("777", 8), dst);
            } catch (SftpException e) {
                try {
                    sftp.mkdir(dstname);
                    logger.debug("创建目录成功.." + dstname);
                } catch (SftpException e1) {
                    logger.error(e.getMessage(), e);
                }
            }
            String[] fStrings = srcfile.list();
            for (String f : fStrings) {
                copyFile(sftp, src + "/" + f, dstname);
            }
        } else {
            try {
                //sftp.cd(dst);
                //sftp.chmod(Integer.parseInt("777", 8), dst);
                sftp.put(src, dst, ChannelSftp.OVERWRITE);
                sftp.chmod(Integer.parseInt("777", 8), dst + "/" + srcfile.getName());
            } catch (SftpException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @return 输出
     */
    public List<String> getStandardOutput() {
        return stdout;
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        ShellUtil su = new ShellUtil("192.168.1.204", "root", "calabar");
        // int st =
        //su.execute("gzip -c /home/1.txt | spawn ssh root@192.168.1.99 \"gunzip -c - > 
        //      /home/1.txt\" \r expect {\"*password:\" {send \"123456\\r\"}}");
        // su.execute("jps");
        //		int st = su.upFile("E:\\tools\\java\\jdk\\jdk1.8.0_144", "/home/jdk");
        //		System.out.println(st); 
        //		List<String> out = su.getStandardOutput();
        //		for (String t : out) {
        //			System.out.println(t);
        //		}
        //su.upFile("D:\\apache-tomcat-8.0.33\\webapps\\mda\\attachFiles/agent_client/agent_client.jar",
        //        "/home/agent_client");
        su.execute("sh /home/agent_client/start-agent.sh restart");
        //su.execute("sh /home/agent_client/start-agent.sh stop");
        List<String> out = su.getStandardOutput();
        for (String t : out) {
            System.out.println(t);
        }
    }
}

/**
 * 
 * <p/>
 * <li>Description:TODO</li>
 * <li>@author: Administrator</li>
 * <li>Date: 2017年9月8日 下午4:26:06</li>
 */
class MyUserInfo implements UserInfo {
    
    @Override
    public String getPassphrase() {
        System.out.println("MyUserInfo.getPassphrase()");
        return null;
    }
    
    @Override
    public String getPassword() {
        System.out.println("MyUserInfo.getPassword()");
        return null;
    }
    
    @Override
    public boolean promptPassphrase(String arg0) {
        System.out.println("MyUserInfo.promptPassphrase()");
        System.out.println(arg0);
        return false;
    }
    
    @Override
    public boolean promptPassword(String arg0) {
        System.out.println("MyUserInfo.promptPassword()");
        System.out.println(arg0);
        return false;
    }
    
    @Override
    public boolean promptYesNo(String arg0) {
        System.out.println("MyUserInfo.promptYesNo()");
        System.out.println(arg0);
        if (arg0.contains("The authenticity of host")) {
            return true;
        }
        return false;
    }
    
    @Override
    public void showMessage(String arg0) {
        System.out.println("MyUserInfo.showMessage()");
    }
    
}
