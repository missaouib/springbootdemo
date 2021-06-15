package com.example.demo.controler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 如果只需要返回字符串而不是页面，需要使用 @Controller + @ResponseBody 或者 @RestController
 */
@Controller
@ResponseBody
@Slf4j
@RequestMapping("/")
public class IndexController {
    @GetMapping("/")
    public String home() {
        return "Hello World";
    }

    @GetMapping("/health")
    public int health() {
        return 0;
    }
}
