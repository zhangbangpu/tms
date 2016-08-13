package com.intercept;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class SessionFilter implements HandlerInterceptor {  
	  
    @Override  
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {  
      //  后台session控制  
        Object user=request.getSession().getAttribute("username");  
        String returnUrl = request.getRequestURI();
        if(null==user){  
            if(returnUrl.equals("/public/getImg.shtml")||returnUrl.equals("/public/checkLogin.shtml")||returnUrl.equals("/public/login.shtml")||returnUrl.equals("/public/logOut.shtml")){  
                return true;  
            }else{  
                response.setContentType("text/html");  
                response.setCharacterEncoding("utf-8");  
                PrintWriter out = response.getWriter();    
                StringBuilder builder = new StringBuilder();    
                builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");    
                builder.append("alert(\"请重新登陆！\");");    
                builder.append("window.location.href=\"/public/login.shtml\";");    
                builder.append("</script>");    
                out.print(builder.toString());    
                out.close();    
                return false;  
            }  
        }  
        return true;  
    }  
  
    @Override  
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {  
          
    }  
  
    @Override  
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse response, Object o, Exception e) throws Exception {  
          
    }  
  
  
    /**  
     * @param request  
     * @return Create Date:2013-6-5  
     * @author Shine  
     * Description:获取IP  
     */  
    @SuppressWarnings("unused")
	private String getIpAddr(HttpServletRequest request) {  
        String ip = request.getHeader("x-forwarded-for");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    }  
}  
