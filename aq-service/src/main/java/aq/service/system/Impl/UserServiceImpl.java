package aq.service.system.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.GsonHelper;
import aq.common.util.MD5;
import aq.common.util.StringUtil;
import aq.common.util.UUIDUtil;
import aq.dao.system.SystemDao;
import aq.dao.user.UserDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.system.Func;
import aq.service.system.UserService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
        jsonObject.addProperty("service","User");
        Map<String,Object> res = new HashMap<>();
        JsonObject user = verifyToken(jsonObject,(map)->{
            return sysDao.selectSysToken(map);
        },null);
        if(!StringUtil.isEmpty(user.get("code"))){
            return user;
        }
        res.clear();
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
        Rtn rtn = new Rtn("User");
        jsonObject.addProperty("service","User");
        Map<String,Object> res = new HashMap<>();
        JsonObject user = verifyToken(jsonObject,(map)->{
            return sysDao.selectSysToken(map);
        },null);
        if(!StringUtil.isEmpty(user.get("code"))){
            return user;
        }
        return query(jsonObject,(map)->{
            map.put("token",null);
            return userDao.selectUserInfo(map);
        });
    }

    @Override
    public JsonObject queryUserCustomerInfo(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Config");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> map = new HashMap<>();
        map = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> results = userDao.selectUserCustomerInfo(map);
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(results)).getAsJsonArray();
        rtn.setCode(200);
        rtn.setMessage("success");
        data.add("items",jsonArray);
        rtn.setData(data);
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject updateUserInfo(JsonObject jsonObject) {
        Rtn rtn = new Rtn("User");
        jsonObject.addProperty("service","User");
        Map<String,Object> res = new HashMap<>();
        JsonObject user = verifyToken(jsonObject,(map)->{
            return sysDao.selectSysToken(map);
        },null);
        if(!StringUtil.isEmpty(user.get("code"))){
            return user;
        }
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
        jsonObject.addProperty("service","User");
        Map<String,Object> res = new HashMap<>();
        JsonObject user = verifyToken(jsonObject,(map)->{
            return sysDao.selectSysToken(map);
        },null);
        if(!StringUtil.isEmpty(user.get("code"))){
            return user;
        }
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        userDao.deleteUser(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }
}
