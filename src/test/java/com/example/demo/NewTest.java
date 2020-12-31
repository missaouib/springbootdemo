package com.example.demo;

import com.example.demo.util.PageUtils;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NewTest {
    public static void main(String[] args) {
        ArrayList<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5, 6,7,8,9,10);
        PageUtils.Page<Integer> integerPage = PageUtils.buildByPageNum(list, 1, 3);
        list.stream().filter(integer -> {
            boolean b = integer > 3;
            return b;
        }).forEach(System.out::println);

        List<User> userList = Lists.newArrayList(
                new User().setId("A").setName("张三"),
                new User().setId("B").setName("李四"),
                new User().setId("C").setName("王五")
        );
        Map<String, String> map1 = userList.stream().collect(Collectors.toMap(User::getId, User::getName));
        Map<String, User> map2 = userList.stream().collect(Collectors.toMap(User::getId, Function.identity()));
        Map<String, User> map3 = userList.stream().collect(Collectors.toMap(User::getId, t -> t));
        Map<String, String> map4 = userList.stream().collect(Collectors.toMap(User::getId,User::getName,(v1,v2) -> v1 + v2));


        Optional<String> strOptional = Optional.of("jay@huaxiao");
        strOptional.ifPresentOrElse(System.out::println, () -> System.out.println("Null"));

        ArrayList<Integer> integers = Lists.newArrayList(1, 2, 3);
        addList(integers);
        System.out.println(integers);

        Stream<String> stream = Stream.of("hello", "felord.cn");
        List<String> list1 = stream.peek(System.out::println).collect(Collectors.toList());

    }

    public static void addList(List<Integer> list){
        list.add(111);
    }
}

@Data
@Accessors(chain = true)//使用chain属性，setter方法返回当前对象
class User {
    private String id;
    private String name;

}
