package com.spider.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by 周超 on 2017/02/10.
 */
@Controller
public class HomeComtroller {
    @RequestMapping("/index")
    public String home() {

        return "index";
    }
}
