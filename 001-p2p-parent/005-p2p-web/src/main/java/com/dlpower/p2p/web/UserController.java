package com.dlpower.p2p.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.dlpower.p2p.cons.Constants;
import com.dlpower.p2p.model.user.User;
import com.dlpower.p2p.service.UserService;
import com.dlpower.p2p.utils.HttpClientUtils;
import com.dlpower.p2p.utils.Result;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * description:
 *
 * @author chenlanjiang
 * @date 2021/6/28
 */
@Controller
public class UserController {


    @Reference(interfaceClass = UserService.class, version = "1.0.0", check = false, timeout = 15000)
    private UserService userService;

//    @Reference(interfaceClass = RedisService.class, version = "1.0.0", check = false, timeout = 15000)
//    private RedisService redisService;


    /**
     * 跳转到注册页面
     * @return
     */
    @RequestMapping("/loan/page/register")
    public String register() {
        return "register";
    }

    @RequestMapping("/loan/checkPhone")
    @ResponseBody
    public Map<Object,Object> checkPhone(String phone) {

        // 获取手机号 将手机号作为查询条件 ，查询u_user表。
        User user = userService.queryUserByPhone(phone);
        if (!ObjectUtils.anyNotNull(user)) {
            // 1. 有数据
            // 返回手机号不可用的json {"code":-1,"message":"137xxxx被占用了","success":false}
            return Result.success();
        }else {
            // 2. 没有数据
            // 返回手机号可用的json {"code":1,"message":"","success":true}
            return Result.error("手机号:" + phone + "已经被注册了");
        }


        //return Result.success();
    }

    /**
     * 进行注册
     * @return
     */
    @RequestMapping("/loan/register")
    @ResponseBody
    public Map<Object,Object> toRegister(HttpServletRequest request,
                                         String phone, String loginPassword) {
        try {
            //开始注册,注册完成后，除了往数据库中保存数据。 直接把User对象保存到Session
            User user = userService.register(phone,loginPassword);
            // 将user对象保存到session中
            request.getSession().setAttribute(Constants.SESSION_USER,user);
        }catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统繁忙!..");
        }
        return Result.success();
    }

    @RequestMapping("/loan/senMessageCode")
    @ResponseBody
    public Map senMessageCode(String phone) throws Exception {
        // 请求第三方接口发送短信验证码 京东万象，短信平台
        Map<String, String> params = new HashMap<>();
        params.put("appkey","52c49aad676e0617748c31b6e1e455f4");
        params.put("mobile",phone);
        String randomCode = this.getRandomNum(6);
        params.put("content","【凯信通】您的验证码是：" + randomCode);
        String result = HttpClientUtils.doGet("https://way.jd.com/kaixintong/kaixintong", params);

        JSONObject jsonObject  = JSONObject.parseObject(result);
        System.out.println(result);
        String code = jsonObject.getString("code");
        System.out.println(code);
        if (!StringUtils.equals(code,"10000")) {
            return Result.error("请求第三方接口发送短信失败");
        }
        String string = jsonObject.getString("result");
        // string==> xml文件
        Document document = DocumentHelper.parseText(string);
        Node node = document.selectSingleNode("returnsms/returnstatus[1]");
        String successStr = node.getText();
        if (!StringUtils.equals("Success",successStr)) {
            return Result.error("发送短信失败");
        }

        // 将生成的验证码保存到redis中
        //redisService.put(phone,randomCode,5);


        return Result.success();
    }

    private String getRandomNum(int num) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++) {
            int randNum = random.nextInt(10);
            sb.append(randNum);
        }
        return sb.toString();
    }

}
