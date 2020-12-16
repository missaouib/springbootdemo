package com.example.demo.elasticjob;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.springframework.stereotype.Component;

/**
 * 定义job
 */
@Component
@Slf4j
public class SimpleJobDemo implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("Thread ID: {}, 作业分片总数: {}, 当前分片项: {}, 当前参数: {}, 作业名称:  {}, 作业自定义参数: {}",
                Thread.currentThread().getId(),
                shardingContext.getShardingTotalCount(),
                shardingContext.getShardingItem(),
                shardingContext.getShardingParameter(),
                shardingContext.getJobName(),
                shardingContext.getJobParameter()
        );
        switch (shardingContext.getShardingItem()) {
            case 0:
                System.err.println("0");
                break;
            case 1:
                System.err.println("1");
                // do something by sharding item 1
                break;
            case 2:
                System.err.println("2");
                // do something by sharding item 2
                break;
            // case n: ...
            default:
                break;
        }
    }
}
