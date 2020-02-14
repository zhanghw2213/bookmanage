package com.bookmanage.bookmanage.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book")
public class BookController {

    @RequestMapping(value = "/submit", method = RequestMethod.PUT)
    public JSONObject getAccount(String tittle, String authorName, String Source, String fileList) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result",true);
        return jsonObject;
    }
}
