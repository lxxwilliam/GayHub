package com.calabar.commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import org.apache.commons.io.FileUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

/**
 * 
 * <p/>
 * <li>Description:TODO</li>
 * <li>@author: Administrator</li>
 * <li>Date: 2017年9月8日 下午4:45:41</li>
 */
public class ZipUtil {
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param filesDirPath 文件地址
     * @param zipDirPath 压缩包地址
     * @param zipName 压缩包名称
     */
    public static void doZip(String filesDirPath, String zipDirPath, String zipName) {
        ZipOutputStream out = null;
        try {
            
            File inFile = new File(filesDirPath);
            FileUtils.forceMkdir(inFile);
            File outFile = new File(zipDirPath);
            FileUtils.forceMkdir(outFile);
            
            out = new ZipOutputStream(new File(zipDirPath + "/" + zipName));
            if (inFile.isDirectory()) {
                File[] inFiles = inFile.listFiles();
                for (File file : inFiles) {
                    FileInputStream in = null;
                    try {
                        out.putNextEntry(new ZipEntry(file.getName()));
                        in = new FileInputStream(file);
                        int l;
                        byte[] b = new byte[1024];
                        while ((l = in.read(b)) != -1) {
                            out.write(b, 0, l);
                        }
                        out.setEncoding("GBK"); //指定编码
                        out.flush();
                        out.closeEntry();
                    } finally {
                        if (in != null)
                            in.close();
                    }
                }
            } else {
                FileInputStream in = null;
                try {
                    out.putNextEntry(new ZipEntry(inFile.getName()));
                    in = new FileInputStream(inFile);
                    int l;
                    byte[] b = new byte[1024];
                    while ((l = in.read(b)) != -1) {
                        out.write(b, 0, l);
                    }
                    out.setEncoding("GBK"); //指定编码
                    out.flush();
                    out.closeEntry();
                } finally {
                    if (in != null)
                        in.close();
                }
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param srcFile 原文件地址
     * @param dest 目标文件地址
     * @param deleteFile 是否删除原文件
     * @return 是否解压成功
     */
    public static boolean unZip(String srcFile, String dest, boolean deleteFile) {
        try {
            File file = new File(srcFile);
            if (!file.exists()) {
                // throw new RuntimeException("解压文件不存在!");
                return false;
            }
            ZipFile zipFile = new ZipFile(file);
            Enumeration<ZipEntry> e = zipFile.getEntries();
            while (e.hasMoreElements()) {
                ZipEntry zipEntry = e.nextElement();
                if (zipEntry.isDirectory()) {
                    String name = zipEntry.getName();
                    name = name.substring(0, name.length() - 1);
                    File f = new File(dest + name);
                    f.mkdirs();
                } else {
                    File f = new File(dest + zipEntry.getName());
                    f.getParentFile().mkdirs();
                    f.createNewFile();
                    InputStream is = zipFile.getInputStream(zipEntry);
                    FileOutputStream fos = new FileOutputStream(f);
                    int length = 0;
                    byte[] b = new byte[1024];
                    while ((length = is.read(b, 0, 1024)) != -1) {
                        fos.write(b, 0, length);
                    }
                    is.close();
                    fos.close();
                }
            }
            
            if (zipFile != null) {
                zipFile.close();
            }
            
            if (deleteFile) {
                file.deleteOnExit();
            }
            
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param args 参数
     * @throws Exception 异常
     */
    public static void main(String[] args) throws Exception {
        
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param souceFileName 原文件地址
     * @param destFileName 目标文件地址
     */
    public static void zip(String souceFileName, String destFileName) {
        try {
            File souceFile = new File(souceFileName);
            FileOutputStream fileOut = new FileOutputStream(destFileName);
            ZipOutputStream out = new ZipOutputStream(fileOut);
            zip(souceFile, out, "");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param souceFile 原文件
     * @param out 输出流
     * @param base 根路径
     */
    public static void zip(File souceFile, ZipOutputStream out, String base) {
        try {
            if (souceFile.isDirectory()) {
                File[] files = souceFile.listFiles();
                out.putNextEntry(new ZipEntry(base + "/"));
                base = base.length() == 0 ? "" : base + "/";
                for (File file : files) {
                    zip(file, out, base + file.getName());
                }
            } else {
                if (base.length() > 0) {
                    out.putNextEntry(new ZipEntry(base));
                } else {
                    out.putNextEntry(new ZipEntry(souceFile.getName()));
                }
                FileInputStream in = new FileInputStream(souceFile);
                int l;
                byte[] b = new byte[1024];
                while ((l = in.read(b)) != -1) {
                    out.write(b, 0, l);
                }
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
