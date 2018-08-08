package aq.service.customer.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.GsonHelper;
import aq.common.util.MD5;
import aq.common.util.StringUtil;
import aq.common.util.UUIDUtil;
import aq.dao.customer.CustomerDao;
import aq.dao.system.SystemDao;
import aq.dao.user.UserDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.customer.CustomerService;
import aq.service.system.Func;
import com.google.gson.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceCustomer")
@DyncDataSource
public class CustomerServiceImpl extends BaseServiceImpl  implements CustomerService {

    @Resource
    private SystemDao sysDao;
    @Resource
    private CustomerDao customerDao;
    @Resource
    private UserDao userDao;

    @Override
    public JsonObject queryCustomerList(JsonObject jsonObject) {
        jsonObject.addProperty("service","Customer");
        JsonObject user = verifyToken(jsonObject,(map)->{
            return sysDao.selectSysToken(map);
        },null);
        if(!StringUtil.isEmpty(user.get("code"))){
            return user;
        }
        return query(jsonObject,(map)->{
            return customerDao.selectCustomerList(map);
        });
    }

    @Override
    public JsonObject insertCustomer(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Customer");
        jsonObject.addProperty("service","Customer");
        Map<String,Object> res = new HashMap<>();
        JsonObject user = verifyToken(jsonObject,(map)->{
            return sysDao.selectSysToken(map);
        },(map)->{
            return sysDao.selectUserInfo(map);
        });
        if(!StringUtil.isEmpty(user.get("code"))){
            return user;
        }
        res.clear();
        res.put("customerUser",jsonObject.get("customerUser").getAsString());
        List<Map<String, Object>> customers = customerDao.selectCustomerList(res);
        if(customers.size() > 0) {
            rtn.setCode(60001);
            rtn.setMessage("已存在客户！");
            return Func.functionRtnToJsonObject.apply(rtn);
        }
        res.clear();
        res.put("userName",jsonObject.get("customerUser").getAsString());
        List<Map<String, Object>> users = userDao.selectUserInfo(res);
        if(users.size() > 0) {
            rtn.setCode(60002);
            rtn.setMessage("已存在用户！");
            return Func.functionRtnToJsonObject.apply(rtn);
        }
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id",UUIDUtil.getUUID());
        res.put("createUser",user.get("id").getAsString());
        res.put("lastCreateUser",user.get("id").getAsString());
        res.put("createTime",new Date());
        res.put("lastCreateTime",new Date());
        customerDao.insertCustomer(res);
        res.put("nickName",res.get("customerName"));
        res.put("userName",res.get("customerUser"));
        res.put("password", MD5.getMD5String("123456"));
        res.put("phone", res.get("code"));
        res.put("status", res.get("type"));
        res.put("updateTime",new Date());
        userDao.insertUserInfo(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject updateCustomer(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Customer");
        jsonObject.addProperty("service","Customer");
        Map<String,Object> res = new HashMap<>();
        JsonObject user = verifyToken(jsonObject,(map)->{
            return sysDao.selectSysToken(map);
        },(map)->{
            return sysDao.selectUserInfo(map);
        });
        if(!StringUtil.isEmpty(user.get("code"))){
            return user;
        }
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id",res.get("customerId"));
        res.put("lastCreateUser",user.get("id").getAsString());
        res.put("lastCreateTime",new Date());
        customerDao.updateCustomer(res);
        res.put("nickName",res.get("customerName"));
        res.put("phone", res.get("code"));
        res.put("status", res.get("type"));
        res.put("updateTime",new Date());
        userDao.updateUserInfo(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject deleteCustomer(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Customer");
        jsonObject.addProperty("service","Customer");
        Map<String,Object> res = new HashMap<>();
        JsonObject user = verifyToken(jsonObject,(map)->{
            return sysDao.selectSysToken(map);
        },(map)->{
            return sysDao.selectUserInfo(map);
        });
        if(!StringUtil.isEmpty(user.get("code"))){
            return user;
        }
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        customerDao.deleteCustomer(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }


}
