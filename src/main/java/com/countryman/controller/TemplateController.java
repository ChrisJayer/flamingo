package com.countryman.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author countryman
 * @mail chris_jay_9111@sina.com
 * @create 2018-06-19 16:54
 * @description
 **/

@Slf4j
@Controller
@RequestMapping("/api")
public class TemplateController {

    @RequestMapping(value = "/file/upload", method = RequestMethod.GET)
    public String fileUpload(){
        return "/files/upload";
    }

    @RequestMapping(value = "/file/upload_thumb", method = RequestMethod.GET)
    public String thumbUpload(){
        return "/files/uploadThumb";
    }
}
