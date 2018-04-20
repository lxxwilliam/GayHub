package com.calabar.commons.utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public class RequestUtil {
	private static ThreadLocal<HttpServletRequest> reqLocal = new ThreadLocal<HttpServletRequest>();

	private static ThreadLocal<HttpServletResponse> responseLocal = new ThreadLocal<HttpServletResponse>();

	// private static Logger logger =
	// LoggerFactory.getLogger(RequestUtil.class);

	public static void setHttpServletRequest(HttpServletRequest request) {
		reqLocal.set(request);
	}

	public static void clearHttpReqResponse() {
		reqLocal.remove();
		responseLocal.remove();
	}

	public static void setHttpServletResponse(HttpServletResponse response) {
		responseLocal.set(response);
	}

	public static HttpServletRequest getHttpServletRequest() {
		return reqLocal.get();
	}

	public static HttpServletResponse getHttpServletResponse() {
		return responseLocal.get();
	}

	public static String getString(String key, String defaultValue, boolean b) {
		String value = getHttpServletRequest().getParameter(key);
		if (StringUtils.isEmpty(value))
			return defaultValue;
		if (b) {
			return value.replace("'", "''").trim();
		}
		return value.trim();
	}

	public static String getString(String key) {
		return getString(key, "", true);
	}

	public static String getString(String key, boolean b) {
		return getString(key, "", b);
	}

	public static String getString(String key, String defaultValue) {
		return getString(key, defaultValue, true);
	}

	public static String getStringAry(String key) {
		String[] aryValue = getHttpServletRequest().getParameterValues(key);
		if ((aryValue == null) || (aryValue.length == 0)) {
			return "";
		}
		String tmp = "";
		for (String v : aryValue) {
			if ("".equals(tmp)) {
				tmp = new StringBuilder().append(tmp).append(v).toString();
			} else {
				tmp = new StringBuilder().append(tmp).append(",").append(v).toString();
			}
		}
		return tmp;
	}

	public static String getSecureString(String key, String defaultValue) {
		String value = getHttpServletRequest().getParameter(key);
		if (StringUtils.isEmpty(value))
			return defaultValue;
		return filterInject(value);
	}

	public static String getSecureString(String key) {
		return getSecureString(key, "");
	}

	public static String filterInject(String str) {
		String injectstr = "\\||;|exec|insert|select|delete|update|count|chr|truncate|char";
		Pattern regex = Pattern.compile(injectstr, 226);

		Matcher matcher = regex.matcher(str);
		str = matcher.replaceAll("");
		str = str.replace("'", "''");
		str = str.replace("-", "—");
		str = str.replace("(", "（");
		str = str.replace(")", "）");
		str = str.replace("%", "％");

		return str;
	}

	public static String getLowercaseString(String key) {
		return getString(key).toLowerCase();
	}

	public static int getInt(String key) {
		return getInt(key, 0);
	}

	public static int getInt(String key, int defaultValue) {
		String str = getHttpServletRequest().getParameter(key);
		if (StringUtils.isEmpty(str))
			return defaultValue;
		return Integer.parseInt(str);
	}

	public static long getLong(String key) {
		return getLong(key, 0L);
	}

	public static Long[] getLongAry(String key) {
		String[] aryKey = getHttpServletRequest().getParameterValues(key);
		if (BeanUtils.isEmpty(aryKey))
			return null;
		Long[] aryLong = new Long[aryKey.length];
		for (int i = 0; i < aryKey.length; i++) {
			aryLong[i] = Long.valueOf(Long.parseLong(aryKey[i]));
		}
		return aryLong;
	}

	public static Long[] getLongAryByStr(String key) {
		String str = getHttpServletRequest().getParameter(key);
		if (StringUtils.isEmpty(str))
			return null;
		String[] aryId = str.split(",");
		Long[] lAryId = new Long[aryId.length];
		for (int i = 0; i < aryId.length; i++) {
			lAryId[i] = Long.valueOf(Long.parseLong(aryId[i]));
		}
		return lAryId;
	}

	public static String[] getStringAryByStr(String key) {
		String str = getHttpServletRequest().getParameter(key);
		if (StringUtils.isEmpty(str))
			return null;
		String[] aryId = str.split(",");
		String[] lAryId = new String[aryId.length];
		for (int i = 0; i < aryId.length; i++) {
			lAryId[i] = aryId[i];
		}
		return lAryId;
	}

	public static Integer[] getIntAry(String key) {
		String[] aryKey = getHttpServletRequest().getParameterValues(key);
		if (BeanUtils.isEmpty(aryKey))
			return null;
		Integer[] aryInt = new Integer[aryKey.length];
		for (int i = 0; i < aryKey.length; i++) {
			aryInt[i] = Integer.valueOf(Integer.parseInt(aryKey[i]));
		}
		return aryInt;
	}

	public static Float[] getFloatAry(String key) {
		String[] aryKey = getHttpServletRequest().getParameterValues(key);
		if (BeanUtils.isEmpty(aryKey))
			return null;
		Float[] fAryId = new Float[aryKey.length];
		for (int i = 0; i < aryKey.length; i++) {
			fAryId[i] = Float.valueOf(Float.parseFloat(aryKey[i]));
		}
		return fAryId;
	}

	public static long getLong(String key, long defaultValue) {
		String str = getHttpServletRequest().getParameter(key);
		if (StringUtils.isEmpty(str))
			return defaultValue;
		return Long.parseLong(str.trim());
	}

	public static Long getLong(String key, Long defaultValue) {
		String str = getHttpServletRequest().getParameter(key);
		if (StringUtils.isEmpty(str))
			return defaultValue;
		return Long.valueOf(Long.parseLong(str.trim()));
	}

	public static float getFloat(String key) {
		return getFloat(key, 0.0F);
	}

	public static float getFloat(String key, float defaultValue) {
		String str = getHttpServletRequest().getParameter(key);
		if (StringUtils.isEmpty(str))
			return defaultValue;
		return Float.parseFloat(str);
	}

	public static boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		String str = getHttpServletRequest().getParameter(key);
		if (StringUtils.isEmpty(str))
			return defaultValue;
		if (StringUtils.isNumeric(str))
			return Integer.parseInt(str) == 1;
		return Boolean.parseBoolean(str);
	}

	public static Short getShort(String key) {
		return getShort(key, (short) 0);
	}

	public static Short getShort(String key, Short defaultValue) {
		String str = getHttpServletRequest().getParameter(key);
		if (StringUtils.isEmpty(str))
			return defaultValue;
		return Short.valueOf(Short.parseShort(str));
	}

	/*
	 * public static Date getDate(String key, String style) throws
	 * ParseException { String str = getHttpServletRequest().getParameter(key);
	 * if (StringUtils.isEmpty(str)) return null; if
	 * (StringUtils.isEmpty(style)) style = "yyyy-MM-dd HH:mm:ss"; return
	 * DateFormatUtil.parse(str, style); }
	 * 
	 * public static Date getDate(String key) throws ParseException { String str
	 * = getHttpServletRequest().getParameter(key); if
	 * (StringUtils.isEmpty(str)) return null; return
	 * DateFormatUtil.parseDate(str); }
	 * 
	 * public static Date getTimestamp(String key) throws ParseException {
	 * String str = getHttpServletRequest().getParameter(key); if
	 * (StringUtils.isEmpty(str)) return null; return
	 * DateFormatUtil.parseDateTime(str); }
	 */

	@SuppressWarnings("rawtypes")
	public static String getUrl() {
		StringBuffer urlThisPage = new StringBuffer();
		urlThisPage.append(getHttpServletRequest().getRequestURI());
		Enumeration e = getHttpServletRequest().getParameterNames();
		String para = "";
		String values = "";
		urlThisPage.append("?");
		while (e.hasMoreElements()) {
			para = (String) e.nextElement();
			values = getHttpServletRequest().getParameter(para);
			urlThisPage.append(para);
			urlThisPage.append("=");
			urlThisPage.append(values);
			urlThisPage.append("&");
		}
		return urlThisPage.substring(0, urlThisPage.length() - 1);
	}

	public static String getPrePage() {
		HttpServletRequest request = getHttpServletRequest();
		String returnUrl = getString("returnUrl");
		if (StringUtils.isEmpty(returnUrl)) {
			returnUrl = request.getHeader("Referer");
		}
		return returnUrl;
	}

	public static final String getErrorUrl() {
		HttpServletRequest request = getHttpServletRequest();
		String errorUrl = (String) request.getAttribute("javax.servlet.error.request_uri");
		if (errorUrl == null) {
			errorUrl = (String) request.getAttribute("javax.servlet.forward.request_uri");
		}
		if (errorUrl == null) {
			errorUrl = (String) request.getAttribute("javax.servlet.include.request_uri");
		}
		if (errorUrl == null) {
			errorUrl = request.getRequestURL().toString();
		}
		return errorUrl;
	}

	public static String getIpAddr() {
		HttpServletRequest request = getHttpServletRequest();
		String ip = request.getHeader("x-forwarded-for");
		if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static Map<String, Object[]> getQueryMap() {
		Map<String, Object[]> map = new HashMap<>();
		Enumeration<String> params = getHttpServletRequest().getParameterNames();
		while (params.hasMoreElements()) {
			String key = params.nextElement();
			String value = getHttpServletRequest().getParameter(key);
			if (StringUtils.isNotBlank(value)) {
				if (key.startsWith("Q_")) {
					map.put(key, new Object[] { value.trim() });
				}
			}
		}
		return map;
	}

	public static HttpSession getSession() {
		return getHttpServletRequest().getSession(true);
	}

	public static List<Order> getOrders() {
		List<Order> orders = new ArrayList<>();
		Enumeration<String> params = getHttpServletRequest().getParameterNames();
		while (params.hasMoreElements()) {
			String key = params.nextElement();
			// String value = getHttpServletRequest().getParameter(key);
			if (key.startsWith("S_")) {
				String[] ss = key.split("[_]");
				if (ss.length == 3) {
					if ("desc".equalsIgnoreCase(ss[2])) {
						orders.add(new Order(Sort.Direction.DESC, ss[1]));
					} else {
						orders.add(new Order(Sort.Direction.ASC, ss[1]));
					}

				}
			}
		}
		return orders;
	}

	public static boolean isWx() {
		String ua = getHttpServletRequest().getHeader("user-agent").toLowerCase();
		if (ua.indexOf("micromessenger") > 0) {// 是微信浏览器
			return true;
		}
		return false;
	}
}
