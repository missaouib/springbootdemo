package com.example.demo.controler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.*;

/**
 * V get(); 会堵塞当前的线程 抛出的是检查异常，必须用户throw或者try/catch处理
 * V get(long timeout,Timeout unit); 会堵塞当前的线程,超时时间
 * T getNow(T defaultValue); 当有了返回结果时会返回结果,如果异步线程抛了异常会返回自己设置的默认值
 * T join(); join返回结果，抛出未检查异常
 */

@RequestMapping("/futuer")
@RestController
@Slf4j
public class FutuerControler {
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 可选传入线程池
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping("CompletableFuture")
    public void completableFuture() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        //future调用complete(T t)会立即执行。但是complete(T t)只能调用一次，后续的重复调用会失效。
        //completeExceptionally(Throwable ex)则抛出一个异常，而不是一个成功的结果。
        completableFuture.complete("done");
        String result = completableFuture.get();
        log.error(result);

        //runAsync() 无返回值
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.error("I'll run in a separate thread than the main thread.");
        });

        //supplyAsync() 运行一个异步任务并且返回结果
        CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException ignored) {
            }
            log.error("run end ...");
            return System.currentTimeMillis();
        }, threadPoolTaskExecutor);
        Long aLong = future.get();
        log.error(aLong.toString());
    }


    /**
     * 回调方法：
     * <p>
     * 当CompletableFuture的计算结果完成，或者抛出异常的时候，可以执行特定的Action。主要是下面的方法：
     * public CompletableFuture<T> whenComplete(BiConsumer<? super T,? super Throwable> action)
     * public CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T,? super Throwable> action)
     * public CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T,? super Throwable> action, Executor executor)
     * public CompletableFuture<T> exceptionally(Function<Throwable,? extends T> fn) 异常处理，当运行出现异常时,调用该方法可进行一些补偿操作,如设置默认值.
     * <p>
     * whenComplete：是执行当前任务的线程执行继续执行 whenComplete 的任务。
     * whenCompleteAsync：是执行把 whenCompleteAsync 这个任务继续提交给线程池来进行执行。
     */
    @GetMapping("callback")
    public void callback() {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
            }
            if (new Random().nextInt() % 2 >= 0) {
                log.error("exception...");
                int i = 12 / 0;
            }
            log.error("run end ...");
        });

        //下面两个是一样的，一个是lambda表达式，一个是匿名内部类
        //当CompletableFuture的计算结果完成，或者抛出异常的时候，都可以进入whenComplete方法执行
        future.whenCompleteAsync((u, t) -> log.error("执行完成"));

//        future.whenCompleteAsync(new BiConsumer<>() {
//            @Override
//            public void accept(Void unused, Throwable throwable) {
//                log.error("执行完成 ");
//            }
//        });


        future.exceptionally(t -> {
            log.error("执行失败 " + t.getMessage());
            return null;
        });

//        future.exceptionally(new Function<>() {
//            @Override
//            public Void apply(Throwable throwable) {
//                log.error("执行失败 " + throwable.getMessage());
//                return null;
//            }
//        });


        CompletableFuture.supplyAsync(() -> "执行结果:" + (10 / 0))
                .thenApply(s -> "apply result:" + s)
                .whenComplete((s, e) -> {
                    if (s != null) {
                        System.out.println(s);//未执行
                    }
                    if (e == null) {
                        System.out.println(s);//未执行
                    } else {
                        log.error(e.getMessage());//java.lang.ArithmeticException: / by zero
                    }
                })
                .exceptionally(e -> {
                    System.out.println("ex" + e.getMessage()); //ex:java.lang.ArithmeticException: / by zero
                    return "futureA result: 100";
                });
    }

    /**
     * 　功能:当前任务正常完成以后执行，当前任务的执行的结果会作为下一任务的输入参数,有返回值
     * 　场景:多个任务串联执行,下一个任务的执行依赖上一个任务的结果,每个任务都有输入和输出
     */
    @GetMapping("thenApply")
    public void thenApply() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello")
                .thenApply(s -> s + " world")
                .thenApply(String::toUpperCase);
        String s1 = future.join();
        System.out.println(s1);
        try {
            String s = future.get();
            log.error(s);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    /**
     * public CompletionStage<Void> thenAccept(Consumer<? super T> action);
     * public CompletionStage<Void> thenAcceptAsync(Consumer<? super T> action);
     * public CompletionStage<Void> thenAcceptAsync(Consumer<? super T> action,Executor executor);
     * <p>
     * 功能:当前任务正常完成以后执行,当前任务的执行结果可以作为下一任务的输入参数,无返回值.
     * 场景:执行任务A,同时异步执行任务B,待任务B正常返回之后,用B的返回值执行任务C,任务C无返回值
     */
    @GetMapping("/thenAccept")
    public void thenAccept() {
        CompletableFuture.supplyAsync(() -> "A")
                .thenAccept(e -> {
                    log.error("这是B");
                    log.error(e + " B");
                });
    }


    /**
     * 功能:对不关心上一步的计算结果，执行下一个操作
     * <p>
     * 　　场景:执行任务A,任务A执行完以后,执行任务B,任务B不接受任务A的返回值(不管A有没有返回值),也无返回值
     */
    @GetMapping("thenRun")
    public void thenRun() {
        CompletableFuture.supplyAsync(() -> "hello ").thenRun(() -> log.error("world"));
    }

    /**
     * 功能:结合两个CompletionStage的结果，进行转化后返回
     * 场景:需要根据商品id查询商品的当前价格,分两步,查询商品的原始价格和折扣,这两个查询相互独立,当都查出来的时候用原始价格乘折扣,算出当前价格.
     * thenCombine(..)是结合两个任务的返回值进行转化后再返回,那如果不需要返回呢,
     * 那就需要thenAcceptBoth(..),同理,如果连两个任务的返回值也不关心呢,那就需要runAfterBoth了,
     */
    @GetMapping("thenCombine")
    public void thenCombine() {
        CompletableFuture<Double> futurePrice = CompletableFuture.supplyAsync(() -> 100d);
        CompletableFuture<Double> futureDiscount = CompletableFuture.supplyAsync(() -> 0.8);
        CompletableFuture<Double> futureResult = futurePrice.thenCombine(futureDiscount, (price, discount) -> price * discount);
        log.error("最终价格为:" + futureResult.join());
    }


    /**
     * public <U> CompletableFuture<U>     thenCompose(Function<? super T,? extends CompletionStage<U>> fn)
     * public <U> CompletableFuture<U>     thenComposeAsync(Function<? super T,? extends CompletionStage<U>> fn)
     * public <U> CompletableFuture<U>     thenComposeAsync(Function<? super T,? extends CompletionStage<U>> fn, Executor executor)
     * <p>
     * 功能:这个方法接收的输入是当前的CompletableFuture的计算值，返回结果将是一个新的CompletableFuture
     * <p>
     * 　　这个方法和thenApply非常像,都是接受上一个任务的结果作为入参,执行自己的操作,然后返回.那具体有什么区别呢?
     * <p>
     * 　　thenApply():它的功能相当于将CompletableFuture<T>转换成CompletableFuture<U>,改变的是同一个CompletableFuture中的泛型类型
     * <p>
     * 　　thenCompose():用来连接两个CompletableFuture，返回值是一个新的CompletableFuture
     */
    @GetMapping("thenCompose")
    public void thenCompose() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello")
                .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " world"));
        log.error(future.join());
    }


    /**
     * applyToEither(..)  acceptEither(..)  runAfterEither(..)
     * 功能:执行两个CompletionStage的结果,那个先执行完了,就是用哪个的返回值进行下一步操作
     * 场景:假设查询商品a,有两种方式,A和B,但是A和B的执行速度不一样,我们希望哪个先返回就用那个的返回值.
     */
    @GetMapping("applyToEither")
    public void applyToEither() {
        CompletableFuture<String> futureA = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "通过方式A获取商品a";
        });
        CompletableFuture<String> futureB = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "通过方式B获取商品a";
        });
        CompletableFuture<String> future = futureB.applyToEither(futureA, p -> "结果：" + p);
        log.error(future.join());
    }


    /**
     * 功能:当CompletableFuture的计算结果完成，或者抛出异常的时候，都可以进入whenComplete方法执行
     */
    @GetMapping("/whenComplete")
    public void whenComplete() {
        //先调用whenComplete再调用exceptionally
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "执行结果：" + (1 / 0))
                .thenApply(s -> "apply result:" + s)
                .whenComplete((s, e) -> {
                    if (s != null) {
                        System.out.println(s);//未执行
                    }
                    if (e == null) {
                        System.out.println(s);//未执行
                    } else {
                        System.out.println(e.getMessage());//java.lang.ArithmeticException: / by zero
                    }
                })
                .exceptionally(s -> {
                    System.out.println("ex：" + s.getMessage()); //ex:java.lang.ArithmeticException: / by zero
                    return "futureA result: 100";
                });
        log.error(future.join());
        System.out.println("______________________________________________");

        //先调用exceptionally,再调用whenComplete
        //　代码先执行了exceptionally后执行whenComplete,可以发现
        // ,由于在exceptionally中对异常进行了处理,并返回了默认值,
        // whenComplete中接收到的结果是一个正常的结果,被exceptionally美化过的结果,这一点需要留意一下.
        CompletableFuture<String> futureA = CompletableFuture.
                supplyAsync(() -> "执行结果:" + (100 / 0))
                .thenApply(s -> "apply result:" + s)
                .exceptionally(e -> {
                    System.out.println("ex:" + e.getMessage()); //ex:java.lang.ArithmeticException: / by zero
                    return "futureA result: 100";
                })
                .whenComplete((s, e) -> {
                    if (e == null) {
                        System.out.println(s);//futureA result: 100
                    } else {
                        System.out.println(e.getMessage());//未执行
                    }
                });
        log.error(futureA.join());

    }

    /**
     * 功能:当CompletableFuture的计算结果完成，或者抛出异常的时候，可以通过handle方法对结果进行处理
     */
    @GetMapping("/handle")
    public void handle(){
        //先执行exceptionally，后执行handle
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "执行结果：" + (10 / 0))
                .thenApply(s -> "apply result:" + s)
                .exceptionally(e -> {
                    System.out.println("ex:" + e.getMessage()); //java.lang.ArithmeticException: / by zero
                    return "futureA result: 100";
                })
                .handle((s, e) -> {
                    if(e == null) {
                        System.out.println(s);//futureA result: 100
                    } else{
                        System.out.println(e.getMessage());//未执行
                    }
                    return "handle result:" + (s == null ? "500" : s);
                });
        log.error(future.join()); //handle result:futureA result: 100

        //先调用handle再调用exceptionally
        //以看到先执行handle,打印了异常信息,并对接过设置了默认值500,exceptionally并没有执行,因为它得到的是handle返回给它的值
        //,由此我们大概推测handle和whenComplete的区别
        //
        //　　　1.都是对结果进行处理,handle有返回值,whenComplete没有返回值
        //
        //　　　2.由于1的存在,使得handle多了一个特性,可在handle里实现exceptionally的功能
        CompletableFuture<String> futureA = CompletableFuture.
                supplyAsync(() -> "执行结果:" + (100 / 0))
                .thenApply(s -> "apply result:" + s)
                .handle((s, e) -> {
                    if (e == null) {
                        System.out.println(s);//未执行
                    } else {
                        System.out.println(e.getMessage());//java.lang.ArithmeticException: / by zero
                    }
                    return "handle result:" + (s == null ? "500" : s);
                })
                .exceptionally(e -> {
                    System.out.println("ex:" + e.getMessage()); //未执行
                    return "futureA result: 100";
                });
        System.out.println(futureA.join());//handle result:500
    }

    /**
     * allOf:当所有的CompletableFuture都执行完后执行计算
     *
     * 　　anyOf:最快的那个CompletableFuture执行完之后执行计算
     */
    @GetMapping("/allOf")
    public void allOf(){
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        long start = System.currentTimeMillis();
        CompletableFuture<String> futureA = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000 + RandomUtils.nextInt(0,1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "商品详情";
        },executorService);

        CompletableFuture<String> futureB = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000 + RandomUtils.nextInt(0,1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "卖家信息";
        },executorService);

        CompletableFuture<String> futureC = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000 + RandomUtils.nextInt(0,1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "库存信息";
        },executorService);

        CompletableFuture<String> futureD = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000 + RandomUtils.nextInt(0,1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "订单信息";
        },executorService);

        CompletableFuture<Void> allFuture = CompletableFuture.allOf(futureA, futureB, futureC, futureD);
        allFuture.join();

        System.out.println(futureA.join() + futureB.join() + futureC.join() + futureD.join());
        System.out.println("总耗时:" + (System.currentTimeMillis() - start));
    }

}
