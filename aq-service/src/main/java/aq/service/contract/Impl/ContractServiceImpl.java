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
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public JsonObject queryContractPartner(JsonObject jsonObject) {
        jsonObject.addProperty("service","Contract");
        return query(jsonObject,(map)->{
            return contractDao.selectContracPartner(map);
        });
    }


    @Override
    public JsonObject insertContractPartner(JsonObject jsonObject) {
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
        res.put("id",res.get("contractId"));
        List<Map<String, Object>> maps = contractDao.selectContracPartner(res);
        if(maps.size() == 0){
            res.put("no","10");
        }else{
            res.put("no",maps.size()*10 +10);
        }
        res.put("user",res.get("user"));
        res.put("types",res.get("types"));
        res.put("income",res.get("income"));
        res.put("proportions",res.get("proportions"));
        res.put("remarks1", res.get("reamrks1"));
        res.put("createUser",user.getUserId());
        res.put("lastCreateUser",user.getUserId());
        res.put("createTime",new Date());
        res.put("lastCreateTime",new Date());
        contractDao.insertContractPartner(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject queryContractSubtList(JsonObject jsonObject) {
        jsonObject.addProperty("service","Contract");
        return query(jsonObject,(map)->{
            return contractDao.selectContractSubList(map);
        });
    }

    @Override
    public JsonObject insertContractSub(JsonObject jsonObject) {
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
        List<Map<String, Object>> mapParent = contractDao.selectContracList(res);
        res.put("id",UUIDUtil.getUUID());
        res.put("number",mapParent.get(0).get("number"));
        res.put("parent",UUIDUtil.getRandomReqNumber());
        res.put("title",mapParent.get(0).get("title"));
        res.put("customerKeyA",mapParent.get(0).get("customerKeyA"));
        res.put("customerKeyB", mapParent.get(0).get("customerKeyB"));
        res.put("money", mapParent.get(0).get("money"));
        res.put("money_init", mapParent.get(0).get("money_init"));
        res.put("paid", mapParent.get(0).get("paid"));
        res.put("unpaid", mapParent.get(0).get("unpaid"));
        res.put("expenses", mapParent.get(0).get("expenses"));
        res.put("income", mapParent.get(0).get("income"));
        res.put("tax", mapParent.get(0).get("tax"));
        res.put("taxlimit", mapParent.get(0).get("taxlimit"));
        try {
            Date expireTime = new SimpleDateFormat("yyyy-MM-dd").parse(mapParent.get(0).get("expireTime").toString());
            Date signTime = new SimpleDateFormat("yyyy-MM-dd").parse(mapParent.get(0).get("signTime").toString());
            res.put("signTime",signTime);
            res.put("expireTime", expireTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        res.put("currency", mapParent.get(0).get("currency"));
        res.put("remarks1", mapParent.get(0).get("reamrks1"));
        res.put("status", "BG");
        res.put("createUser",user.getUserId());
        res.put("lastCreateUser",user.getUserId());
        res.put("del", "Y");
        res.put("createTime",new Date());
        res.put("lastCreateTime",new Date());
        contractDao.insertContract(res);
        res.put("parentId",mapParent.get(0).get("id"));
        List<Map<String, Object>> maps = contractDao.selectContracSub(res);
        if(maps.size() == 0){
            res.put("no","10");
        }else{
            res.put("no",maps.size()*10 +10);
        }
        contractDao.insertContractSub(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }


    @Override
    public JsonObject insertContractExpnses(JsonObject jsonObject) {
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
//        List<Map<String, Object>> maps = contractDao.selectContracExpenses(res);
//        if(maps.size() == 0){
//            res.put("no","10");
//        }else{
//            res.put("no",maps.size()*10 +10);
//        }
//        res.put("typePaye",res.get("typePaye"));
//        res.put("payee",res.get("customerKeyA"));
//        res.put("payer",res.get("customerKeyB"));
//        if("FK".equals(res.get("typePaye"))){
//            res.put("amount","-"+ res.get("money_init"));
//        }else{
//            res.put("amount",res.get("money_init"));
//        }
//        contractDao.insertContractExpenses(res);

        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }
}
