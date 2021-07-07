package com.example.demo.canal;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.example.demo.util.RedisUtil;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @description: 使用责任链模式  链顺序：增->删->改
 * @version: 1.0
 */
@Slf4j
public abstract class AbstractHandler {

    /**
     * 下一个执行者
     */
    protected AbstractHandler nextHandler;

    /**
     * 事件类型 UPDATE、DELETE、INSERT
     */
    protected EventType eventType;

    @Autowired
    protected RedisUtil redisUtil;


    /**
     * 传递处理的事件
     */
    public void handleMessage(Entry entry) {
        if (this.eventType == entry.getHeader().getEventType()) {
            //发生写入操作的库名
            String database = entry.getHeader().getSchemaName();
            //发生写入操作的表名
            String table = entry.getHeader().getTableName();
            log.info("监听到数据库：{}，表：{} 的 {} 事件", database, table, eventType.toString());
            //如果 rowChange 不为空,则执行 handleRowChange()
            Optional.ofNullable(this.getRowChange(entry))
                    .ifPresent(this::handleRowChange);
        } else {
            if (nextHandler != null) {
                nextHandler.handleMessage(entry);
            }
        }
    }

    /**
     * 处理数据库 UPDATE、DELETE、INSERT 的数据
     */
    public abstract void handleRowChange(RowChange rowChange);

    /**
     * 获得数据库 UPDATE、DELETE、INSERT 的数据
     */
    private RowChange getRowChange(Entry entry) {
        RowChange rowChange = null;
        try {
            rowChange = RowChange.parseFrom(entry.getStoreValue());
        } catch (InvalidProtocolBufferException e) {
            log.error("根据CanalEntry获取RowChange异常:", e);
        }
        return rowChange;
    }

    /**
     * columns 转 map
     */
    protected Map<String, String> columnsToMap(List<Column> columnsList) {
        return columnsList.stream().collect(Collectors.toMap(Column::getName, Column::getValue));
    }

    /**
     * columns 转 bean
     */
    protected <T> T columnsToBean(List<Column> columnsList, Class<T> clazz) {
        return CanalDataHandler.convertToBean(columnsList, clazz);
    }



}
