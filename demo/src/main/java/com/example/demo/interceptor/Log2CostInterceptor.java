package com.example.demo.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 优先该方式，减少不必要的模板代码
 * 实现其二：继承 HandlerInterceptorAdapter
 */
@Slf4j
public class Log2CostInterceptor extends HandlerInterceptorAdapter {

    private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<>("cost time");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long startTime = System.currentTimeMillis();
        startTimeThreadLocal.set(startTime);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        Long start = startTimeThreadLocal.get();
        long cost = System.currentTimeMillis() - start;
        log.info("Log2CostInterceptor cost:{} ms", cost);
    }
}
