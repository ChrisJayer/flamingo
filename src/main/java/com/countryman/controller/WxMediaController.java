package com.countryman.controller;

import com.alibaba.fastjson.JSON;
import com.countryman.Result;
import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.http.weixin.ApiResult;
import com.foxinmy.weixin4j.model.media.MediaUploadResult;
import com.foxinmy.weixin4j.mp.WeixinProxy;
import com.foxinmy.weixin4j.mp.api.MassApi;
import com.foxinmy.weixin4j.mp.api.MediaApi;
import com.foxinmy.weixin4j.tuple.MpArticle;
import com.foxinmy.weixin4j.tuple.MpNews;
import com.google.common.base.Strings;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * @author countryman
 * @mail chris_jay_9111@sina.com
 * @create 2018-06-19 13:58
 * @description
 **/

@Slf4j
@RestController
@RequestMapping("/wechat/media")
public class WxMediaController {

    @Autowired
    private WeixinProxy weixinProxy;

    @Autowired
    private MediaApi mediaApi;

    @Autowired
    private MassApi massApi;

    @PostMapping(value = "/upload_image"/*, consumes = MULTIPART_FORM_DATA_VALUE*/)
    public Result<String> upload(@RequestParam("file") MultipartFile file){
        if(null == file || file.isEmpty())
            return Result.fail("无效文件");


        log.info("上传文件到微信服务器开始，文件名：{}", file.getOriginalFilename());

        try {
            return Result.succeed(weixinProxy.uploadImage(file.getInputStream(), file.getOriginalFilename()));
        } catch (WeixinException e) {
            log.error("上传失败：{} - {}", e.getErrorCode(), e.getErrorDesc());
            return Result.fail(e.getErrorCode(), e.getErrorDesc());
        } catch (IOException e) {
            return Result.fail(e.getMessage());
        }

    }

    @PostMapping("/upload_thumb")
    public Result<MediaUploadResult> thumb(MultipartFile file, @RequestParam(defaultValue = "FALSE") Boolean isMaterial){
        if(null == file)
            return Result.fail("上传文件无效");

        try {
            return Result.succeed(mediaApi.uploadMedia(isMaterial, file.getInputStream(), file.getName(), true));
        } catch (WeixinException e) {
            log.error("上传失败：{} - {}", e.getErrorCode(), e.getErrorDesc());
            return Result.fail(e.getErrorCode(), e.getErrorDesc());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Result.fail("上传失败：{}", e.getMessage());
        }
    }

    @PostMapping("/upload_news")
    public Result<String> news(@RequestBody List<MpArticle> articles){

        if(null == articles || articles.isEmpty())
            return Result.fail("文章参数不合法");
//        Assert.isNull(articles, "文章参数不合法");
//        Assert.notEmpty(articles, "文章参数不合法");
        log.info("创建图文消息：{}", JSON.toJSONString(articles));
        try {
            return Result.succeed(weixinProxy.uploadMassArticle(articles));
        } catch (WeixinException e) {
            return Result.fail(e.getErrorCode(), e.getErrorDesc());
        }
    }

    @Data
    private static class MassReturnVo {
        private String wxSendJobId;
        private String wxMsgDataId;
    }

    @PostMapping("send_by_openids")
    public Result<MassReturnVo> massSend(@RequestBody List<MpArticle> articles){
        if(null == articles || articles.isEmpty())
            return Result.fail("文章参数不合法");

        try {
            String[] result = massApi.massArticleByOpenIds(articles, false,
                    new String[]{"oUNGu0VqlvfVBjlso367Pbc_aO8o", "oUNGu0dYGkYt9gq3kwQ4UHEdiqn4"});
            MassReturnVo massReturnVo = new MassReturnVo();
            massReturnVo.setWxSendJobId(result[0]);
            massReturnVo.setWxMsgDataId(result[1]);

            return Result.succeed(massReturnVo);
        } catch (WeixinException e) {
            return Result.fail(e.getErrorCode(), e.getErrorDesc());
        }
    }

    @Data
    private static class MpNewsPreviewVo{
        private String openId;

        private String mediaId;
        private MpArticle article;
    }

    @PostMapping("preview_mpnews")
    public Result<ApiResult> preview(@RequestBody MpNewsPreviewVo previewVo){
        if(Strings.isNullOrEmpty(previewVo.getOpenId()))
            return Result.fail("openId不允许为空");

        try {
            return Result.succeed(weixinProxy.previewMassNews(previewVo.getOpenId(), null,
                    new MpNews(previewVo.getMediaId()
                            /*new MpArticle[] {previewVo.getArticle()}*//*articles.toArray(new MpArticle[]{})*/ )));
        } catch (Exception e) {
            log.error("预览异常：{}", e.getMessage());
            return Result.fail(e.getMessage());
        }

    }

//    @PostMapping("/send_news")
//    public Result<> sendNews(String mediaId){
//        if(Strings.isNullOrEmpty(mediaId))
//            return Result.fail("无效mediaId");
//
//    }

    public static void main(String[] args){
        MpArticle article = new MpArticle("MoWLKTGK0cYXMLIbkOsicY4M5GGsAQi-eOLGd0GsFSGaXNIHNPf9Kp4EfjUkYXS0",
                "测试", "<p>测试测试</p>");

    }
}
