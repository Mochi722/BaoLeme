package com.gooood.GooooCoin.filter;


import com.alibaba.fastjson.JSONObject;
import com.gooood.GooooCoin.VO.ResponseVO;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

@WebFilter(filterName = "AdminFilter",urlPatterns = "/admin/*")
public class AdminFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("filter");
        Cookie[] cookies = ((HttpServletRequest)servletRequest).getCookies();
        boolean checkPass=false;
        if (null != cookies) {
            for (Cookie cookie : cookies)
                if (cookie.getName().equals("goooocoinUserType")){
                    if(cookie.getValue().equals("1")){
                        checkPass=true;
                        filterChain.doFilter(servletRequest, servletResponse);
                    }
                }
        }
        if(!checkPass) {
            servletResponse.setCharacterEncoding("UTF-8");
            servletResponse.setContentType("application/json; charset=utf-8");
            try (PrintWriter writer = servletResponse.getWriter()) {
                writer.append(JSONObject.toJSONString(ResponseVO.builder().success(false).message("不是管理员").build()));
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    @Override
    public void destroy() {
    }

}
