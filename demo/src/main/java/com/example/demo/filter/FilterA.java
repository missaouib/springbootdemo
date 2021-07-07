package com.example.demo.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

/**
 * 当有多个Filter时，这里的@Order(1)注解会指定执行顺序，数字越小，越优先执行，如果只写@Order，默认顺序值是Integer.MAX_VALUE。
 */


@Slf4j
public class FilterA implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.warn("Filter A...");
        // 要继续处理请求，必须添加 filterChain.doFilter()
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
