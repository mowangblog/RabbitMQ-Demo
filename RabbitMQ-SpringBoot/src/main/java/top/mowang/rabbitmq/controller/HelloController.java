package top.mowang.rabbitmq.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RabbitMQ-Demo
 *
 * @author : Xuan Li
 * @website : https://mowangblog.top
 * @date : 2021/10/28 11:16
 **/
@RestController
public class HelloController {
    @GetMapping("/")
    public String hello(){
        return "hello";
    }
}
