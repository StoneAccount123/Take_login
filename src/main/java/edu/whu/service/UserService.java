package edu.whu.service;

import edu.whu.entity.Result;
import edu.whu.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author myAdministrator
* @description 针对表【tb_user】的数据库操作Service
* @createDate 2022-11-03 22:00:02
*/
public interface UserService extends IService<User> {

    Result loginByPassword(String username, String content);

    Result loginBySMS(String username, String content);

    Result loginByEmail(String username, String content);
}