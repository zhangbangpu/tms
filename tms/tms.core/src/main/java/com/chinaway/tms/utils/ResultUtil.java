package com.chinaway.tms.utils;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 请求工具类
 * @author zhang
 *
 */
public class ResultUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(RequestUtil.class);
	private static final Base64 base64 = new Base64(true);
	public static final String LAST_PAGE = "lastPage";// 未登录时访问的页面
	public static final String REDIRECT_HOME = "/";// 未登录时跳转到首页
	public static final String LOGIN_HOME = "/index.jsp";// 登录成功后进入的页面

	/**
	 * 保存当前请求
	 */
	public static void saveRequest(HttpServletRequest request) {
		request.getSession().setAttribute(LAST_PAGE, RequestUtil.hashRequestPage(request));
		logger.debug("被拦截的url的sessionID:{}", request.getSession().getId());
		logger.debug("save request for {}", request.getRequestURI());
	}

	/**
	 * 加密请求页面
	 * 
	 * @param request
	 * @return
	 */
	public static String hashRequestPage(HttpServletRequest request) {
		String reqUri = request.getRequestURI();
		String query = request.getQueryString();
		if (query != null) {
			reqUri += "?" + query;
		}
		String targetPage = null;
		try {
			targetPage = base64.encodeAsString(reqUri.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException ex) {
			// this does not happen
		}
		return targetPage;
	}

	/**
	 * 取出之前保存的请求
	 * 
	 * @return
	 */
	public static String retrieveSavedRequest(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session == null) {
			return REDIRECT_HOME;
		}
		String HashedlastPage = (String) session.getAttribute(LAST_PAGE);
		if (HashedlastPage == null) {
			return LOGIN_HOME;
		} else {
			return retrieve(HashedlastPage);
		}
	}

	/**
	 * 解密请求的页面
	 * 
	 * @param targetPage
	 * @return
	 */
	public static String retrieve(String targetPage) {
		byte[] decode = base64.decode(targetPage);
		try {
			String requestUri = new String(decode, "UTF-8");
			int i = requestUri.indexOf("/", 1);
			return requestUri.substring(i);
		} catch (UnsupportedEncodingException ex) {
			// this does not happen
			return null;
		}
	}
}
