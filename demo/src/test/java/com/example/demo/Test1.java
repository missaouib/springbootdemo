package com.example.demo;

import com.example.demo.entity.Book;
import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class Test1 {
    public static void main(String[] args) throws NoSuchFieldException {
//        method1();
//
//        method2();
//
//        System.out.println("----------------------");
//
//        method3();

        AA aa = new AA();
        aa.getList().stream().filter(e -> e.getId() > 2).forEach(System.out::println);


    }

    private static void method3() {
        //String 不一样，hashcode一样
        String s1 = "ABCDEa123abc";
        String s2 = "ABCDFB123abc";
        System.out.println(s1.hashCode());  // 165374702
        System.out.println(s2.hashCode()); //  165374702
        System.out.println(s1 == s2);
        System.out.println(s1.equals(s2));
    }

    private static void method2() throws NoSuchFieldException {
        Field[] declaredFields = AA.class.getDeclaredFields();
        for (Field field : declaredFields) {
            System.out.println(field);
        }

        //获取泛型具体参数
        System.out.println("----------------------");
        Field listField = AA.class.getDeclaredField("map");
        System.out.println(listField);
        Type genericType = listField.getGenericType();
        System.out.println(genericType);
        ParameterizedType parameterizedType = (ParameterizedType) genericType;
        System.out.println(parameterizedType);
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        for (Type actualTypeArgument : actualTypeArguments) {
            System.out.println(actualTypeArgument);
        }
    }

    private static void method1() {
        Integer page = null;
        System.out.println(page);

        //三元运算符优先级最低
        page = page == null || page < 1 ? 0 : (page - 1);
        System.out.println(page);
    }
}


@Data
class AA {
    private List<Book> list;
    private Map<String, Integer> map;
}