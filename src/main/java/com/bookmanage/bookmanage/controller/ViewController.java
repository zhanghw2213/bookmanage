package com.bookmanage.bookmanage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class ViewController {

    @RequestMapping(value = "/filemanager", method = RequestMethod.GET)
    public String fileManager() {
        return "/filemanager";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String upload() {
        return "/upload";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/index";
    }

    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public String signin() {
        return "/signin";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String admin() {
        return "/admin";
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String test() {
        return "<b>test</b>";
    }
}
