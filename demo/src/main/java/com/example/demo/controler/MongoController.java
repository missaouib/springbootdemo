package com.example.demo.controler;

import com.example.demo.entity.mongodb.User;
import com.example.demo.mongo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/mongo/")
@Slf4j
public class MongoController {

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private ViewService viewService;

    @Autowired
    private InsertService insertService;

    @Autowired
    private QueryService queryService;

    @Autowired
    private UpdateService updateService;

    @Autowired
    private RemoveService removeService;

    @Autowired
    private AggregateGroupService aggregateGroupService;

    @Autowired
    private IndexService indexService;

    @GetMapping("create")
    public void collection() {
        Object collection = collectionService.createCollection();
        log.info("collection => {}", collection);
        Object collectionFixedSize = collectionService.createCollectionFixedSize();
        log.info("collectionFixedSize => {}", collectionFixedSize);
        Object collectionValidation = collectionService.createCollectionValidation();
        log.info("collectionValidation => {}", collectionValidation);
        Object collectionNames = collectionService.getCollectionNames();
        System.err.println(collectionNames);
        Object dropCollection = collectionService.dropCollection();
        System.err.println(dropCollection);
    }

    @GetMapping("document")
    public void document() {
        User user = insertService.insert();
        System.err.println(user);
        Collection<User> userCollection = insertService.insertMany();
        System.err.println(userCollection);
    }

    @GetMapping("query")
    public void query() {
        queryService.findAll();
        queryService.findById("10");
        queryService.findOne();
        queryService.findByConditionAndSort();
        queryService.countNumber();
        queryService.findByCondition();
        queryService.findByRegex();
    }

    @GetMapping("update")
    public void upadte() {
        System.err.println(updateService.update());
        System.err.println(updateService.updateFirst());
        System.err.println(updateService.updateMany());
    }

    @GetMapping("remove")
    public void remove() {
        removeService.remove();
        removeService.findAndRemove();
        removeService.findAllAndRemove();
    }

    @GetMapping("group")
    public void aggregateGroup() {
        System.err.println(aggregateGroupService.aggregationGroupCount());
    }

    @GetMapping("index")
    public void index() {
        System.err.println(indexService.createAscendingIndex());
        System.err.println(indexService.createTextIndex());
        System.err.println(indexService.getIndexAll());
    }
}
