package com.calabar.commons.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.io.IOUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * <p/>
 * <li>Description:TODO</li>
 * <li>@author: Administrator</li>
 * <li>Date: 2017年9月8日 下午3:40:36</li>
 */
public class HDFSUtil {
    /**
     * 任务上传目录
     */
    public static final String CDCALABAR_MDA_TASKS_TEMP = "/HLSJFXZXT/tasks/";
    
    /**
     * spark官方依赖包目录
     */
    public static final String CDCALABAR_MDA_JARS_TEMP = "/jars/";
    
    /**
     * sparkonyarn日志目录directory
     */
    public static final String CDCALABAR_MDA_DREWCTORY_TEMP = "/directory/";
    
    /**
     * 运行实例目录
     */
    public static final String CDCALABAR_MDA_RUNTASKS_TEMP = "/HLSJFXZXT/runtasks/";
    
    /**
     * 
     */
    public static final String HISTORY_FOR_SPARK = "/historyforSpark/";
    
    /**
     * 
     */
    private static HDFSUtil hdfs;
    
    /**
     * 
     */
    private static Map<String, HDFSUtil> hdfsMap = new HashMap<>();
    
    /**
     * 
     */
    private FileSystem fileSystem;
    
    /**
     * 
     * <li> HDFSUtil的构造函数. </li>
     * 
     * @param fs HDFS路径
     */
    private HDFSUtil(String fs) {
        Configuration conf = new Configuration();
        //String fs = AppConfigUtil.get("task.nameNode");
        //String fss = "hdfs://192.168.1.95:9000";
        conf.set("fs.defaultFS", fs);
        try {
            fileSystem = FileSystem.get(conf);
            mkdirs(CDCALABAR_MDA_TASKS_TEMP);
            mkdirs(CDCALABAR_MDA_RUNTASKS_TEMP);
            mkdirs(CDCALABAR_MDA_JARS_TEMP);
            mkdirs(CDCALABAR_MDA_DREWCTORY_TEMP);
        } catch (IOException e) {
        }
    }
    
    /**
     * 
     * <li>Description: 判断后获得HDFSUtil对象</li>
     *
     * @param fs HDFS路径
     * @return HDFSUtil对象
     */
    public static HDFSUtil instance(String fs) {
        if (hdfsMap.containsKey(fs)) {
            hdfs = hdfsMap.get(fs);
        } else {
            hdfs = new HDFSUtil(fs);
            hdfsMap.put(fs, hdfs);
        }
        return hdfs;
    }
    /*public static HDFSUtil instance(String fs) {
        if (hdfs == null) {
            hdfs = new HDFSUtil(fs);
        }
        return hdfs;
    }*/
    
    /**
     *
     * <li>Description: 点击数据管理时按照当前用户的HDFS路径获得新HDFSUtil对象</li>
     *
     * @param fs HDFS路径
     * @return HDFSUtil对象
     */
    public static HDFSUtil getInstance(String fs) {
        hdfs = new HDFSUtil(fs);
        return hdfs;
    }
    
    /**
     * 查看当前路径是否存在
     * @param path 目录
     * @return 是否存在
     */
    public boolean checkPathExist(String path) {
        boolean isExist = false;
        try {
            isExist = fileSystem.exists(new Path(path));
        } catch (IOException e) {
            isExist = true;
        }
        return isExist;
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param path 创建目录
     * @return 是否成功
     */
    public boolean mkdirs(String path) {
        boolean flg = false;
        try {
            Path p = new Path(path);
            if (!fileSystem.exists(p)) {
                flg = fileSystem.mkdirs(p);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return flg;
    }
    
    /**
     * 
     * <li>Description: 上传本地文件</li>
     *
     * @param src 本地路径，可以上传多个文件数组方式
     * @param dst 目标路径
     * @return 是否成功
     */
    public String copyFromLocalFile(String src, String dst) {
        String s = copyFromLocalFile(false, false, src, dst);
        return s;
    }
    
    /**
     * 
     * <li>Description: 上传本地文件</li>
     *
     * @param overwrite 是否覆盖已存在的文件
     * @param src 原路径
     * @param dst 目标路径
     */
    public void copyFromLocalFile(boolean overwrite, String src, String dst) {
        copyFromLocalFile(false, overwrite, src, dst);
    }
    
    /**
     * 
     * <li>Description: 上传本地文件</li>
     *
     * @param delSrc 是否删除源文件
     * @param overwrite 是否覆盖已存在的文件
     * @param src 原路径,可以上传多个文件数组方式
     * @param dst 目标路径
     * @return 是否成功
     */
    public String copyFromLocalFile(boolean delSrc, boolean overwrite, String src, String dst) {
        try {
            mkdirs(dst);
            fileSystem.copyFromLocalFile(delSrc, overwrite, new Path(src), new Path(dst));
        } catch (Exception e) {
            String message = e.getMessage();
            if (message.contains("already exists")) {
                return "重复";
            }
        }
        return "成功";
    }
    
    /**
     * 
     * <li>Description: 创建文件,在hdfs上创建一个文件并写入内容（utf-8）</li>
     *
     * @param overwrite 是否覆盖已存在的文件
     * @param srcName 原路径
     * @param src 要写入的内容
     * @param dst 目标路径
     */
    public void createFile(boolean overwrite, String srcName, String src, String dst) {
        try {
            mkdirs(dst);
            FSDataOutputStream out = fileSystem.create(new Path(dst + srcName), overwrite);
            byte[] buff = src.getBytes("UTF-8");
            out.write(buff, 0, buff.length);
            out.flush();
            out.close();
        } catch (Exception e) {
        }
    }
    
    /**
     * 
     * <li>Description: 创建空文件 </li>
     *
     * @param path 目标路径
     * @return 是否创建成功
     */
    public boolean createNewFile(String path) {
        boolean flg = false;
        try {
            Path p = new Path(path);
            if (!fileSystem.exists(p)) {
                flg = fileSystem.createNewFile(p);
            }
        } catch (Exception e) {
        }
        return flg;
    }
    
    /**
     * 
     * <li>Description: 删除文件</li>
     *
     * @param name 路径或文件
     * @param recursion 是否递归
     * @return 是否删除成功
     */
    public boolean deleteFile(String name, boolean recursion) {
        boolean flg = false;
        try {
            flg = fileSystem.delete(new Path(name), recursion);
        } catch (Exception e) {
        }
        return flg;
    }
    
    /**
     * 
     * <li>Description: 文件重命名</li>
     *
     * @param oldName 原文件目录
     * @param newName 修改后的目录
     * @return 是否成功
     */
    public boolean rename(String oldName, String newName) {
        boolean flg = false;
        try {
            flg = fileSystem.rename(new Path(oldName), new Path(newName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flg;
    }
    
    /**
     * 
     * <li>Description: 下载文件夹到本地</li>
     *
     * @param srcPath hdfs文件路径
     * @param dstPath 本地文件目录
     */
    public void copyToLocalFile(String srcPath, String dstPath) {
        try {
            if (fileSystem.isFile(new Path(srcPath))) {
                downloadFile(srcPath, dstPath);
            } else {
                downloadFolder(srcPath, dstPath);
            }
        } catch (Exception e) {
        }
    }
    
    /**
     * 
     * <li>Description: 下载文件</li>
     *
     * @param srcPath hdfs文件路径
     * @param dstPath 本地文件目录
     * @throws Exception 异常
     */
    private void downloadFile(String srcPath, String dstPath) throws Exception {
        FSDataInputStream in = null;
        FileOutputStream out = null;
        try {
            in = fileSystem.open(new Path(srcPath));
            out = new FileOutputStream(dstPath);
            IOUtils.copyBytes(in, out, 4096, false);
        } catch (Exception e) {
        } finally {
            IOUtils.closeStream(in);
            IOUtils.closeStream(out);
        }
    }
    
    /**
     * 
     * <li>Description: 下载文件夹</li>
     *
     * @param srcPath hdfs文件路径
     * @param dstPath 本地文件目录
     * @throws Exception 异常
     */
    private void downloadFolder(String srcPath, String dstPath) throws Exception {
        File dstDir = new File(dstPath);
        if (!dstDir.exists()) {
            dstDir.mkdirs();
        }
        FileStatus[] srcFileStatus = fileSystem.listStatus(new Path(srcPath));
        Path[] srcFilePath = FileUtil.stat2Paths(srcFileStatus);
        for (int i = 0; i < srcFilePath.length; i++) {
            String srcFile = srcFilePath[i].toString();
            int fileNamePosi = srcFile.lastIndexOf('/');
            String fileName = srcFile.substring(fileNamePosi + 1);
            copyToLocalFile(srcPath + '/' + fileName, dstPath + '/' + fileName);
        }
    }
    
    /**
     * 下载单个文件到本地
     * @param srcPath hdfs文件路径
     * @param out 输出流
     */
    public void downloadSingleFile(String srcPath, OutputStream out) {
        FSDataInputStream in = null;
        try {
            in = fileSystem.open(new Path(srcPath));
            IOUtils.copyBytes(in, out, 4096, false);
        } catch (Exception e) {
        } finally {
            IOUtils.closeStream(in);
            IOUtils.closeStream(out);
        }
    }
    
    /**
     * 下载本地文件
     * @param srcPath 本地文件路径
     * @param out 输出流
     */
    public void downloadLocalFile(String srcPath, OutputStream out) {
        File file = new File(srcPath);
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            int l;
            byte[] b = new byte[1024];
            while ((l = in.read(b)) != -1) {
                out.write(b, 0, l);
            }
            in.close();
            out.close();
            out.flush();
        } catch (Exception e) {
        } finally {
            IOUtils.closeStream(in);
            IOUtils.closeStream(out);
        }
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param srcPath 文件目录
     * @return 是否是文件
     */
    public boolean isFile(String srcPath) {
        boolean isFile = false;
        try {
            isFile = fileSystem.isFile(new Path(srcPath));
        } catch (Exception e) {
            isFile = true;
        }
        return isFile;
    }
    
    /**
     * 
     * <li>Description: 读取spark history读取spark history</li>
     *
     * @param name 目录
     * @return 文件内容
     */
    public String readSparkHistoryFromHdfs(String name) {
        FSDataInputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            in = fileSystem.open(new Path(HISTORY_FOR_SPARK + name));
            int i;
            while ((i = in.read()) != -1) {
                out.write(i);
            }
            String str = out.toString();
            return str;
        } catch (Exception e) {
        } finally {
            IOUtils.closeStream(in);
            IOUtils.closeStream(out);
        }
        return null;
    }
    
    /**
     * 读取文件内容
     * @param path 文件路径
     * @return 文件内容
     */
    public String readFromHdfs(String path) {
        FSDataInputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            in = fileSystem.open(new Path(path));
            int i;
            while ((i = in.read()) != -1) {
                out.write(i);
            }
            String str = out.toString();
            return str;
        } catch (Exception e) {
        } finally {
            IOUtils.closeStream(in);
            IOUtils.closeStream(out);
        }
        return null;
    }
    
    /**
     * 读取TXT文件内容
     * @param path 文件路径
     * @param lineNum 读取行数
     * @return 文件内容
     */
    public String readTXTFromHdfs(String path, int lineNum) {
        return readTXTFromHdfsGBK(path, lineNum, "UTF-8");
    }
    
    /**
     * 读取TXT文件内容
     * @param path 文件路径
     * @param lineNum 读取行数
     *  @param code 编码方式
     *  @return 返回
     */
    public String readTXTFromHdfsGBK(String path, int lineNum, String code) {
        StringBuilder buffer = new StringBuilder();
        
        FSDataInputStream in = null;
        BufferedReader reader = null;
        String lineTxt = null;
        int count = 0;
        try {
            in = fileSystem.open(new Path(path));
            reader = new BufferedReader(new InputStreamReader(in, code));
            while ((lineTxt = reader.readLine()) != null) {
                if (lineNum == -1) {
                    buffer.append(lineTxt + "\n");
                } else {
                    if (count < lineNum) {
                        buffer.append(lineTxt + "\n");
                        count++;
                    }
                }
            }
            return buffer.toString();
        } catch (Exception e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            IOUtils.closeStream(in);
        }
        return null;
    }
    
    /**
     * 遍历HDFS上的文件和目录
     * @param filePath 目录
     * @return 文件对象
     */
    public FileStatus[] getDirectoryFromHdfs(String filePath) {
        FileStatus[] status = null;
        try {
            if (filePath == null) {
                filePath = "/";
            }
            status = fileSystem.listStatus(new Path(filePath));
        } catch (Exception e) {
        }
        return status;
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @return 文件对象
     */
    public FileStatus[] getDirectoryFromHdfs() {
        return getDirectoryFromHdfs("/");
    }
    
    /**
     * 读取二进制文件内容
     * @param path 文件路径
     * @param encode 编码方式
     * @param lineNum 读取行数
     * @param headcharNum 头字符数
     * @param recordLength 记录长度
     * @param field 字段列表
     * @return 文件内容
     */
    public List<List<String>> readBinaryFromHdfs(String path, String encode, int lineNum, int headcharNum,
            int recordLength, String field) {
        List<List<String>> list = new ArrayList<>();
        JSONArray fieldArr = JSONObject.parseArray(field);
        List<String> row = null;
        FSDataInputStream in = null;
        try {
            if (!(fileSystem.isFile(new Path(path)))) {
                FileStatus[] srcFileStatus = fileSystem.listStatus(new Path(path));
                Path[] srcFilePath = FileUtil.stat2Paths(srcFileStatus);
                if (srcFilePath != null && srcFilePath.length > 0) {
                    path = srcFilePath[0].toString();
                } else {
                    return null;
                }
            }
            in = fileSystem.open(new Path(path));
            DataInputStream dataInputStream = new DataInputStream(in);
            dataInputStream.skip(headcharNum); //跳过字符数
            while (lineNum > 0 && dataInputStream.available() > 0) {
                row = new ArrayList<String>();
                for (int i = 0; i < fieldArr.size(); i++) {
                    JSONObject object = fieldArr.getJSONObject(i);
                    String type = object.getString("type");
                    if ("byte".equals(type)) {
                        Byte str = dataInputStream.readByte();
                        row.add(str.toString());
                    } else if ("short".equals(type)) {
                        Short str = dataInputStream.readShort();
                        row.add(str.toString());
                    } else if ("int".equals(type)) {
                        Integer str = dataInputStream.readInt();
                        row.add(str.toString());
                    } else if ("long".equals(type)) {
                        Long str = dataInputStream.readLong();
                        row.add(str.toString());
                    } else if ("float".equals(type)) {
                        Float str = dataInputStream.readFloat();
                        row.add(str.toString());
                    } else if ("double".equals(type)) {
                        Double str = dataInputStream.readDouble();
                        row.add(str.toString());
                    } else if ("char".equals(type)) {
                        Character str = dataInputStream.readChar();
                        row.add(str.toString());
                    } else if ("string".equals(type)) {
                        int len = object.getIntValue("len");
                        byte[] strArr = new byte[len];
                        int readLen = dataInputStream.read(strArr);
                        row.add(new String(strArr, 0, readLen, encode));
                    }
                }
                list.add(row);
                //遍历递减
                lineNum--;
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(in);
        }
        return null;
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param host hdfs路径
     * @return hdfs信息
     */
    public String getNameNodeInfo(String host) {
        //http://namenode:50070/jmx?qry=Hadoop:service=NameNode,name=NameNodeInfo 
        String url = "http://" + host + ":50070/jmx";
        String params = "?qry=Hadoop:service=NameNode,name=NameNodeInfo";
        String result = HttpUtil.instance().get(url + params);
        return result;
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param host hdfs路径
     * @return hdfs信息
     */
    public String getNameNodeStatus(String host) {
        //http://namenode:50070/jmx?qry=Hadoop:service=NameNode,name=NameNodeInfo 
        String url = "http://" + host + ":50070/jmx";
        String params = "?qry=Hadoop:service=NameNode,name=NameNodeStatus";
        String result = HttpUtil.instance().get(url + params);
        return result;
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param args 参数
     * @throws Exception 异常
     */
    public static void main(String[] args) throws Exception {
        //HDFSUtil.instance().copyFromLocalFile("C:\\Users\\Administrator\\Desktop\\999.txt",
        //"hdfs://192.168.1.201:9000/333112");
        
        //HDFSUtil.instance().deleteFile("/test", true);
        
        //HDFSUtil.instance().copyToLocalFile("application_1479175523058_0002_1","D:/CHM_TEST");
        
        //String str = HDFSUtil.instance().readSparkHistoryFromHdfs("application_1479175523058_0002_1");
        //System.out.println(str);
        /*FileStatus[] status = HDFSUtil.instance().getDirectoryFromHdfs("/");
        for (FileStatus file : status) {
        	System.out.println("name:" + file.getPath().getName()+ ",size:" + file.getLen());
        }*/
        
        //        HDFSUtil.instance().createNewFile("/test");
        
    }
    
}
