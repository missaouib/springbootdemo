package com.example.demo;

import com.google.common.collect.Lists;

import java.util.ArrayList;

public class NewTest {
    public static void main(String[] args) {
        ArrayList<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5, 6);
        list.stream().filter(integer -> {
            boolean b = integer > 3;
            return b;
        }).forEach(System.out::println);
    }
}
