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
        if(StringUtil.isEmpty(jsonObject.get("flag"))) {
            jsonObject.addProperty("term",user.getUserId());
        }
        JsonObject contractMapJson = query(jsonObject, (map) -> {
            return contractDao.selectContracList(map);
        });
        Map contractMap = GsonHelper.getInstance().fromJson(contractMapJson, Map.class);
        List<Map> contractMaps = (List) ((Map) contractMap.get("data")).get("items");
        for(Map<String,Object> orderDetailMap :contractMaps) {
            res.clear();
            res.put("id",orderDetailMap.get("id"));
            res.put("no",orderDetailMap.get("NO"));
            res.put("type","HT");
            List<Map<String, Object>> maps = contractDao.selectContractGoodList(res);
            List<Map<String, Object>> mapAtta = sysDao.selectContractAttaList(res);
            orderDetailMap.put("goods",maps);
            orderDetailMap.put("file",mapAtta);
            orderDetailMap.put("fileList",mapAtta);
        }
        JsonArray asJsonArray = GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(contractMaps)).getAsJsonArray();
        contractMapJson.get("data").getAsJsonObject().add("items",asJsonArray);
        return contractMapJson;
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
        List<Map<String, Object>> parentList = null;
        if(!res.get("subContract").equals("")){
            rest.put("id",res.get("subContract"));
            List<Map<String, Object>> mapParent = contractDao.selectContracList(rest);
            parentList = mapParent;
            res.put("parent",mapParent.get(0).get("number"));
        }
        res.put("id",UUIDUtil.getUUID());
        res.put("title",res.get("title"));
        res.put("number",res.get("number"));
        res.put("type",res.get("contractType"));
        res.put("customerKeyA",res.get("customerKeyA"));
        res.put("customerKeyB", res.get("customerKeyB"));
        res.put("money", Double.parseDouble(res.get("money").equals("")? "0" :res.get("money").toString().replace(",", "")));
        res.put("money_init", Double.parseDouble(res.get("money_init").equals("")? "0" :res.get("money_init").toString().replace(",", "")));
        res.put("paid", Double.parseDouble(res.get("paid").equals("")? "0" :res.get("paid").toString().replace(",", "")));
        res.put("unpaid", Double.parseDouble(res.get("unpaid").equals("")? "0" :res.get("unpaid").toString().replace(",", "")));
        res.put("expenses", Double.parseDouble(res.get("expenses").equals("")? "0" :res.get("expenses").toString().replace(",", "")));
        res.put("income", Double.parseDouble(res.get("income").equals("")? "0" :res.get("income").toString().replace(",", "")));
        res.put("tax", Double.parseDouble(res.get("tax").equals("")? "0" :res.get("tax").toString().replace(",", "")));
        res.put("taxlimit", Double.parseDouble(res.get("taxlimit").equals("")? "0" :res.get("taxlimit").toString().replace(",", "")));
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
        res.put("del", "N");
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
        if(!res.get("subContract").equals("")){
            res.put("parentId",parentList.get(0).get("id"));
            List<Map<String, Object>> maps = contractDao.selectContracSub(res);
            if(maps.size() == 0){
                res.put("no","10");
            }else{
                res.put("no",maps.size()*10 +10);
            }
            contractDao.insertContractSub(res);
        }
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
                rest.put("url",mapfile.get(i).get("url"));
                rest.put("createUser",user.getUserId());
                rest.put("lastCreateUser",user.getUserId());
                rest.put("createTime",new Date());
                rest.put("lastCreateTime",new Date());
                //插入附件表
                sysDao.insertAttachment(rest);
            }
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
        res.put("number",res.get("number"));
        res.put("customerKeyA",res.get("customerKeyA"));
        res.put("customerKeyB", res.get("customerKeyB"));
        if(res.get("money") != null)  res.put("money", Double.parseDouble(res.get("money").equals("")? "0" :res.get("money").toString().replace(",", "")));
        res.put("money_init", res.get("money_init").equals("") ?"0" :res.get("money_init"));
        res.put("paid", Double.parseDouble(res.get("paid").equals("")? "0" :res.get("paid").toString().replace(",", "")));
        if(res.get("unpaid") != null) res.put("unpaid", Double.parseDouble(res.get("unpaid").equals("")? "0" :res.get("unpaid").toString().replace(",", "")));
        if(res.get("expenses") != null) res.put("expenses", Double.parseDouble(res.get("expenses").equals("")? "0" :res.get("expenses").toString().replace(",", "")));
        if(res.get("income") != null) res.put("income", Double.parseDouble(res.get("income").equals("")? "0" :res.get("income").toString().replace(",", "")));
        res.put("tax", Double.parseDouble(res.get("tax").equals("")? "0" :res.get("tax").toString().replace(",", "")));
        res.put("taxlimit", Double.parseDouble(res.get("taxlimit").equals("")? "0" :res.get("taxlimit").toString().replace(",", "")));
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
        List<Map<String, Object>> mapfileId = (List<Map<String, Object>>) res.get("fileId");
        if(mapfileId.size()>0){
            //進行刪除操作
            for(int i = 0; i < mapfileId.size(); i++){
                rest.put("id",mapfileId.get(i));
                sysDao.deleteAtta(rest);
            }
        }
        List<Map<String, Object>> mapFile = (List<Map<String, Object>>) res.get("file");
        List<Map<String, Object>> mapfileList = (List<Map<String, Object>>) res.get("fileList");
        List<Map<String, Object>> mapname = (List<Map<String, Object>>) res.get("name");
        List<Map<String, Object>> mapsize = (List<Map<String, Object>>) res.get("size");
        List<Map<String, Object>> mapsuffix = (List<Map<String, Object>>) res.get("suffix");
        for(int i = 0; i < mapFile.size(); i++){
            if("".equals(mapFile.get(i).get("id"))){
                rest.put("id",UUIDUtil.getUUID());
                rest.put("type",res.get("types"));
                rest.put("refId",res.get("id"));
                rest.put("name",mapname.get(i));
                rest.put("size",mapsize.get(i));
                rest.put("extend",mapsuffix.get(i));
                rest.put("url",mapFile.get(i).get("url"));
                rest.put("createUser",user.getUserId());
                rest.put("lastCreateUser",user.getUserId());
                rest.put("createTime",new Date());
                rest.put("lastCreateTime",new Date());
                //插入附件表
                sysDao.insertAttachment(rest);
            }
        }

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
    public JsonObject deleteContractPartner(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Contract");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        contractDao.deleteContractPartner(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject updateContractPartner(JsonObject jsonObject) {
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
        double pro = Double.parseDouble(res.get("pro").toString().replace(",", ""));
        double mainincome = Double.parseDouble(res.get("mainincome").toString().replace(",", ""));
        res.put("id",res.get("id"));
        res.put("no",res.get("no"));
        res.put("user",res.get("user"));
        res.put("pro",pro);
        res.put("income",pro*mainincome/100);
        contractDao.updateContractPartner(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
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
        res.put("type",res.get("typePayes"));
        res.put("payee",res.get("payee"));
        res.put("payer",res.get("payer"));
        res.put("costType", res.get("costType"));
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
                rest.put("remarks",finalRes1.get("no"));
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
        List<Map<String, Object>> maprest = contractDao.selectContracList(rest);
        rest.put("subId",res.get("id"));
        List<Map<String, Object>> mapSub = contractDao.selectContracSub(rest);
        List<Map<String, Object>> mapContract = new ArrayList<>();
        if(mapSub.size()>0) {
            rest.put("id", mapSub.get(0).get("FD_ID"));
            mapContract = contractDao.selectContracList(rest);
        }
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
            //确认为子合同
            if(maprest.get(0).get("parent")!= null){
                //修改母合同信息
                rest.put("id",res.get("id"));
                contractDao.updateContract(rest);
                for(int i = 0; i < mapContract.size(); i++){
                    double paid1 = Double.parseDouble(mapContract.get(i).get("paid").toString());
                    double expenses1 = Double.parseDouble(mapContract.get(i).get("expenses").toString());
                    double tax1 = Double.parseDouble(mapContract.get(i).get("tax").toString());
                    if("FK".equals(res.get("typePaye"))){
                        rest.put("expenses",df.format(expenses1+amount));
                        rest.put("income",df.format(paid1-(expenses1+amount)-tax1));
                    }else{
                        double taxlimit1 = Double.parseDouble(mapContract.get(0).get("taxlimit").toString());
                        rest.put("paid",df.format(paid1+amount));
                        rest.put("income",df.format((paid1+amount) - (paid1+amount)*taxlimit1/100 -expenses1));
                        rest.put("tax",df.format((paid1+amount)*taxlimit1/100));
                    }
                    rest.put("id",mapContract.get(i).get("id"));
                    contractDao.updateContract(rest);
                }
            }
            contractDao.updateContract(rest);
        }
        List<Map<String, Object>> finalMaprest = new ArrayList<>();
        if(mapContract.size()>0){
            rest.put("id",mapContract.get(0).get("id"));
            finalMaprest = mapContract;
        }else{
            rest.put("id",res.get("id"));
            finalMaprest = maprest;
        }
        List<Map<String, Object>> mapPaetner = contractDao.selectContracPartner(rest);
        rest.clear();
        Map<String, Object> finalRes = res;
        List<Map<String, Object>> finalMaprest1 = finalMaprest;
        mapPaetner.forEach(p->{
            double pro = Double.parseDouble(p.get("pro").toString());
            double paid = Double.parseDouble(finalMaprest1.get(0).get("paid").toString());
            double expenses = Double.parseDouble(finalMaprest1.get(0).get("expenses").toString());
            double taxlimit = Double.parseDouble(finalMaprest1.get(0).get("taxlimit").toString());
            double tax1 = Double.parseDouble(finalMaprest1.get(0).get("tax").toString());
            if("FK".equals(finalRes.get("typePaye"))){
                //已垫付(支出)金额
                rest.put("income",df.format((paid-(expenses+amount)-tax1)*pro/100));
            }else{
                //实收总金额
                rest.put("income",df.format((((paid+amount) - (paid+amount)*taxlimit/100)- expenses) *pro/100));
            }
            rest.put("no",p.get("no"));
            rest.put("id",finalMaprest1.get(0).get("id"));
            //修改合伙人收益
            contractDao.updateContractPartner(rest);
        });
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }



    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject updateContractExpnses(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        DecimalFormat df = new DecimalFormat("#.00");
        Rtn rtn = new Rtn("Contract");
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> rest = new HashMap<>();
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
            return Func.functionRtnToJsonObject.apply(rtn);
        }
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> mapfileId = (List<Map<String, Object>>) res.get("fileId");
        if(mapfileId.size()>0){
            //進行刪除操作
            for(int i = 0; i < mapfileId.size(); i++){
                rest.put("id",mapfileId.get(i));
                sysDao.deleteAtta(rest);
            }
        }
        List<Map<String, Object>> mapFile = (List<Map<String, Object>>) res.get("file");
        List<Map<String, Object>> mapname = (List<Map<String, Object>>) res.get("name");
        List<Map<String, Object>> mapsize = (List<Map<String, Object>>) res.get("size");
        List<Map<String, Object>> mapsuffix = (List<Map<String, Object>>) res.get("suffix");
        for(int i = 0; i < mapFile.size(); i++){
            if("".equals(mapFile.get(i).get("id"))){
                rest.put("id",UUIDUtil.getUUID());
                rest.put("type",res.get("types"));
                rest.put("remarks",res.get("NO"));
                rest.put("refId",res.get("id"));
                rest.put("name",mapname.get(i));
                rest.put("size",mapsize.get(i));
                rest.put("extend",mapsuffix.get(i));
                rest.put("url",mapFile.get(i).get("url"));
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
        List<Map<String, Object>> maprest = contractDao.selectContracList(rest);
        rest.put("subId",res.get("id"));
        List<Map<String, Object>> mapSub = contractDao.selectContracSub(rest);
        List<Map<String, Object>> mapContract = new ArrayList<>();
        if(mapSub.size()>0) {
            rest.put("id", mapSub.get(0).get("FD_ID"));
            mapContract = contractDao.selectContracList(rest);
        }
        List<Map<String, Object>> maps = contractDao.selectContracExpenses(res);
        double amountExp = 0.0;
        if(maps.size()>0){
            amountExp =  Double.parseDouble(maps.get(0).get("amount").toString());
        }
        double amount = Double.parseDouble(res.get("amount").toString());
        if(amountExp != amount){
            amount = amount - amountExp;
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
                //确认为子合同
                if(maprest.get(0).get("parent")!= null){
                    //修改母合同信息
                    rest.put("id",res.get("id"));
                    contractDao.updateContract(rest);
                    for(int i = 0; i < mapContract.size(); i++){
                        double paid1 = Double.parseDouble(mapContract.get(i).get("paid").toString());
                        double expenses1 = Double.parseDouble(mapContract.get(i).get("expenses").toString());
                        double tax1 = Double.parseDouble(mapContract.get(i).get("tax").toString());
                        if("FK".equals(res.get("typePaye"))){
                            rest.put("expenses",df.format(expenses1+amount));
                            rest.put("income",df.format(paid1-(expenses1+amount)-tax1));
                        }else{
                            double taxlimit1 = Double.parseDouble(mapContract.get(0).get("taxlimit").toString());
                            rest.put("paid",df.format(paid1+amount));
                            rest.put("income",df.format((paid1+amount) - (paid1+amount)*taxlimit1/100 -expenses1));
                            rest.put("tax",df.format((paid1+amount)*taxlimit1/100));
                        }
                        rest.put("id",mapContract.get(i).get("id"));
                        contractDao.updateContract(rest);
                    }
                }
                contractDao.updateContract(rest);
                rest.put("amount",Double.parseDouble(res.get("amount").toString()));
                rest.put("no",res.get("NO"));
                rest.put("typePay",res.get("typePayKey"));
                rest.put("costType",res.get("costTypeKey"));
                rest.put("payee",res.get("payeeId"));
                rest.put("payer",res.get("payerId"));
                rest.put("lastCreateUser",user.getUserId());
                rest.put("lastCreateTime",new Date());
                rest.put("reamrks",res.get("reamrks1"));
                contractDao.updateContractExpenses(rest);
            }
            List<Map<String, Object>> finalMaprest = new ArrayList<>();
            if(mapContract.size()>0){
                rest.put("id",mapContract.get(0).get("id"));
                finalMaprest = mapContract;
            }else{
                rest.put("id",res.get("id"));
                finalMaprest = maprest;
            }
            List<Map<String, Object>> mapPaetner = contractDao.selectContracPartner(rest);
            rest.clear();
            Map<String, Object> finalRes = res;
            List<Map<String, Object>> finalMaprest1 = finalMaprest;
            double finalAmount = amount;
            mapPaetner.forEach(p->{
                double pro = Double.parseDouble(p.get("pro").toString());
                double paid = Double.parseDouble(finalMaprest1.get(0).get("paid").toString());
                double expenses = Double.parseDouble(finalMaprest1.get(0).get("expenses").toString());
                double taxlimit = Double.parseDouble(finalMaprest1.get(0).get("taxlimit").toString());
                double tax1 = Double.parseDouble(finalMaprest1.get(0).get("tax").toString());
                if("FK".equals(finalRes.get("typePaye"))){
                    //已垫付(支出)金额
                    rest.put("income",df.format((paid-(expenses+ finalAmount)-tax1)*pro/100));
                }else{
                    //实收总金额
                    rest.put("income",df.format((((paid+ finalAmount) - (paid+ finalAmount)*taxlimit/100)- expenses) *pro/100));
                }
                rest.put("no",p.get("no"));
                rest.put("id",finalMaprest1.get(0).get("id"));
                //修改合伙人收益
                contractDao.updateContractPartner(rest);
            });
        }else{
            res.put("id",res.get("id"));
            res.put("no",res.get("no"));
            res.put("typePayKey",res.get("typePayKey"));
            res.put("costTypeKey",res.get("costTypeKey"));
            res.put("amount",res.get("amount"));
            res.put("payeeId",res.get("payeeId"));
            res.put("payerId",res.get("payerId"));
            res.put("lastCreateUser",user.getUserId());
            res.put("lastCreateTime",new Date());
            res.put("reamrks",res.get("reamrks1"));
            contractDao.updateContractExpenses(res);
        }

        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryContractExpnses(JsonObject jsonObject) {
        jsonObject.addProperty("service","Contract");
        Map<String,Object> res = new HashMap<>();
        return query(jsonObject,(map)->{
            List<Map<String, Object>> contractMaps = contractDao.selectContractExpnses(map);
            List nameMaps = new ArrayList<>();
            List urlMaps = new ArrayList<>();
            List extendMaps = new ArrayList<>();
            List sizesMaps = new ArrayList<>();
            List<Map<String, Object>> listFileMaps = new ArrayList<>();
            List<Map<String, Object>> fileMaps = new ArrayList<>();
            for(Map<String,Object> orderDetailMap :contractMaps){
                res.put("no",orderDetailMap.get("no"));
                res.put("type","SZ");
                res.put("id",orderDetailMap.get("id"));
                List<Map<String, Object>> maps = sysDao.selectContractAttaList(res);
                orderDetailMap.put("file",maps);
                orderDetailMap.put("fileList",maps);
            }

            return contractMaps;
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


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject deleteAtta(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Contract");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id",res.get("id"));
        res.put("type",res.get("types"));
        res.put("url",res.get("flie"));
        sysDao.deleteAtta(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject deleteContractExpnses(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        DecimalFormat df = new DecimalFormat("#.00");
        Rtn rtn = new Rtn("Contract");
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> rest = new HashMap<>();
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
            return Func.functionRtnToJsonObject.apply(rtn);
        }
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id",res.get("id"));
        res.put("no",res.get("no"));
        List<Map<String, Object>> mapz = contractDao.selectContracExpenses(res);
        if(mapz.size()>0){
            mapz.get(0).get("amount");
            rest.put("id",res.get("id"));
            List<Map<String, Object>> maprest = contractDao.selectContracList(rest);
            rest.put("subId",res.get("id"));
            List<Map<String, Object>> mapSub = contractDao.selectContracSub(rest);
            List<Map<String, Object>> mapContract = new ArrayList<>();
            if(mapSub.size()>0) {
                rest.put("id", mapSub.get(0).get("FD_ID"));
                mapContract = contractDao.selectContracList(rest);
            }
            List<Map<String, Object>> maps = contractDao.selectContracExpenses(res);
            double amount = Double.parseDouble(mapz.get(0).get("amount").toString());
            if(maprest.size()>0 ){
                //实收总金额
                double paid = Double.parseDouble(maprest.get(0).get("paid").toString());
                //支出总金额
                double expenses = Double.parseDouble(maprest.get(0).get("expenses").toString());
                //总税收
                double tax = Double.parseDouble(maprest.get(0).get("tax").toString());
                double taxlimit = Double.parseDouble(maprest.get(0).get("taxlimit").toString());
                rest.put("paid",df.format(paid+amount));
                rest.put("income",df.format((paid+amount) - (paid+amount)*taxlimit/100 -expenses));
                rest.put("tax",df.format((paid+amount)*taxlimit/100));
                //确认为子合同
                if(maprest.get(0).get("parent")!= null){
                    //修改母合同信息
                    rest.put("id",res.get("id"));
                    contractDao.updateContract(rest);
                    for(int i = 0; i < mapContract.size(); i++){
                        double paid1 = Double.parseDouble(mapContract.get(i).get("paid").toString());
                        double expenses1 = Double.parseDouble(mapContract.get(i).get("expenses").toString());
                        double tax1 = Double.parseDouble(mapContract.get(i).get("tax").toString());
                        double taxlimit1 = Double.parseDouble(mapContract.get(0).get("taxlimit").toString());
                        rest.put("paid",df.format(paid1+amount));
                        rest.put("income",df.format((paid1+amount) - (paid1+amount)*taxlimit1/100 -expenses1));
                        rest.put("tax",df.format((paid1+amount)*taxlimit1/100));
                        rest.put("id",mapContract.get(i).get("id"));
                        contractDao.updateContract(rest);
                    }
                }
                contractDao.updateContract(rest);
            }
            List<Map<String, Object>> finalMaprest = new ArrayList<>();
            if(mapContract.size()>0){
                rest.put("id",mapContract.get(0).get("id"));
                finalMaprest = mapContract;
            }else{
                rest.put("id",res.get("id"));
                finalMaprest = maprest;
            }
            List<Map<String, Object>> mapPaetner = contractDao.selectContracPartner(rest);
            rest.clear();
            Map<String, Object> finalRes = res;
            List<Map<String, Object>> finalMaprest1 = finalMaprest;
            double finalAmount = amount;
            mapPaetner.forEach(p-> {
                double pro = Double.parseDouble(p.get("pro").toString());
                double paid = Double.parseDouble(finalMaprest1.get(0).get("paid").toString());
                double expenses = Double.parseDouble(finalMaprest1.get(0).get("expenses").toString());
                double taxlimit = Double.parseDouble(finalMaprest1.get(0).get("taxlimit").toString());
                double tax1 = Double.parseDouble(finalMaprest1.get(0).get("tax").toString());
                //实收总金额
                rest.put("income", df.format((((paid + finalAmount) - (paid + finalAmount) * taxlimit / 100) - expenses) * pro / 100));
                rest.put("no", p.get("no"));
                rest.put("id", finalMaprest1.get(0).get("id"));
                //修改合伙人收益
                contractDao.updateContractPartner(rest);
            });
            res.put("id",res.get("id"));
            res.put("no",res.get("no"));
            contractDao.deleteContracExpenses(res);
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

}
