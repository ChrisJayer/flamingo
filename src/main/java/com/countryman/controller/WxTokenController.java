package com.countryman.controller;

import com.countryman.Result;
import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.mp.WeixinProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author countryman
 * @mail chris_jay_9111@sina.com
 * @create 2018-06-19 13:54
 * @description
 **/

@Slf4j
@RestController
@RequestMapping("/wechat")
public class WxTokenController {

    @Autowired
    private WeixinProxy weixinProxy;

    @GetMapping("/token")
    public Result<String> glance(){
        try {
            return Result.succeed(weixinProxy.getTokenManager().getAccessToken());
        } catch (WeixinException e) {
            return Result.fail(e.getErrorCode(), e.getErrorDesc());
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }
}
