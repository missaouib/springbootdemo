package com.example.demo.canal;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.example.demo.entity.Blog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class InsertHandler extends AbstractHandler {

    public InsertHandler() {
        this.eventType = EventType.INSERT;
    }

    @Autowired
    public void setNextHandler(DeleteHandler deleteHandler) {
        this.nextHandler = deleteHandler;
    }

    @Override
    public void handleRowChange(RowChange rowChange) {
        rowChange.getRowDatasList().forEach(rowData -> {
            //每一行的每列数据  字段名->值
            List<Column> afterColumnsList = rowData.getAfterColumnsList();
            Blog blog = super.columnsToBean(afterColumnsList, Blog.class);
            String id = blog.getId().toString();
            log.info("新增的数据：{}\r\n", blog.toString());
            redisService.set("blog:" + id, blog);
        });
    }


}
