package aq.service.system;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface UserService extends BaseService {

    //新增用户
    JsonObject insertUserInfo(JsonObject jsonObject);

    //获取用户信息
    JsonObject queryUserInfo(JsonObject jsonObject);

    //获取用户信息
    JsonObject updateUserInfo(JsonObject jsonObject);

    //删除/恢复用户
    JsonObject deleteUser(JsonObject jsonObject);

}
