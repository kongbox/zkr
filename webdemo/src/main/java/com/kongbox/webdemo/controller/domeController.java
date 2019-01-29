package com.kongbox.webdemo.controller;

import com.kongbox.webdemo.entry.Greeting;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Title:
 * Description:
 *
 * @author kdc
 * @version 1.0
 * @date 2019/1/28.14:31 首次创建
 * @date 2019/1/28.14:31 最后修改
 * @copyright 中科软科技股份有限公司
 */
@RestController
@RequestMapping("/")
public class domeController {


    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();


    @RequestMapping("")
    public String defaule(){
        return "index";
    }

    @RequestMapping("index")
    public Greeting index(@RequestParam(value="name", defaultValue="World") String name){
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }


}
