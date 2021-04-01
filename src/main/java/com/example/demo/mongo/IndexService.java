package com.example.demo.mongo;

import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class IndexService {

    /**
     * 设置集合名称
     */
    private static final String COLLECTION_NAME = "users";

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 创建升序索引
     *
     * @return 索引信息
     */
    public String createAscendingIndex() {
        // 设置字段名称
        String field = "name";
        // 创建索引
        return mongoTemplate.getCollection(COLLECTION_NAME).createIndex(Indexes.ascending(field));
    }

    /**
     * 创建降序索引
     *
     * @return 索引信息
     */
    public String createDescendingIndex() {
        // 设置字段名称
        String field = "name";
        // 创建索引
        return mongoTemplate.getCollection(COLLECTION_NAME).createIndex(Indexes.descending(field));
    }

    /**
     * 创建升序复合索引
     *
     * @return 索引信息
     */
    public String createCompositeIndex() {
        // 设置字段名称
        String field1 = "name";
        String field2 = "age";
        // 创建索引
        return mongoTemplate.getCollection(COLLECTION_NAME).createIndex(Indexes.ascending(field1, field2));
    }

    /**
     * 创建文字索引
     *
     * @return 索引信息
     */
    public String createTextIndex() {
        // 设置字段名称
        String field = "name";
        // 创建索引
        return mongoTemplate.getCollection(COLLECTION_NAME).createIndex(Indexes.text(field));
    }

    /**
     * 创建哈希索引
     *
     * @return 索引信息
     */
    public String createHashIndex() {
        // 设置字段名称
        String field = "name";
        // 创建索引
        return mongoTemplate.getCollection(COLLECTION_NAME).createIndex(Indexes.hashed(field));
    }

    /**
     * 创建升序唯一索引
     *
     * @return 索引信息
     */
    public String createUniqueIndex() {
        // 设置字段名称
        String indexName = "name";
        // 配置索引选项
        IndexOptions options = new IndexOptions();
        // 设置为唯一索引
        options.unique(true);
        // 创建索引
        return mongoTemplate.getCollection(COLLECTION_NAME).createIndex(Indexes.ascending(indexName), options);
    }

    /**
     * 创建局部索引
     *
     * @return 索引信息
     */
    public String createPartialIndex() {
        // 设置字段名称
        String field = "name";
        // 配置索引选项
        IndexOptions options = new IndexOptions();
        // 设置过滤条件
        options.partialFilterExpression(Filters.exists("name", true));
        // 创建索引
        return mongoTemplate.getCollection(COLLECTION_NAME).createIndex(Indexes.ascending(field), options);
    }


    /**
     * 获取当前【集合】对应的【所有索引】的【名称列表】
     *
     * @return 当前【集合】所有【索引名称列表】
     */
    public List<Document> getIndexAll() {
        // 获取集合中所有列表
        ListIndexesIterable<Document> indexList = mongoTemplate.getCollection(COLLECTION_NAME).listIndexes();
        // 创建字符串集合
        List<Document> list = new ArrayList<>();
        // 获取集合中全部索引信息
        for (Document document : indexList) {
            log.info("索引列表：{}", document);
            list.add(document);
        }
        return list;
    }


    /**
     * 根据索引名称移除索引
     */
    public void removeIndex() {
        // 设置索引名称
        String indexName = "name_1";
        // 删除集合中某个索引
        mongoTemplate.getCollection(COLLECTION_NAME).dropIndex(indexName);
    }

    /**
     * 移除全部索引
     */
    public void removeIndexAll() {
        // 删除集合中全部索引
        mongoTemplate.getCollection(COLLECTION_NAME).dropIndexes();
    }

}
