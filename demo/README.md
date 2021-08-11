## 关闭kafka

KafkaReceiver.java 中注释 `@KafkaListener`

## 关闭rocketmq

1. MQ*figuration.java 中注释 `@Configuration`
2. MqControler.java 中注释 `defaultMQProducer`的使用

## 关闭springboot scheduler

在启动类中注释 `@EnableScheduling`

## 关闭 elastic job

配置文件 `spring.autoconfigure.exclude`

## 关闭多数据源

1. 配置文件注释`second`数据源
2. SecondaryDataSourceConfig.java `@Configuration`
3. BlogServerImpl `@Autowired(required = false)`

## 关闭mongoDB
1. service `@Autowired(required = false)`
2. `@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})`
