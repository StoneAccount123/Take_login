package edu.whu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.whu.entity.Result;
import edu.whu.entity.User;
import edu.whu.service.UserService;
import edu.whu.mapper.UserMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author myAdministrator
* @description 针对表【tb_user】的数据库操作Service实现
* @createDate 2022-11-03 22:00:02
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    //被禁用的账号集合的key
    public static final String BLACKLIST_KEY = "login:blacklist";

    public static final String LOGINFAIL_PREFIX = "login:fail";

    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
     *
     * @param username 用户名
     * @param content 密码
     * @return
     */
    @Override
    public Result loginByPassword(String username, String content) {
        //1.检查是否被禁用
        if(stringRedisTemplate.opsForSet().isMember(BLACKLIST_KEY,username)){
            return Result.Fail("账号已被禁用!");
        }
        //2.查询用户信息是否正确
        User one = getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));

        //3不正确则在Redis中记录次数 超过五次则加入黑名单将其禁用
        if(one == null||one.getPassword()!=content){
            Long increment = stringRedisTemplate.opsForValue().increment(LOGINFAIL_PREFIX + username);
            if(increment>5){
                stringRedisTemplate.opsForSet().add(BLACKLIST_KEY,username);
                return Result.Fail("失败次数过多，已被禁用!");
            }
            return Result.Fail("密码错误");
        }

        //4.正确则登录成功
        return Result.success(one);
    }

    /**
     *
     * @param username 手机号
     * @param content 验证码
     * @return
     */
    @Override
    public Result loginBySMS(String username, String content) {
       //1.检查是否被禁用
        if(stringRedisTemplate.opsForSet().isMember(BLACKLIST_KEY,username)){
            return Result.Fail("账号已被禁用!");
        }

        //2.查询用户信息是否正确
        String code = stringRedisTemplate.opsForValue().get(username);

        //3不正确则在Redis中记录次数 超过五次则加入黑名单将其禁用
        if(code==null||code!=content){
            Long increment = stringRedisTemplate.opsForValue().increment(LOGINFAIL_PREFIX + username);
            if(increment>5){
                stringRedisTemplate.opsForSet().add(BLACKLIST_KEY,username);
                return Result.Fail("失败次数过多，已被禁用!");
            }
            return Result.Fail("密码错误");
        }

        //4.正确则登录成功
        User one = getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, username));
        return Result.success(one);
    }

    @Override
    public Result loginByEmail(String username, String content) {
        //1.检查是否被禁用
        if(stringRedisTemplate.opsForSet().isMember(BLACKLIST_KEY,username)){
            return Result.Fail("账号已被禁用!");
        }

        //2.查询用户信息是否正确
        String code = stringRedisTemplate.opsForValue().get(username);

        //3不正确则在Redis中记录次数 超过五次则加入黑名单将其禁用
        if(code==null||code!=content){
            Long increment = stringRedisTemplate.opsForValue().increment(LOGINFAIL_PREFIX + username);
            if(increment>5){
                stringRedisTemplate.opsForSet().add(BLACKLIST_KEY,username);
                return Result.Fail("失败次数过多，已被禁用!");
            }
            return Result.Fail("密码错误");
        }

        //4.正确则登录成功
        User one = getOne(new LambdaQueryWrapper<User>().eq(User::getEmail, username));
        return Result.success(one);
    }
}




