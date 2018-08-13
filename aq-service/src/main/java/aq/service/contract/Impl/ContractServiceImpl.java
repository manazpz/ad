package aq.service.contract.Impl;

import aq.common.access.AbsAccessUser;
import aq.common.access.Factory;
import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.*;
import aq.dao.contract.ContractDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.contract.ContractService;
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
    private ContractDao contractDao;

    @Override
    public JsonObject queryContractList(JsonObject jsonObject) {
        jsonObject.addProperty("service","Contract");
        return query(jsonObject,(map)->{
            return contractDao.selectContracList(map);
        });
    }

    @Override
    public JsonObject insertContract(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("Contract");
        Map<String,Object> res = new HashMap<>();
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
            return Func.functionRtnToJsonObject.apply(rtn);
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
        res.put("remarks1", res.get("reamrks1"));
        res.put("status", "BG");
        res.put("createUser",user.getUserId());
        res.put("lastCreateUser",user.getUserId());
        res.put("del", "Y");
        res.put("createTime",new Date());
        res.put("lastCreateTime",new Date());
        contractDao.insertContract(res);
        List<Map<String, Object>> maps = contractDao.selectContracExpenses(res);
        if(maps.size() == 0){
            res.put("no","10");
        }else{
            res.put("no",maps.size()*10 +10);
        }
        res.put("type","01");
        res.put("amount",res.get("money_init"));
        res.put("amount",res.get("money_init"));
        res.put("payee",res.get("customerKeyA"));
        res.put("payer",res.get("customerKeyB"));
        contractDao.insertContractExpenses(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject updateContract(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("Contract");
        Map<String,Object> res = new HashMap<>();
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
            return Func.functionRtnToJsonObject.apply(rtn);
        }
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id",res.get("id"));
        res.put("lastCreateUser",user.getUserId());
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
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        contractDao.deleteContract(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject getContract(JsonObject jsonObject) {
        HashMap map = new HashMap();
        Rtn rtn = new Rtn("Contract");
        List<Map<String, Object>> tabs = contractDao.selectContracTabs(map);
        return null;
    }
}
