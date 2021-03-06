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

    //更新用户信息
    JsonObject updateUserInfo(JsonObject jsonObject);

    //删除/恢复用户
    JsonObject deleteUser(JsonObject jsonObject);

    //权限列表
    JsonObject selectPermissionList(JsonObject jsonObject);

    //更新权限
    JsonObject updatePermission(JsonObject jsonObject);

    //新增权限
    JsonObject insertPermission(JsonObject jsonObject);

    //用户权限列表
    JsonObject selectUserPermission(JsonObject jsonObject);

    //用户权限分配
    JsonObject updateUserPermission(JsonObject jsonObject);

}
