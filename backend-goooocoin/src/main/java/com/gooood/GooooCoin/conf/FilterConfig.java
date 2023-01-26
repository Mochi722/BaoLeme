package com.gooood.GooooCoin.conf;

import com.gooood.GooooCoin.PO.User;
import com.gooood.GooooCoin.filter.AdminFilter;
import com.gooood.GooooCoin.filter.UserFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<AdminFilter> adminFilter() {
        FilterRegistrationBean<AdminFilter> filterRegBean = new FilterRegistrationBean<>();
        filterRegBean.setFilter(new AdminFilter());
        filterRegBean.addUrlPatterns("/admin/*");
        filterRegBean.setOrder(Ordered.LOWEST_PRECEDENCE -1);
        return filterRegBean;
    }
    @Bean
    public FilterRegistrationBean<UserFilter> userFilter() {
        FilterRegistrationBean<UserFilter> filterRegBean = new FilterRegistrationBean<>();
        filterRegBean.setFilter(new UserFilter());
        filterRegBean.addUrlPatterns("/user/modifyPassword","/user/modifyFavor","/user/collect","/user/getCollection","/user/deleteCollection");
        filterRegBean.setOrder(Ordered.LOWEST_PRECEDENCE -2);
        return filterRegBean;
    }
}
