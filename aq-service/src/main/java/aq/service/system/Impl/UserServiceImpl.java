package aq.service.system.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.GsonHelper;
import aq.common.util.MD5;
import aq.common.util.UUIDUtil;
import aq.dao.system.SystemDao;
import aq.dao.user.UserDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.system.Func;
import aq.service.system.UserService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceUser")
@DyncDataSource
public class UserServiceImpl extends BaseServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private SystemDao sysDao;


    @Override
    public JsonObject insertUserInfo(JsonObject jsonObject) {
        Rtn rtn = new Rtn("User");
        Map<String,Object> res = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id", UUIDUtil.getUUID());
        res.put("createTime",new Date());
        res.put("updateTime",new Date());
        res.put("password", MD5.getMD5String(res.get("password").toString()));
        userDao.insertUserInfo(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject queryUserInfo(JsonObject jsonObject) {
        jsonObject.addProperty("service","User");
        return query(jsonObject,(map)->{
            return userDao.selectUserInfo(map);
        });
    }

    @Override
    public JsonObject updateUserInfo(JsonObject jsonObject) {
        Rtn rtn = new Rtn("User");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("updateTime",new Date());
        userDao.updateUserInfo(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject deleteUser(JsonObject jsonObject) {
        Rtn rtn = new Rtn("User");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        userDao.deleteUser(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject selectPermissionList(JsonObject jsonObject) {
        jsonObject.addProperty("service","User");
        return query(jsonObject,(map)->{
            return userDao.selectPermissionList(map);
        });
    }
}
