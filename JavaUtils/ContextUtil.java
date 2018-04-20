package com.calabar.commons.utils;

public class ContextUtil {
	
	//private static ThreadLocal<SysLoginUser> curUser = new ThreadLocal<>();

	public static void clearAll() {
		//curUser.remove();
		RequestUtil.clearHttpReqResponse();
	}
	
	/**
	 * 获取当前登录后台用户
	 * @return
	 */
//	public static SysLoginUser getCurrentUser() {
//		SysLoginUser user = curUser.get();
//		if(user!=null){
//			return user;
//		}
//		HttpSession session = RequestUtil.getHttpServletRequest().getSession(true);
//		user = (SysLoginUser) session.getAttribute(Const.SESSIONUSER_KEY);
//		curUser.set(user);
//		return user;
//	}
}
