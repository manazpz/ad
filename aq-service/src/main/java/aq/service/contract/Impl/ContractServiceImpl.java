package aq.service.contract.Impl;

import aq.common.access.AbsAccessUser;
import aq.common.access.Factory;
import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.*;
import aq.dao.contract.ContractDao;
import aq.dao.goods.GoodsDao;
import aq.dao.system.SystemDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.contract.ContractService;
import aq.service.system.Func;
import com.google.gson.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceContract")
@DyncDataSource
public class ContractServiceImpl extends BaseServiceImpl  implements ContractService {

    @Resource
    private ContractDao contractDao;

    @Resource
    private GoodsDao goodsDao;

    @Resource
    private SystemDao sysDao;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryContractList(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Map<String,Object> res = new HashMap<>();
        jsonObject.addProperty("service","Contract");
        return query(jsonObject,(map)->{
            if(map.get("flag")== null){
                map.put("term",user.getUserId());
            }
            List<Map<String, Object>> contractMaps = contractDao.selectContracList(map);
            for(Map<String,Object> orderDetailMap :contractMaps) {
                res.clear();
                res.put("id",orderDetailMap.get("id"));
                List goodsMaps = new ArrayList<>();
                List goodsIdMaps = new ArrayList<>();
                List<Map<String, Object>> maps = contractDao.selectContractGoodList(res);
                for(int s = 0; s < maps.size(); s++){
                    goodsMaps.add(maps.get(s).get("name"));
                    goodsIdMaps.add(maps.get(s).get("goodsId"));
                }
                res.put("goods",goodsMaps);
                res.put("goodsIds",goodsIdMaps);
                orderDetailMap.putAll(res);
            }
            return contractMaps;
        });
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject insertContract(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("Contract");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
            return Func.functionRtnToJsonObject.apply(rtn);
        }
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id",UUIDUtil.getUUID());
        rest.put("number",res.get("contractType")+UUIDUtil.getRandomNo()+"01");
        List<Map<String, Object>> maps = contractDao.selectContracList(rest);
        if(maps.size()>0){
            res.put("number",res.get("contractType")+UUIDUtil.getRandomNo()+"0"+ maps.size()+1);
        }else{
            res.put("number",res.get("contractType")+UUIDUtil.getRandomNo()+"01");
        }
        rest.clear();
        res.put("title",res.get("title"));
        res.put("type",res.get("contractType"));
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
        try {
            Date signTime = new SimpleDateFormat("yyyy-MM-dd").parse(res.get("signTime").toString());
            Date expireTime = new SimpleDateFormat("yyyy-MM-dd").parse(res.get("expireTime").toString());
            res.put("signTime", signTime);
            res.put("expireTime", expireTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        res.put("currency", res.get("currency"));
        res.put("remarks1", res.get("reamrks1"));
        res.put("status", "BG");
        res.put("createUser",user.getUserId());
        res.put("lastCreateUser",user.getUserId());
        res.put("del", "Y");
        res.put("createTime",new Date());
        res.put("lastCreateTime",new Date());
        contractDao.insertContract(res);
        List<Map<String, Object>> mapgoods = (List<Map<String, Object>>) res.get("goodsIds");
        for(int i = 0; i < mapgoods.size(); i++){
            rest.put("id",res.get("id"));
            rest.put("no",i*10 +10);
            rest.put("goods",mapgoods.get(i));
            rest.put("createUser",user.getUserId());
            rest.put("lastCreateUser",user.getUserId());
            rest.put("createTime",new Date());
            rest.put("lastCreateTime",new Date());
            contractDao.insertGoods(rest);
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject updateContract(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("Contract");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
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
        try {
            Date signTime = new SimpleDateFormat("yyyy-MM-dd").parse(res.get("signTime").toString());
            Date expireTime = new SimpleDateFormat("yyyy-MM-dd").parse(res.get("expireTime").toString());
            res.put("signTime", signTime);
            res.put("expireTime", expireTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        res.put("currency", res.get("currency"));
        res.put("reamrks1", res.get("reamrks1"));
        res.put("updateTime",new Date());
        contractDao.updateContract(res);
        List<Map<String, Object>> mapgoods = (List<Map<String, Object>>) res.get("goodsIds");
        contractDao.deleteContractGoodList(res);
        rest.put("id",res.get("id"));
        for(int i = 0; i < mapgoods.size(); i++){
            res.clear();
            res.put("name",mapgoods.get(i));
            rest.put("no",i*10 +10);
            rest.put("goods",mapgoods.get(i));
            rest.put("createUser",user.getUserId());
            rest.put("lastCreateUser",user.getUserId());
            rest.put("createTime",new Date());
            rest.put("lastCreateTime",new Date());
            contractDao.insertGoods(rest);
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
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

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryContractPartner(JsonObject jsonObject) {
        jsonObject.addProperty("service","Contract");
        return query(jsonObject,(map)->{
            return contractDao.selectContracPartner(map);
        });
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject insertContractPartner(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        DecimalFormat df = new DecimalFormat("#.00");
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
        double income = Double.parseDouble(res.get("income").toString().replace(",", ""));
        double proportions = Double.parseDouble(res.get("proportions").toString().replace(",", ""));
        res.put("income",income*proportions/100);
        res.put("proportions",proportions);
        res.put("remarks1", res.get("reamrks1"));
        res.put("costType", res.get("costType"));
        res.put("createUser",user.getUserId());
        res.put("lastCreateUser",user.getUserId());
        res.put("createTime",new Date());
        res.put("lastCreateTime",new Date());
        contractDao.insertContractPartner(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryContractSubtList(JsonObject jsonObject) {
        jsonObject.addProperty("service","Contract");
        return query(jsonObject,(map)->{
            return contractDao.selectContractSubList(map);
        });
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
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
        res.put("number","Z"+ mapParent.get(0).get("number"));
        res.put("parent",mapParent.get(0).get("number"));
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



    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject insertContractExpnses(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        DecimalFormat df = new DecimalFormat("#.00");
        Rtn rtn = new Rtn("Contract");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
            return Func.functionRtnToJsonObject.apply(rtn);
        }
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id",res.get("id"));
        List<Map<String, Object>> maps = contractDao.selectContracExpenses(res);
        if(maps.size() == 0){
            res.put("no","10");
        }else{
            res.put("no",maps.size()*10 +10);
        }
        res.put("type",res.get("typePaye"));
        res.put("payee",res.get("payee"));
        res.put("payer",res.get("payer"));
        res.put("createUser",user.getUserId());
        res.put("lastCreateUser",user.getUserId());
        res.put("createTime",new Date());
        res.put("lastCreateTime",new Date());
        res.put("reamrks1",res.get("reamrks1"));
        contractDao.insertContractExpenses(res);
        if(res.get("file") != null ){
            List<Map<String, Object>> mapfile = (List<Map<String, Object>>) res.get("file");
            List<Map<String, Object>> mapsize = (List<Map<String, Object>>) res.get("size");
            List<Map<String, Object>> mapsuffix = (List<Map<String, Object>>) res.get("suffix");
            List<Map<String, Object>> mapname = (List<Map<String, Object>>) res.get("name");
            Map<String, Object> finalRes1 = res;
            for(int i = 0; i < mapfile.size(); i++){
                rest.put("id",UUIDUtil.getUUID());
                rest.put("type",finalRes1.get("types"));
                rest.put("refId",finalRes1.get("id"));
                rest.put("name",mapname.get(i));
                rest.put("size",mapsize.get(i));
                rest.put("extend",mapsuffix.get(i));
                rest.put("url",mapfile.get(i));
                rest.put("createUser",user.getUserId());
                rest.put("lastCreateUser",user.getUserId());
                rest.put("createTime",new Date());
                rest.put("lastCreateTime",new Date());
                //插入附件表
                sysDao.insertAttachment(rest);
            }
        }
        rest.clear();
        rest.put("id",res.get("id"));
        rest.put("parentId",res.get("id"));
        List<Map<String, Object>> maprest = contractDao.selectContracList(rest);
        List<Map<String, Object>> mapPaetner = contractDao.selectContracPartner(rest);
        List<Map<String, Object>> mapSub = contractDao.selectContracSub(rest);
        double amount = Double.parseDouble(res.get("amount").toString());
        if(maprest.size()>0 ){
            //实收总金额
            double paid = Double.parseDouble(maprest.get(0).get("paid").toString());
            //支出总金额
            double expenses = Double.parseDouble(maprest.get(0).get("expenses").toString());
            //总税收
            double tax = Double.parseDouble(maprest.get(0).get("tax").toString());
            if("FK".equals(res.get("typePaye"))){
                rest.put("expenses",df.format(expenses+amount));
                rest.put("income",df.format(paid-(expenses+amount)-tax));
            }else{
                double taxlimit = Double.parseDouble(maprest.get(0).get("taxlimit").toString());
                rest.put("paid",df.format(paid+amount));
                rest.put("income",df.format((paid+amount) - (paid+amount)*taxlimit/100 -expenses));
                rest.put("tax",df.format((paid+amount)*taxlimit/100));
            }
            //修改母合同信息
            contractDao.updateContract(rest);
            mapSub.forEach(p->{
                rest.put("id",p.get("FD_SUB_ID"));
                //修改子合同信息
                contractDao.updateContract(rest);
            });
        }
        rest.clear();
        Map<String, Object> finalRes = res;
        mapPaetner.forEach(p->{
            double pro = Double.parseDouble(p.get("pro").toString());
            double paid = Double.parseDouble(maprest.get(0).get("paid").toString());
            double expenses = Double.parseDouble(maprest.get(0).get("expenses").toString());
            double taxlimit = Double.parseDouble(maprest.get(0).get("taxlimit").toString());
            if("FK".equals(finalRes.get("typePaye"))){
                //已垫付金额
                rest.put("income",df.format((paid-amount)/pro));
            }else{
                //实收总金额
                rest.put("income",df.format(((paid+amount) - (paid+amount)/taxlimit) /pro));
            }
            rest.put("no",p.get("no"));
            rest.put("id",finalRes.get("id"));
            //修改合伙人收益
            contractDao.updateContractPartner(rest);
        });
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }



    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryContractExpnses(JsonObject jsonObject) {
        jsonObject.addProperty("service","Contract");
        return query(jsonObject,(map)->{
            return contractDao.selectContractExpnses(map);
        });
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryContractGoodList(JsonObject jsonObject) {
        jsonObject.addProperty("service","Contract");
        return query(jsonObject,(map)->{
            return contractDao.selectContractGoodList(map);
        });
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryContractAttaList(JsonObject jsonObject) {
        jsonObject.addProperty("service","Contract");
        return query(jsonObject,(map)->{
            return sysDao.selectContractAttaList(map);
        });
    }

}
