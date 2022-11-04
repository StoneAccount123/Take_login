package edu.whu.controller;

import cn.hutool.core.util.RandomUtil;
import edu.whu.entity.Result;
import edu.whu.service.UserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
public class LoginController {

    @Resource
    UserService userService;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @GetMapping("/code")
    public Result getCode(String phone){
        //利用第三方工具类生成六位随机验证码 并存入Redis中 且5min有效
        String code = RandomUtil.randomString("123456789",6);
        stringRedisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
        return Result.success(code);
    }

    /**
     *
     * @param username 用户名/手机号/邮箱账号
     * @param content 密码/验证码/验证码
     * @param loginType 登录方式
     * @return
     */
    @PostMapping("/login")
    public Result login(String username, String content, int loginType) {
        Result result=null;
        if(loginType==1){
            //密码登录
            result= userService.loginByPassword(username,content);
        }else if(loginType==2){
            //手机验证码登录
            result=userService.loginBySMS(username,content);
        }else if(loginType==3){
            //邮箱验证码登录
            result = userService.loginByEmail(username,content);
        }
        return result;
    }
}
