package aq.service.contract.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.*;
import aq.dao.contract.ContractDao;
import aq.dao.customer.CustomerDao;
import aq.dao.goods.GoodsDao;
import aq.dao.system.SystemDao;
import aq.dao.user.UserDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.contract.ContractService;
import aq.service.customer.CustomerService;
import aq.service.goods.GoodsService;
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
@Service("serviceContract")
@DyncDataSource
public class ContractServiceImpl extends BaseServiceImpl  implements ContractService {

    @Resource
    private SystemDao sysDao;
    @Resource
    private ContractDao contractDao;
    @Resource
    private UserDao userDao;

    @Override
    public JsonObject queryContractList(JsonObject jsonObject) {
        jsonObject.addProperty("service","Contract");
        JsonObject user = verifyToken(jsonObject,(map)->{
            return sysDao.selectSysToken(map);
        },null);
        if(!StringUtil.isEmpty(user.get("code"))){
            return user;
        }
        return query(jsonObject,(map)->{
            return contractDao.selectContracList(map);
        });
    }

    @Override
    public JsonObject insertContract(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Contract");
        jsonObject.addProperty("service","Contract");
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
        res.put("id",UUIDUtil.getUUID());
        res.put("number",UUIDUtil.getRandomReqNumber());
        res.put("title",res.get("title"));
        res.put("customerKeyA",res.get("customerKeyA"));
        res.put("customerKeyB", res.get("customerKeyB"));
        res.put("money", res.get("money").equals("") ?"0" :res.get("money"));
        res.put("money_init", res.get("money_init").equals("") ?"0" :res.get("money_init"));
        res.put("paid", res.get("paid").equals("")? "0" :res.get("paid"));
        res.put("unpaid", res.get("unpaid").equals("") ?"0" :res.get("unpaid"));
        res.put("expenses", res.get("expenses").equals("") ?"0" :res.get("expenses"));
        res.put("income", res.get("income").equals("")? "0" :res.get("income"));
        res.put("tax", res.get("tax").equals("")? "0" :res.get("tax"));
        res.put("taxlimit", res.get("taxlimit").equals("")? "0" :res.get("taxlimit"));
        res.put("signTime", DateTime.compareDate(res.get("signTime").toString()));
        res.put("expireTime", DateTime.compareDate(res.get("expireTime").toString()));
        res.put("currency", res.get("currency"));
        res.put("reamrks1", res.get("reamrks1"));
        res.put("status", "BG");
        res.put("createUser",user.get("id").getAsString());
        res.put("lastCreateUser",user.get("id").getAsString());
        res.put("createTime",new Date());
        res.put("lastCreateTime",new Date());
        contractDao.insertContract(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject updateContract(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Contract");
        jsonObject.addProperty("service","Contract");
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
        res.put("id",res.get("id"));
        res.put("lastCreateUser",user.get("id").getAsString());
        res.put("lastCreateTime",new Date());
        res.put("title",res.get("title"));
        res.put("customerKeyA",res.get("customerKeyA"));
        res.put("customerKeyB", res.get("customerKeyB"));
        if(res.get("money") != null)  res.put("money", res.get("money").equals("")?"0" :res.get("money"));
        res.put("money_init", res.get("money_init").equals("") ?"0" :res.get("money_init"));
        res.put("paid", res.get("paid").equals("")? "0" :res.get("paid"));
        if(res.get("unpaid") != null) res.put("unpaid", res.get("unpaid").equals("") ?"0" :res.get("unpaid"));
        if(res.get("expenses") != null) res.put("expenses", res.get("expenses").equals("") ?"0" :res.get("expenses"));
        if(res.get("income") != null) res.put("income", res.get("income").equals("")? "0" :res.get("income"));
        res.put("tax", res.get("tax").equals("")? "0" :res.get("tax"));
        res.put("taxlimit", res.get("taxlimit").equals("")? "0" :res.get("taxlimit"));
        res.put("signTime", DateTime.compareDate(res.get("signTime").toString()));
        res.put("expireTime", DateTime.compareDate(res.get("expireTime").toString()));
        res.put("currency", res.get("currency"));
        res.put("reamrks1", res.get("reamrks1"));
        res.put("updateTime",new Date());
        contractDao.updateContract(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject deleteContract(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Contract");
        jsonObject.addProperty("service","Contract");
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
        contractDao.deleteContract(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }


}
