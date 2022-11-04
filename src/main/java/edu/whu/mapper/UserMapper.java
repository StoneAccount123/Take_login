package edu.whu.mapper;

import edu.whu.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author myAdministrator
* @description 针对表【tb_user】的数据库操作Mapper
* @createDate 2022-11-03 22:00:02
* @Entity edu.whu.entity.User
*/

@Mapper
public interface UserMapper extends BaseMapper<User> {

}




