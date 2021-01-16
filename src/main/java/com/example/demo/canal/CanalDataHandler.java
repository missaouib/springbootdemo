package com.example.demo.canal;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
class CanalDataHandler extends TypeConvertHandler {


    /**
     * 将binlog的记录解析为一个bean对象
     *
     * @param columnList
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T convertToBean(List<CanalEntry.Column> columnList, Class<T> clazz) {
        T bean = null;
        try {
            bean = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = clazz.getDeclaredFields();
            Field.setAccessible(fields, true);
            Map<String, Field> fieldMap = new HashMap<>(fields.length);
            for (Field field : fields) {
                fieldMap.put(field.getName(), field);
            }
            if (fieldMap.containsKey("serialVersionUID")) {
                fieldMap.remove("serialVersionUID");
            }
            for (CanalEntry.Column column : columnList) {
                //将小写——下划线改为驼峰
                String columnName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, column.getName());
                String columnValue = column.getValue();
                if (fieldMap.containsKey(columnName)) {
                    //基础类型转换不了
                    Field field = fieldMap.get(columnName);
                    Class<?> type = field.getType();
                    if(BEAN_FIELD_TYPE.containsKey(type)){
                        switch (BEAN_FIELD_TYPE.get(type)) {
                            case "Integer":
                                field.set(bean, parseToInteger(columnValue));
                                break;
                            case "Long":
                                field.set(bean, parseToLong(columnValue));
                                break;
                            case "Double":
                                field.set(bean, parseToDouble(columnValue));
                                break;
                            case "String":
                                field.set(bean, columnValue);
                                break;
                            case "java.util.Date":
                                field.set(bean, parseToDate(columnValue));
                                break;
                            case "java.sql.Date":
                                field.set(bean, parseToSqlDate(columnValue));
                                break;
                            case "java.sql.Timestamp":
                                field.set(bean, parseToTimestamp(columnValue));
                                break;
                            case "java.sql.Time":
                                field.set(bean, parseToSqlTime(columnValue));
                                break;
                            default:
                                break;
                        }
                    }else{
                        field.set(bean, parseObj(columnValue));
                    }
                }

            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            log.error("[CanalDataHandler]convertToBean，初始化对象出现异常，对象无法被实例化,异常为{}", e);
        }
        return bean;
    }

    /**
     * 其他类型自定义处理
     *
     * @param source
     * @return
     */
    public static Object parseObj(String source){
        return null;
    }


}
