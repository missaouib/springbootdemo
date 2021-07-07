package com.example.demo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BeanConvertUtils {

    public static <T> List<T> convert(List fromValues, Class<T> toValueType) {
        List<T> instances = new ArrayList<T>();
        if (fromValues == null || fromValues.isEmpty()) {
            return instances;
        }
        fromValues.stream().forEach(fromValue -> {
            T instance = null;
            try {
                if (fromValue != null) {
                    instance = toValueType.getConstructor().newInstance();
                    BeanUtils.copyProperties(fromValue, instance);
                    instances.add(instance);
                }
            } catch (InstantiationException e) {
                log.error("convert InstantiationException", e);
            } catch (IllegalAccessException e) {
                log.error("convert IllegalAccessException", e);
            } catch (Exception e) {
                log.error("convert exception", e);
            }
        });
        return instances;
    }

    public static <T> T convert(Object fromValue, Class<T> toValueType) {
        T instance = null;
        try {
            if (fromValue != null) {
                instance = toValueType.getConstructor().newInstance();
                BeanUtils.copyProperties(fromValue, instance);
            }
        } catch (InstantiationException e) {
            log.error("convert InstantiationException", e);
        } catch (IllegalAccessException e) {
            log.error("convert IllegalAccessException", e);
        } catch (Exception e) {
            log.error("convert exception", e);
        }
        return instance;
    }

    public static <T> List<T> convert(List fromValues, Class<T> toValueType, String... ignoreProperties) {
        List<T> instances = new ArrayList<T>();
        if (fromValues == null || fromValues.isEmpty()) {
            return instances;
        }
        fromValues.stream().forEach(fromValue -> {
            T instance = null;
            try {
                if (fromValue != null) {
                    instance = toValueType.getConstructor().newInstance();
                    BeanUtils.copyProperties(fromValue, instance, ignoreProperties);
                    instances.add(instance);
                }
            } catch (InstantiationException e) {
                log.error("convert InstantiationException", e);
            } catch (IllegalAccessException e) {
                log.error("convert IllegalAccessException", e);
            } catch (Exception e) {
                log.error("convert exception", e);
            }
        });
        return instances;
    }

    public static <T> T convert(Object fromValue, Class<T> toValueType, String... ignoreProperties) {
        T instance = null;
        try {
            instance = toValueType.getConstructor().newInstance();
            if (fromValue != null) {
                BeanUtils.copyProperties(fromValue, instance, ignoreProperties);
            }
        } catch (InstantiationException e) {
            log.error("convert InstantiationException", e);
        } catch (IllegalAccessException e) {
            log.error("convert IllegalAccessException", e);
        } catch (Exception e) {
            log.error("convert exception", e);
        }
        return instance;
    }

    public static <T> List<T> cglibConvert(List fromValues, Class<T> toValueType) {
        List<T> instances = new ArrayList<T>();
        if (fromValues == null || fromValues.isEmpty()) {
            return instances;
        }
        BeanCopier bc = BeanCopier.create(fromValues.get(0).getClass(), toValueType, false);
        fromValues.stream().forEach(fromValue -> {
            T instance = null;
            try {
                if (fromValue != null) {
                    instance = toValueType.getConstructor().newInstance();
                    bc.copy(fromValue, instance, null);
                    instances.add(instance);
                }
            } catch (InstantiationException e) {
                log.error("convert InstantiationException", e);
            } catch (IllegalAccessException e) {
                log.error("convert IllegalAccessException", e);
            } catch (InvocationTargetException e) {
                log.error("convert InvocationTargetException", e);
            } catch (NoSuchMethodException e) {
                log.error("convert IllegalAccessException", e);
            }
        });
        return instances;
    }

    public static <T> T cglibConvert(Object fromValue, Class<T> toValueType) {
        T instance = null;
        try {
            BeanCopier bc = BeanCopier.create(fromValue.getClass(), toValueType, false);
            instance = toValueType.getConstructor().newInstance();
            if (fromValue != null) {
                bc.copy(fromValue, instance, null);
            }
        } catch (InstantiationException e) {
            log.error("convert InstantiationException", e);
        } catch (IllegalAccessException e) {
            log.error("convert IllegalAccessException", e);
        } catch (InvocationTargetException e) {
            log.error("convert InvocationTargetException", e);
        } catch (NoSuchMethodException e) {
            log.error("convert NoSuchMethodException", e);
        }
        return instance;
    }

    /**
     * fastJson 转换Bean在效率上相对于springframework.beans.BeanUtils 有所不足
     * 效率：springframework.beans.BeanUtils > fastJson > apache.commons.beanutils.BeanUtils
     */
    public static <T> List<T> fastJsonConvert(List fromValue, Class<T> toValueType) {
        // WriteMapNullValue——–是否输出值为null的字段,默认为false
        // WriteNullListAsEmpty—–List字段如果为null,输出为[],而非null
        String tmp = JSON.toJSONString(fromValue, SerializerFeature.WriteNullListAsEmpty);
        List<T> result = JSON.parseArray(tmp, toValueType);
        return result;
    }

    public static <T> T fastJsonConvert(Object fromValue, Class<T> toValueType) {
        // WriteMapNullValue——–是否输出值为null的字段,默认为false
        // WriteNullListAsEmpty—–List字段如果为null,输出为[],而非null
        String tmp = JSON.toJSONString(fromValue, SerializerFeature.WriteNullListAsEmpty);
        T result = JSON.parseObject(tmp, toValueType, SerializerFeature.WRITE_MAP_NULL_FEATURES);
        return result;
    }

}