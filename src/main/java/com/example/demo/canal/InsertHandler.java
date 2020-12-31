package com.example.demo.canal;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class InsertHandler extends AbstractHandler {

    public InsertHandler(){
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
            Map<String, String> map = super.columnsToMap(afterColumnsList);
            String id = map.get("id");
            String jsonStr = JSONObject.toJSONString(map);
            log.info("新增的数据：{}\r\n",jsonStr);
            redisUtil.setDefault("blog:"+id, jsonStr);
        });
    }


}
