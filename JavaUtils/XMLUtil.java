package com.calabar.commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.calabar.mda.entity.taskflow.TaskDefUpServiceEntity;
import com.calabar.mda.entity.taskflow.TaskDefinitionEntity;
import com.calabar.mda.entity.taskmonitor.TaskServiceEntity;

/**
 * 
 * <p/>
 * <li>Description:生成XML</li>
 * <li>@author: lanxg</li>
 * <li>Date: 2017年9月8日 下午2:54:52</li>
 */
public class XMLUtil {
    /**
     * 
     * @param tdf 
     * @param fileurl 文件上传地址
     * @param isService  是否发布为服务
     * <li>Description: Dom生成xml文件</li>
     *
     */
    public static void createXMLFile(TaskServiceEntity tdf, String fileurl, String isService) {
        Document document = DocumentHelper.createDocument(); // 实例化一个文档
        
        Element root = document.addElement("configuration"); // 添加根元素
        root.addComment("计算任务包xml文件设置"); // 添加注释
        
        Element element1 = root.addElement("property");
        Element name = element1.addElement("name");
        name.setText("language");
        Element value = element1.addElement("value");
        String language = "";
        
        if ("c".equals(tdf.getDevelopLanguage().name())) {
            language = "0";
        } else if ("java".equals(tdf.getDevelopLanguage().name())) {
            language = "2";
        } else if ("scals".equals(tdf.getDevelopLanguage().name())) {
            language = "2";
        } else {
            language = "3";
        }
        value.setText(language);
        Element description = element1.addElement("description");
        description.setText("支持的语言类型：0:C++, 1:java, 2:jar, 3:python");
        
        Element element2 = root.addElement("property");
        Element name2 = element2.addElement("name");
        name2.setText("exe");
        Element value2 = element2.addElement("value");
        value2.setText(tdf.getFilePath());
        Element description2 = element2.addElement("description");
        description2.setText("可执行文件路径。如果是C++、python程序，此为可执行文件名。如果是Java，此为主类名");
        
        Element element3 = root.addElement("property");
        Element name3 = element3.addElement("name");
        name3.setText("exe.param");
        Element value3 = element3.addElement("value");
        value3.setText(tdf.getParam());
        Element description3 = element3.addElement("description");
        description3.setText("执行参数");
        Element element4 = root.addElement("property");
        Element name4 = element4.addElement("name");
        name4.setText("calc.platform");
        Element value4 = element4.addElement("value");
        String platform = "";
        if ("other".equals(tdf.getPlatform().name())) {
            platform = "1";
        } else if ("Spark".equals(tdf.getPlatform().name())) {
            platform = "2";
        } else if ("Storm".equals(tdf.getPlatform().name())) {
            platform = "3";
        } else if ("HeterogeneousCluster".equals(tdf.getPlatform().name())) {
            platform = "4";
        } else {
            platform = "5";
        }
        value4.setText(platform);
        Element description4 = element4.addElement("description");
        description4.setText("计算平台：1:其它、2:Spark、3:Strom、4:异构、5:MXNET");
        
        Element element5 = root.addElement("property");
        Element name5 = element5.addElement("name");
        name5.setText("os");
        Element value5 = element5.addElement("value");
        String os = "";
        String system = tdf.getPoperatingSystem().name();
        String systemName = system.substring(0, system.length() - 2);
        if ("lnux".equals(systemName)) {
            os = "0";
        } else {
            os = "1";
        }
        value5.setText(os);
        Element description5 = element5.addElement("description");
        description5.setText("任务运行的操作系统类型：0:linux 、1:win7");
        
        Element element6 = root.addElement("property");
        Element name6 = element6.addElement("name");
        name6.setText("os.bit");
        Element value6 = element6.addElement("value");
        String osbit = "";
        system = tdf.getPoperatingSystem().name();
        String systemNum = system.substring(system.length() - 2, system.length());
        if ("32".equals(systemNum)) {
            osbit = "0";
        } else {
            osbit = "1";
        }
        value6.setText(osbit);
        Element description6 = element6.addElement("description");
        description6.setText("操作系统位数：0:32, 1:64");
        if ("1".equals(isService)) {
            Element element7 = root.addElement("property");
            Element name7 = element7.addElement("name");
            name7.setText("run.type");
            Element value7 = element7.addElement("value");
            value7.setText("1");
            Element description7 = element7.addElement("description");
            description7.setText("当此配置项为1时，表示需要将当前任务发布为服务");
        } else {
            Element element7 = root.addElement("property");
            Element name7 = element7.addElement("name");
            name7.setText("run.type");
            Element value7 = element7.addElement("value");
            value7.setText("0");
            Element description7 = element7.addElement("description");
            description7.setText("当此配置项为1时，表示需要将当前任务发布为服务");
        }
        
        try {
            OutputFormat format = OutputFormat.createPrettyPrint(); // 实例化格式化输出对象
            format.setEncoding("UTF-8"); // 设置编码
            FileOutputStream fos = new FileOutputStream(fileurl + "conf.xml");
            XMLWriter writer = new XMLWriter(fos, format);
            writer.write(document);
            writer.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 
     * @param tdf 
     * @param fileurl 文件上传地址
     * @param isService  是否发布为服务
     * <li>Description: Dom生成xml文件</li>
     *
     */
    public static void createXMLFile(TaskDefinitionEntity tdf, String fileurl, String isService) {
        Document document = DocumentHelper.createDocument(); // 实例化一个文档
        
        Element root = document.addElement("configuration"); // 添加根元素
        root.addComment("计算任务包xml文件设置"); // 添加注释
        
        Element element1 = root.addElement("property");
        Element name = element1.addElement("name");
        name.setText("language");
        Element value = element1.addElement("value");
        String language = "";
        
        if ("c".equals(tdf.getDevelopLanguage().name())) {
            language = "0";
        } else if ("java".equals(tdf.getDevelopLanguage().name())) {
            language = "2";
        } else if ("scals".equals(tdf.getDevelopLanguage().name())) {
            language = "2";
        } else {
            language = "3";
        }
        value.setText(language);
        Element description = element1.addElement("description");
        description.setText("支持的语言类型：0:C++, 1:java, 2:jar, 3:python");
        
        Element element2 = root.addElement("property");
        Element name2 = element2.addElement("name");
        name2.setText("exe");
        Element value2 = element2.addElement("value");
        value2.setText(tdf.getFilePath());
        Element description2 = element2.addElement("description");
        description2.setText("可执行文件路径。如果是C++、python程序，此为可执行文件名。如果是Java，此为主类名");
        
        Element element3 = root.addElement("property");
        Element name3 = element3.addElement("name");
        name3.setText("exe.param");
        Element value3 = element3.addElement("value");
        value3.setText(tdf.getParams());
        Element description3 = element3.addElement("description");
        description3.setText("执行参数");
        Element element4 = root.addElement("property");
        Element name4 = element4.addElement("name");
        name4.setText("calc.platform");
        Element value4 = element4.addElement("value");
        String platform = "";
        if ("other".equals(tdf.getPlatform().name())) {
            platform = "1";
        } else if ("Spark".equals(tdf.getPlatform().name())) {
            platform = "2";
        } else if ("Storm".equals(tdf.getPlatform().name())) {
            platform = "3";
        } else if ("HeterogeneousCluster".equals(tdf.getPlatform().name())) {
            platform = "4";
        } else {
            platform = "5";
        }
        value4.setText(platform);
        Element description4 = element4.addElement("description");
        description4.setText("计算平台：1:其它、2:Spark、3:Strom、4:异构、5:MXNET");
        
        Element element5 = root.addElement("property");
        Element name5 = element5.addElement("name");
        name5.setText("os");
        Element value5 = element5.addElement("value");
        String os = "";
        String system = tdf.getPoperatingSystem().name();
        String systemName = system.substring(0, system.length() - 2);
        if ("lnux".equals(systemName)) {
            os = "0";
        } else {
            os = "1";
        }
        value5.setText(os);
        Element description5 = element5.addElement("description");
        description5.setText("任务运行的操作系统类型：0:linux 、1:win7");
        
        Element element6 = root.addElement("property");
        Element name6 = element6.addElement("name");
        name6.setText("os.bit");
        Element value6 = element6.addElement("value");
        String osbit = "";
        system = tdf.getPoperatingSystem().name();
        String systemNum = system.substring(system.length() - 2, system.length());
        if ("32".equals(systemNum)) {
            osbit = "0";
        } else {
            osbit = "1";
        }
        value6.setText(osbit);
        Element description6 = element6.addElement("description");
        description6.setText("操作系统位数：0:32, 1:64");
        if ("1".equals(isService)) {
            Element element7 = root.addElement("property");
            Element name7 = element7.addElement("name");
            name7.setText("run.type");
            Element value7 = element7.addElement("value");
            value7.setText("1");
            Element description7 = element7.addElement("description");
            description7.setText("当此配置项为1时，表示需要将当前任务发布为服务");
        } else {
            Element element7 = root.addElement("property");
            Element name7 = element7.addElement("name");
            name7.setText("run.type");
            Element value7 = element7.addElement("value");
            value7.setText("0");
            Element description7 = element7.addElement("description");
            description7.setText("当此配置项为1时，表示需要将当前任务发布为服务");
        }
        
        try {
            OutputFormat format = OutputFormat.createPrettyPrint(); // 实例化格式化输出对象
            format.setEncoding("UTF-8"); // 设置编码
            FileOutputStream fos = new FileOutputStream(fileurl + "conf.xml");
            XMLWriter writer = new XMLWriter(fos, format);
            writer.write(document);
            writer.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * <li>Description: 更新xml文件内容</li>
     *
     * @param url 
     * @param tdfs 
     * @throws Exception 
     */
    public static void changeXml(String url, TaskDefUpServiceEntity tdfs) throws Exception {
        
        File xmlFile = new File(url);
        FileInputStream fis = null;
        fis = new FileInputStream(xmlFile);
        SAXReader saxReader = new SAXReader(); //解析xml文件
        List rowList = null;
        try {
            //生成文档对应实体  
            Document doc = saxReader.read(fis);
            //获取指定路径下的元素列表,这里指获取所有的data下的row元素  
            rowList = doc.selectNodes("//configuration/property");
            for ( Iterator iter = rowList.iterator(); iter.hasNext();) {
                //获得具体的row元素   
                List nodesValList = null;
                //获得具体的row元素   
                List nodesNameList = null;
                Element element = (Element) iter.next();
                //获得row元素的所有属性列表  
                //Document elementList = element.getDocument();
                nodesValList = element.selectNodes("value");
                nodesNameList = element.selectNodes("name");
                Element nodesName = (Element) nodesNameList.get(0);
                if ("spark.master.ip-port".equals(nodesName.getText())) {
                    if (tdfs.getSparkIdPort() != null) {
                        ((Node) nodesValList.get(0)).setText(tdfs.getSparkIdPort());
                    }
                } else if ("storm.master.ip-port".equals(nodesName.getText())) {
                    if (tdfs.getStormIdPort() != null) {
                        ((Node) nodesValList.get(0)).setText(tdfs.getStormIdPort());
                    }
                } else if ("mxnet.master.ip-port".equals(nodesName.getText())) {
                    if (tdfs.getMxnetIdPort() != null) {
                        ((Node) nodesValList.get(0)).setText(tdfs.getMxnetIdPort());
                    }
                } else if ("yg.master.ip-portt".equals(nodesName.getText())) {
                    if (tdfs.getYgIdPort() != null) {
                        ((Node) nodesValList.get(0)).setText(tdfs.getYgIdPort());
                    }
                } else if ("grpc.server.ip".equals(nodesName.getText())) {
                    if (tdfs.getGrpcService() != null) {
                        ((Node) nodesValList.get(0)).setText(tdfs.getGrpcService());
                    }
                } else if ("grpc.server.name".equals(nodesName.getText())) {
                    if (tdfs.getGrpcServiceName() != null) {
                        ((Node) nodesValList.get(0)).setText(tdfs.getGrpcServiceName());
                    }
                }
            }
            try {
                //将更新后的内容写入到xml文件中
                XMLWriter writer = new XMLWriter(new FileOutputStream(url));
                writer.write(doc);
                writer.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block  
                e.printStackTrace();
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param args 
     */
    public static void main(String args[]) {
        
        //XMLUtil.createXMLFile();
    }
    
}
