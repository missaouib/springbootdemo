package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 1、默认情况下（即@EnableAsync注解的mode=AdviceMode.PROXY），
 * 同一个类内部没有使用@Async注解修饰的方法调用@Async注解修饰的方法，是不会异步执行的，
 * 这点跟 @Transitional 注解类似，底层都是通过动态代理实现的。
 * 如果想实现类内部自调用也可以异步，则需要切换@EnableAsync注解的mode=AdviceMode.ASPECTJ，详见@EnableAsync注解。
 *
 * 如果使用@EnableAsync注解开启了Spring的异步功能，Spring会按照如下的方式查找相应的线程池用于执行异步方法：
 * 1.查找实现了TaskExecutor接口的Bean实例
 * 2.如果上面没有找到，则查找名称为taskExecutor并且实现了Executor接口的Bean实例
 * 3.如果还是没有找到，则使用SimpleAsyncTaskExecutor，该实现每次都会创建一个新的线程执行任务
 * 上面的1,2步的Bean实例都是需要自己配置的，可以使用Spring实现的线程池，使用Spring实现的线程池不需要配置Bean的名称，
 * 也可以使用JDK实现的线程池，但是需要配置Bean名称为taskExecutor
 *
 * @Async 标记的方法只能是 void 或者 Future 返回值
 *
 * 异常处理
 * 1.对于无返回值的异步任务，配置AsyncExceptionConfig类，统一处理。
 * 2.对于有返回值的异步任务，可以在contoller层捕获异常，进行处理。
 *
 */

@Configuration
@Slf4j
public class AsyncConfig implements AsyncConfigurer {
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final int QUEUE_CAPACITY = 100;
    private static final int AWAIT_TIME = 60;
    private static final int KEEP_ALIVE = 60;


    /**
     * 通过注解 @Async("taskExecutorA") 选择不同的线程池
     *
     * @return
     */
    @Bean
    public Executor taskExecutorA() {
        // Spring 默认配置是核心线程数大小为1，最大线程容量大小不受限制，队列容量也不受限制。
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数
        executor.setCorePoolSize(CORE_POOL_SIZE);
        // 最大线程数
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        // 队列大小
        executor.setQueueCapacity(QUEUE_CAPACITY);
        // 当最大池已满时，此策略保证不会丢失任务请求，但是可能会影响应用程序整体性能。
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //该方法用来设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住。 // 等待时间 （默认为0，此时立即停止），并没等待xx秒后强制停止
        executor.setAwaitTerminationSeconds(AWAIT_TIME);
        //用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean，这样这些异步任务的销毁就会先于Redis线程池的销毁。 //等待任务在关机时完成--表明等待所有线程执行完
        executor.setWaitForTasksToCompleteOnShutdown(true);
//        executor.setThreadNamePrefix("ThreadPoolTaskExecutor-");
        //如果加入到Spring容器，那么就不需要手动调用executor.initialize() 做初始化了，因为在Bean初始化的时候会自动调用这个方法。
        //initialize() 并不是必需的，spring bean 会自动调用 afterPropertiesSet() ，InitializingBean 接口里面只有这个 afterPropertiesSet()
        //而 afterPropertiesSet() 里会调用initialize(), ThreadPoolTaskExecutor 继承了实现 afterPropertiesSet() 的 ExecutorConfigurationSupport
//        executor.initialize();
        return executor;
    }

    @Bean
    public Executor taskExecutorB() {
        // Spring 默认配置是核心线程数大小为1，最大线程容量大小不受限制，队列容量也不受限制。
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数
        executor.setCorePoolSize(CORE_POOL_SIZE);
        // 最大线程数
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        // 队列大小
        executor.setQueueCapacity(QUEUE_CAPACITY);
        // 当最大池已满时，此策略保证不会丢失任务请求，但是可能会影响应用程序整体性能。
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //该方法用来设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住。 // 等待时间 （默认为0，此时立即停止），并没等待xx秒后强制停止
        executor.setAwaitTerminationSeconds(60);
        //用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean，这样这些异步任务的销毁就会先于Redis线程池的销毁。 //等待任务在关机时完成--表明等待所有线程执行完
        executor.setWaitForTasksToCompleteOnShutdown(true);
//        executor.setThreadNamePrefix("ThreadPoolTaskExecutor-");
//        executor.initialize();
        return executor;
    }

    @Bean("canal")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数
        executor.setCorePoolSize(CORE_POOL_SIZE);
        // 最大线程数
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        // 队列大小
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE);
        executor.setThreadNamePrefix("canal-");
        // CallerRunsPolicy 重试添加当前的任务，他会自动重复调用execute()方法
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SpringAsyncExceptionHandler();
    }

    class SpringAsyncExceptionHandler implements AsyncUncaughtExceptionHandler{

        @Override
        public void handleUncaughtException(Throwable ex, Method method, Object... params) {
            log.error("method:{},  Exception message:{},  params:{}",method, ex.toString(),params);
        }
    }
}
