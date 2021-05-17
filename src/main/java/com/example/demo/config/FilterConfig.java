package com.example.demo.config;

import com.example.demo.filter.FilterA;
import com.example.demo.filter.FilterB;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean registerMyFilter() {
        FilterRegistrationBean<FilterA> bean = new FilterRegistrationBean<>();
        bean.setOrder(3);
        bean.setFilter(new FilterA());
        bean.addUrlPatterns("/book/select/*");
        return bean;
    }

    @Bean
    public FilterRegistrationBean registerMyAnotherFilter() {
        FilterRegistrationBean<FilterB> bean = new FilterRegistrationBean<>();
        bean.setOrder(2);
        bean.setFilter(new FilterB());
        bean.addUrlPatterns("/book/select/*");
        return bean;
    }
}

/**
 * | 使用方式                               | 排序 | URL  |
 * | ------------------------------------ | ---- | ---- |
 * | @Component<br/>@Order                | 1    | 0    |
 * | @WebFilter<br/>@ServletComponentScan | 0    | 1    |
 * | JavaConfig                           | 1    | 1    |
 * <p>
 * <p>
 * <p>
 * 1. 使用@Component+@Order
 * 在刚刚定义的MyFilter类上加上@Component和@Order注解，即可被Spring管理
 * 当有多个Filter时，这里的@Order(1)注解会指定执行顺序，数字越小，越优先执行，如果只写@Order，默认顺序值是Integer.MAX_VALUE。
 *
 * @Component+@Order 注解方式配置简单，支持自定义 Filter 顺序。缺点是只能拦截所有URL，不能通过配置去拦截指定的 URL。
 * <p>
 * <p>
 * 2.@WebFilter+@ServletComponentScan
 * 在 MyFilter上添加@WebFilter注解，并在启动类上增加@ServletComponentScan("com.*.filter")注解，参数就是Filter所在的包路径，相当于告诉 SpringBoot，去哪里扫描 Filter
 * @WebFilter+@ServletComponentScan 注解方式支持对 Filter 匹配指定URL，但是不支持指定 Filter 的执行顺序。
 * @WebFilter 这个注解并没有指定执行顺序的属性，其执行顺序依赖于Filter的名称，是根据Filter类名（注意不是配置的filter的名字）的字母顺序倒序排列，并且@WebFilter指定的过滤器优先级都高于FilterRegistrationBean配置的过滤器
 * <p>
 * 3. JavaConfig 配置方式
 * 通过 Java 代码显式配置 Filter ，功能强大，配置灵活。只需要把每个自定义的 Filter 声明成 Bean 交给 Spring 管理即可，还可以设置匹配的 URL 、指定 Filter 的先后顺序。
 */