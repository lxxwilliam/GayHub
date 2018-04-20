package com.calabar.commons.utils;

import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * <p/>
 * <li>Description:TODO</li>
 * <li>@author: Administrator</li>
 * <li>Date: 2017年9月8日 下午3:51:49</li>
 */
@SuppressWarnings("deprecation")
public class HttpUtil {
    
    /**
     * 
     */
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * 
     */
    private static HttpUtil instance;
    
    /**
     * 
     */
    private HttpClient httpclient;
    
    /**
     * 
     * <li> HttpUtil的构造函数. </li>
     *
     */
    private HttpUtil() {
        try {
            httpclient = new DefaultHttpClient();
            
            // 创建TrustManager()
            // 用于解决javax.net.ssl.SSLPeerUnverifiedException: peer not
            // authenticated
            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
                
                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
                
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            // 创建HostnameVerifier
            // 用于解决javax.net.ssl.SSLException: hostname in certificate didn't
            // match: <123.125.97.66> != <123.125.97.241>
            X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
                @Override
                public void verify(String host, SSLSocket ssl) throws IOException {
                }
                
                @Override
                public void verify(String host, X509Certificate cert) throws SSLException {
                }
                
                @Override
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }
                
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            };
            // TLS1.0与SSL3.0基本上没有太大的差别,可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
            SSLContext sslContext = SSLContext.getInstance(SSLSocketFactory.TLS);
            // 使用TrustManager来初始化该上下文,TrustManager只是被SSL的Socket所使用
            sslContext.init(null, new TrustManager[] { trustManager }, null);
            // 创建SSLSocketFactory
            SSLSocketFactory socketFactory = new SSLSocketFactory(sslContext, hostnameVerifier);
            // 通过SchemeRegistry将SSLSocketFactory注册到HttpClient上
            httpclient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @return HTTP实例
     */
    public static HttpUtil instance() {
        if (instance == null) {
            instance = new HttpUtil();
        }
        return instance;
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param url 访问地址
     * @param charset 编码
     * @return 内容
     */
    public String get(String url, String charset) {
        String result = "";
        HttpGet httpget = new HttpGet(url);
        try {
            HttpResponse response = httpclient.execute(httpget);
            if (StringUtils.isEmpty(charset)) {
                String defaultCharset = "iso-8859-1";
                Header contentTypeHeader = response.getFirstHeader("Content-Type");
                String contentType = contentTypeHeader.getValue().toLowerCase();
                if ((contentType.contains("gbk")) || (contentType.contains("gb2312"))
                        || (contentType.contains("gb18030"))) {
                    defaultCharset = "gb18030";
                } else if (contentType.contains("utf-8")) {
                    defaultCharset = "utf-8";
                } else if (contentType.contains("big5")) {
                    defaultCharset = "big5";
                }
                charset = defaultCharset;
            }
            
            StatusLine line = response.getStatusLine();
            if (line.getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, charset);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
//        httpget.completed();
        return result;
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param url 访问路径
     * @return 内容
     */
    public String get(String url) {
        return get(url, null);
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param url 访问路径
     * @param params 参数
     * @param charset 编码
     * @return 内容
     */
    public String post(String url, Map<String, String> params, String charset) {
        String result = "";
        HttpPost httppost = new HttpPost(url);
        
        // 创建参数队列
        List<NameValuePair> formparams = new ArrayList<>();
        if (params != null) {
            for (String key : params.keySet()) {
                formparams.add(new BasicNameValuePair(key, params.get(key)));
            }
        }
        
        try {
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            HttpResponse response = httpclient.execute(httppost);
            if (StringUtils.isEmpty(charset)) {
                String defaultCharset = "iso-8859-1";
                Header contentTypeHeader = response.getFirstHeader("Content-Type");
                if (contentTypeHeader != null) {
                    String contentType = contentTypeHeader.getValue().toLowerCase();
                    if ((contentType.contains("gbk")) || (contentType.contains("gb2312"))
                            || (contentType.contains("gb18030"))) {
                        defaultCharset = "gb18030";
                    } else if (contentType.contains("utf-8")) {
                        defaultCharset = "utf-8";
                    } else if (contentType.contains("big5")) {
                        defaultCharset = "big5";
                    }
                }
                charset = defaultCharset;
            }
            
            StatusLine line = response.getStatusLine();
            if (line.getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, charset);
            }
        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
            logger.error(url + "访问agent异常,请确认节点代理是否启动?"+e.getMessage());
            //e.printStackTrace();
        }
//        httppost.completed();
        return result;
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param url 访问路径
     * @param params 参数
     * @return 内容
     */
    public String post(String url, Map<String, String> params) {
        return post(url, params, null);
    }
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param url 访问路径
     * @param params 参数
     * @return 内容
     */
    public String postCluster(String url, Map<String, String> params) {
        return postCluster(url, params, null);
    }
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param url 访问路径
     * @param params 参数
     * @param charset 编码
     * @return 内容
     */
    public String postCluster(String url, Map<String, String> params, String charset) {
        String result = "";
        HttpPost httppost = new HttpPost(url);
        
        // 创建参数队列
        List<NameValuePair> formparams = new ArrayList<>();
        if (params != null) {
            for (String key : params.keySet()) {
                formparams.add(new BasicNameValuePair(key, params.get(key)));
            }
        }
        
        try {
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            HttpResponse response = httpclient.execute(httppost);
            if (StringUtils.isEmpty(charset)) {
                String defaultCharset = "iso-8859-1";
                Header contentTypeHeader = response.getFirstHeader("Content-Type");
                if (contentTypeHeader != null) {
                    String contentType = contentTypeHeader.getValue().toLowerCase();
                    if ((contentType.contains("gbk")) || (contentType.contains("gb2312"))
                            || (contentType.contains("gb18030"))) {
                        defaultCharset = "gb18030";
                    } else if (contentType.contains("utf-8")) {
                        defaultCharset = "utf-8";
                    } else if (contentType.contains("big5")) {
                        defaultCharset = "big5";
                    }
                }
                charset = defaultCharset;
            }
            
            StatusLine line = response.getStatusLine();
            if (line.getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, charset);
            }
        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
            logger.error(url+"访问agent异常,请确认节点代理是否启动?"+e.getMessage());
            //e.printStackTrace();
        }
//        httppost.completed();
        return result;
    }
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param url 访问路径
     * @return 内容
     */
    public String post(String url) {
        return post(url, null, null);
    }
    
    /**
     * 
     * <li>Description: http上传文件</li>
     *
     * @param uploadUrl 上传路径
     * @param params 参数
     * @param upfile 上传的文件
     * @param fileName 文件名称
     * @return 显示内容
     */
    public String uploadFile(String uploadUrl, Map<String, String> params, File upfile, String fileName) {
        try {
            HttpPost post = new HttpPost(uploadUrl);
            FileBody fileBody = new FileBody(upfile, ContentType.DEFAULT_BINARY, fileName);
            
            // entity.addPart("file", fileBody);
            // 创建待处理的表单域内容文本
            StringBody name = new StringBody("mdmnewsfiles", ContentType.create("text/plain", Consts.UTF_8));
            MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .addPart("file", fileBody)
                    .addPart("fileType", name)
                    .setCharset(CharsetUtils.get("UTF-8"));
            if (params != null) {
                for (String key : params.keySet()) {
                    //builder.addTextBody(key, params.get(key));
                    //中文参数提交
                    builder.addPart(key,
                            new StringBody(params.get(key), ContentType.create("text/plain", Consts.UTF_8)));
                }
            }
            HttpEntity entity = builder.build();
            post.setEntity(entity);
            HttpResponse response = httpclient.execute(post);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                HttpEntity httpentity = response.getEntity();
                // 显示内容
                if (entity != null) {
                    String retult = EntityUtils.toString(httpentity);
                    return retult;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * 
     * <li>Description: TODO</li>
     *
     * @param args 参数
     * @throws Exception 异常
     */
    public static void main(String[] args) throws Exception {
        // uploadFile("http://192.168.0.251:8080/res/resoures/resFile/upload.htm",
        // new File(
        // "D:\\work\\微信\\数据\\日报周报月报\\XML格式1.1.docx"), "测试.docx");
        Map<String, String> params = new HashMap<>();
        params.put("action", "login");
        params.put("username", "azkaban1");
        params.put("password", "azkaban");
        String result = HttpUtil.instance().post("https://192.168.1.83:8443/", params);
        System.out.println(result);
    }
    
    /**
     * 发送请求
     * @param url 请求地址
     * @param param  请求参数对象
     * @return  从服务端获取的数据
     * @throws Exception 操作失败时，抛出异常
     */
    public String doRequest(String url, Object param) throws Exception {
        // 将对象转换为JSON 对象
        if (param instanceof JSONObject) {
        } else {
            param = JSON.toJSON(param);
        }
        logger.info("开始请求服务: " + url.toString());
        logger.info("请求参数 : " + param.toString());

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpMethod = new HttpPost(url);
            httpMethod.addHeader("Content-Type", "application/json;charset=UTF-8");
            httpMethod.setHeader("Accept", "application/json");
            StringEntity stringEntity = new StringEntity(param.toString(), "UTF-8");
            httpMethod.setEntity(stringEntity);
            CloseableHttpResponse httpResponse = httpclient.execute(httpMethod);
            try {
                HttpEntity respEntiy = httpResponse.getEntity();
                String json = EntityUtils.toString(respEntiy, "UTF-8");
                logger.info("收到服务端对请求(" + url.toString() + ")的应答数据: " + json);
                return json;
            } finally {
                httpResponse.close();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                httpclient.close();
              } catch (Exception e) {
              }
        }
    }
    
}