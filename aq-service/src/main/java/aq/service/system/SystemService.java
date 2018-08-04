package aq.service.system;

import aq.service.base.BaseService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Created by ywb on 2017-02-23.
 */
public interface SystemService extends BaseService {

    //登录
    JsonObject queryLogin(JsonObject jsonObject);

    //刷新token
    JsonObject refreshToken(JsonObject jsonObject);

    //查询用户信息
    JsonObject queryUserInfo(JsonObject jsonObject);



















    //region 用户
    //查询
    JsonObject queryUser(JsonObject jsonObject);

    //新增
    JsonObject insertUser(JsonObject jsonObject);

    //修改
    JsonObject updateUser(JsonObject jsonObject);

    //删除
    JsonObject deleteUser(JsonObject jsonObject);
    //endregion

    //region 组织
    //查询
    JsonObject queryOrganization(JsonObject jsonObject);

    JsonObject queryOrganizationById(JsonObject jsonObject);

    //新增
    JsonObject insertOrganization(JsonObject jsonObject);

    //修改
    JsonObject updateOrganization(JsonObject jsonObject);

    //删除
    JsonObject deleteOrganization(JsonObject jsonObject);
    //endregion

}
