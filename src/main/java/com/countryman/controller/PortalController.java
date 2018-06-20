package com.countryman.controller;

import com.countryman.properties.WeixinMpProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author countryman
 * @mail chris_jay_9111@sina.com
 * @create 2018-06-19 14:48
 * @description
 **/

@Slf4j
@Controller
@RequestMapping("/wechat/portal")
public class PortalController {

    @Autowired
    private WeixinMpProperties weixinMpProperties;

    private static final String generateSign(String timestamp,String nonce,String token) throws NoSuchAlgorithmException {
        String[] arr = { token, nonce, timestamp };
        Arrays.sort(arr);

        StringBuffer sb = new StringBuffer();
        String content = Arrays.stream(arr).collect(Collectors.joining());

//        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(content.toString().getBytes());
            return byteToStr(digest).toLowerCase();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
    }

    private static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    private static String byteToHexStr(byte mByte) {
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A','B', 'C', 'D', 'E', 'F' };
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        String s = new String(tempArr);
        return s;
    }

    @GetMapping
    public void checkToken(String timestamp,
                           String nonce,
                           String echostr,
                           String signature,
                           HttpServletResponse response){
        Assert.notNull(timestamp, "时间戳为空");
        Assert.notNull(nonce, "随机字符串为空");
        Assert.notNull(echostr, "echoStr为空");

        try {
            if(generateSign(timestamp, nonce, weixinMpProperties.getToken()).equalsIgnoreCase(signature)){
                PrintWriter pw = response.getWriter();
                try {
                    pw.write(echostr);
                    pw.flush();
                } finally {
                    pw.close();
                }
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
