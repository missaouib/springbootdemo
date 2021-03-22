## 关闭kafka

KafkaReceiver.java 中注释 `@KafkaListener`

## 关闭rocketmq

1. MQ*figuration.java 中注释 `@Configuration`
2. MqControler.java 中注释 `defaultMQProducer`的使用
3. OrderServiceImpl.java 注释 `producer` 的自动注入

## 关闭springboot scheduler

在启动类中注释 `@EnableScheduling`

## 关闭 elastic job

配置文件 `spring.autoconfigure.exclude`

## 关闭多数据源

1. 配置文件注释`second`数据源
2. SecondaryDataSourceConfig.java `@Configuration`
3. BlogServerImpl `@Autowired(required = false)`
