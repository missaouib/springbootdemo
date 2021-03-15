package com.example.demo;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @BenchmarkMode 用来配置 Mode 选项，可用于类或者方法上，这个注解的 value 是一个数组，可以把几种 Mode 集合在一起执行，如：@BenchmarkMode({Mode.SampleTime, Mode.AverageTime})，还可以设置为 Mode.All，即全部执行一遍。
 * Throughput：整体吞吐量，每秒执行了多少次调用，单位为 ops/time
 * AverageTime：用的平均时间，每次操作的平均时间，单位为 time/op
 * SampleTime：随机取样，最后输出取样结果的分布
 * SingleShotTime：只运行一次，往往同时把 Warmup 次数设为 0，用于测试冷启动时的性能
 * All：上面的所有模式都执行一次
 * @State 通过 State 可以指定一个对象的作用范围，JMH 根据 scope 来进行实例化和共享操作。@State 可以被继承使用，如果父类定义了该注解，子类则无需定义。由于 JMH 允许多线程同时执行测试，不同的选项含义如下：
 * Scope.Benchmark：所有测试线程共享一个实例，测试有状态实例在多线程共享下的性能
 * Scope.Group：同一个线程在同一个 group 里共享实例
 * Scope.Thread：默认的 State，每个测试线程分配一个实例
 * @OutputTimeUnit 为统计结果的时间单位，可用于类或者方法注解
 * @Warmup 预热所需要配置的一些基本测试参数，可用于类或者方法上。一般前几次进行程序测试的时候都会比较慢，所以要让程序进行几轮预热，保证测试的准确性。参数如下所示：
 * iterations：预热的次数
 * time：每次预热的时间
 * timeUnit：时间的单位，默认秒
 * batchSize：批处理大小，每次操作调用几次方法
 * @Measurement 实际调用方法所需要配置的一些基本测试参数，可用于类或者方法上，参数和 @Warmup 相同。
 * @Threads 每个进程中的测试线程，可用于类或者方法上。
 * @Fork 进行 fork 的次数，可用于类或者方法上。如果 fork 数是 2 的话，则 JMH 会 fork 出两个进程来进行测试
 * @Param 指定某项参数的多种情况，特别适合用来测试一个函数在不同的参数输入的情况下的性能，只能作用在字段上，使用该注解必须定义 @State 注解。
 * <p>
 * 使用@CompilerControl(Mode)可以告诉编译器应该怎么编译特定的方法。有以下几种Mode:
 * EXCLUDE 禁止编译，解释执行
 * INLINE 强制内联
 * DONT_INLINE 强制不内联
 * COMPILE_ONLY 编译
 * PRINT 打印方法的一些信息
 * BREAK Insert the breakpoint into the generated compiled code.
 */
@BenchmarkMode(Mode.AverageTime)// 测试完成时间
@State(Scope.Thread)// 每个测试线程一个实例
@Fork(1)// fork 1 个线程,如果 fork 数是 2 的话，则 JMH 会 fork 出两个进程来进行测试。
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)// 预热 2 轮，每次 1s
@Measurement(iterations = 5, time = 3, timeUnit = TimeUnit.SECONDS) // 测试 5 轮，每次 3s
public class BenchmarkTest {

    @Param(value = {"1000", "10000", "100000"})
    private int length;

    @Benchmark
    public void stringAdd(Blackhole blackhole) {
        String string = "";
        for (int i = 0; i < length; i++) {
            string = string + i;
        }
        blackhole.consume(string);
    }

    @Benchmark
    public void stringBuilderAppend(Blackhole blackhole) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(i);
        }
        blackhole.consume(stringBuilder.toString());
    }

    public static void main(String[] args) throws RunnerException {
        // 启动基准测试
        Options opt = new OptionsBuilder()
                .include(BenchmarkTest.class.getSimpleName())// 要导入的测试类
                .result("result.json")
                .resultFormat(ResultFormatType.JSON)
                .build();
        new Runner(opt).run();// 执行测试
    }
}
