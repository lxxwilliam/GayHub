package com.calabar.commons.utils;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AppUtil implements ApplicationContextAware {
	static {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai")); //设置时区
	}

	private static ApplicationContext applicationContext;
	private static ServletContext servletContext;

	private static Set<String> allResUrls = new HashSet<String>();

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
	}

	/**
	 * 应用初始化，权限加载等
	 * 
	 * @param _servletContext
	 */
	public static void init(ServletContext _servletContext) {
		servletContext = _servletContext;
		loadResource();
	}

	/**
	 * 加载所有权限信息
	 */
	public synchronized static void loadResource() {
		/*SysResService sysResService = getBean(SysResService.class);
		Set<String> urls = sysResService.findAllUrls();
		setAllResUrls(urls);*/
	}

	public static <T> T getBean(Class<T> type) {
		return applicationContext.getBean(type);
	}

	public static Object getBean(String beanId) {
		return applicationContext.getBean(beanId);
	}

	public static String getAppAbsolutePath() {
		return servletContext.getRealPath("/");
	}

	public static String getRealPath(String path) {
		return servletContext.getRealPath(path);
	}

	public static String getFilePath() {
		String path = ((Properties) getBean("configproperties")).getProperty("file.upload");
		if (StringUtils.isEmpty(path)) {
			path = AppUtil.getRealPath("/attachFiles");
		}
		return path;
	}

	public static Set<String> getAllResUrls() {
		return allResUrls;
	}

	public static void setAllResUrls(Set<String> allResUrls) {
		AppUtil.allResUrls = allResUrls;
	}

}
