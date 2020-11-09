package com.example.demo;

import lombok.extern.slf4j.Slf4j;

public class NewTest {
    public static void main(String[] args) {
        String res = new String("eyJzc29JZCI6IjU2Nzg3NjcxMiIsImRldmljZUlkIjoiMzE4MjEzIiwibWFudWZhY3R1cmVyQ29kZSI6IjI5MzU5OTA5ODgyOCIsInByb2R1Y3RJZCI6IjE4MjgyMDQ1MTIzOTMxODUyOCIsImF0dGFjaG1lbnQiOm51bGx9");

        System.out.println(res);
        try {
            throw new RuntimeException();
        }catch (Exception e){
            System.out.println(e);
        }

        System.out.println("after");
    }
}
