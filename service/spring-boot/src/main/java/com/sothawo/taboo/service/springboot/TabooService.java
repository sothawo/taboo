/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.service.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Spring-Boot Service implementation for the taboo backend service.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@SpringBootApplication
@RequestMapping("/taboo")
public class TabooService {

    @RequestMapping(value = "/hello/{name}", method = RequestMethod.GET)
    @ResponseBody
    public String hello(@PathVariable("name") String name) {
        return "hello " + name;
    }

    public static void main(String[] args) {
        SpringApplication.run(TabooService.class, args);
    }
}
