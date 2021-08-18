package com.example.demo.commonspool2;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 对象池配置
 */
@ConfigurationProperties(prefix = PoolProperties.PROJECT_PREFIX)
public class PoolProperties {
    public static final String PROJECT_PREFIX = "project.object";
    /**
     * 最大空闲
     */
    private int maxIdle = 5;

    /**
     * 最小空闲
     */
    private int minIdle = 2;

    /**
     * 最大总数
     */
    private int maxTotal = 20;

    /**
     * 初始化连接数
     */
    private int initialSize = 3;

    /**
     * 最大等待时间
     */
    private int maxWait = 6;

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }
}
