package aq.dao.user;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoUser")
public interface UserDao {

    //新增用户
    int insertUserInfo(Map<String,Object> map);

    //获取用户信息
    List<Map<String,Object>> selectUserInfo(Map<String,Object> map);

    //获取用户信息
    int updateUserInfo(Map<String,Object> map);
}