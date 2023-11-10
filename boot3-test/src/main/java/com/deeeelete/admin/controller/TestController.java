package com.deeeelete.admin.controller;


import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * TestController
 */
@RestController
public class TestController {



    @GetMapping("/setRedis")
    public String setRedis(){
        return "success";
    }


}
