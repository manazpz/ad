package aq.service.system;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface UserService extends BaseService {

    //新增用户
    JsonObject insertUserInfo(JsonObject jsonObject);

}
