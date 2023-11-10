package com.deeeelete.system.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


import cn.hutool.core.util.IdUtil;
import com.deeeelete.utils.Base64;
import com.deeeelete.utils.JsonResult;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.code.kaptcha.Producer;

import javax.imageio.ImageIO;


/**
 * 验证码操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/common")
public class CaptchaController
{
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Resource
    private RedisTemplate redisTemplate;


    /**
     * 生成验证码
     */
    @PostMapping("/captchaImage")
    public JsonResult getCode() throws IOException
    {
        JsonResult jsonResult = new JsonResult();
        jsonResult.buildTrue();

        // 保存验证码信息
        String uuid = IdUtil.simpleUUID();
        String verifyKey = "captcha_codes:" + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;

        // 生成验证码
        String captchaType = "math";
        if ("math".equals(captchaType))
        {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        }
        else if ("char".equals(captchaType))
        {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        redisTemplate.opsForValue().set(verifyKey, code, 3, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try
        {
            ImageIO.write(image, "jpg", os);
        }
        catch (IOException e)
        {
            return jsonResult.buildFalse(e.getMessage());
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("uuid", uuid);
        map.put("img", "data:image/gif;base64,"+Base64.encode(os.toByteArray()));
        jsonResult.setData(map);
        jsonResult.setMessage(uuid);
        return jsonResult;
    }




}