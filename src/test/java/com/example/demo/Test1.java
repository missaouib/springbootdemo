package com.example.demo;

public class Test1 {
    public static void main(String[] args) {
        Integer page = null;
        page = page == null || page < 1 ? 0 : (page - 1);
        System.out.println(page);


        System.out.println(page == null);
    }
}
