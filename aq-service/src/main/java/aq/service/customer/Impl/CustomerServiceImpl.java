package aq.service.customer.Impl;

import aq.common.access.AbsAccessUser;
import aq.common.access.Factory;
import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.GsonHelper;
import aq.common.util.MD5;
import aq.common.util.StringUtil;
import aq.common.util.UUIDUtil;
import aq.dao.customer.CustomerDao;
import aq.dao.user.UserDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.customer.CustomerService;
import aq.service.system.Func;
import com.google.gson.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private CustomerDao customerDao;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryCustomerList(JsonObject jsonObject) {
        jsonObject.addProperty("service","Customer");
        return query(jsonObject,(map)->{
            return customerDao.selectCustomerList(map);
        });
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject insertCustomer(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("Customer");
        Map<String,Object> res = new HashMap<>();
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
            return Func.functionRtnToJsonObject.apply(rtn);
        }
        res.clear();
        res.put("code",jsonObject.get("code").getAsString());
        List<Map<String, Object>> customers = customerDao.selectCustomerList(res);
        if(customers.size() > 0) {
            rtn.setCode(60001);
            rtn.setMessage("已存在客户！");
            return Func.functionRtnToJsonObject.apply(rtn);
        }
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id",UUIDUtil.getUUID());
        res.put("customerUser",jsonObject.get("code").getAsString());
        res.put("createUser",user.getUserId());
        res.put("lastCreateUser",user.getUserId());
        res.put("createTime",new Date());
        res.put("lastCreateTime",new Date());
        customerDao.insertCustomer(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject updateCustomer(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("Customer");
        Map<String,Object> res = new HashMap<>();
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
            return Func.functionRtnToJsonObject.apply(rtn);
        }
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        if(!StringUtil.isEmpty(jsonObject.get("code"))) {
            res.put("customerUser",jsonObject.get("code").getAsString());
        }
        res.put("id",res.get("customerId"));
        res.put("lastCreateUser",user.getUserId());
        res.put("lastCreateTime",new Date());
        customerDao.updateCustomer(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject deleteCustomer(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Customer");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        customerDao.deleteCustomer(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }


}
