package aq.service.goods.Impl;

import aq.common.access.AbsAccessUser;
import aq.common.access.Factory;
import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.GsonHelper;
import aq.common.util.UUIDUtil;
import aq.dao.goods.GoodsDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.goods.GoodsService;
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
@Service("serviceGoods")
@DyncDataSource
public class GoodsServiceImpl extends BaseServiceImpl  implements GoodsService {

    @Resource
    private GoodsDao goodsDao;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryGoodsList(JsonObject jsonObject) {
        jsonObject.addProperty("service","Goods");
        return query(jsonObject,(map)->{
            return goodsDao.selectGoodsList(map);
        });
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject insertGoods(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
            return Func.functionRtnToJsonObject.apply(rtn);
        }
        res.clear();
        res.put("name",jsonObject.get("name").getAsString());
        List<Map<String, Object>> goods = goodsDao.selectGoodsList(res);
        if(goods.size() > 0) {
            rtn.setCode(70001);
            rtn.setMessage("已存在商品！");
            return Func.functionRtnToJsonObject.apply(rtn);
        }
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id",UUIDUtil.getUUID());
        res.put("code",UUIDUtil.getRandomReqNo());
        res.put("name",res.get("name"));
        res.put("alias",res.get("alias"));
        res.put("typeKey", res.get("typeKey"));
        res.put("unit", res.get("unit"));
        res.put("price", res.get("price"));
        res.put("remarks", res.get("remarks"));
        res.put("status", res.get("status"));
        res.put("createUser",user.getUserId());
        res.put("lastCreateUser",user.getUserId());
        res.put("createTime",new Date());
        res.put("lastCreateTime",new Date());
        goodsDao.insertGoods(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject updateGoods(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
            return Func.functionRtnToJsonObject.apply(rtn);
        }
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id",res.get("goodsId"));
        res.put("lastCreateUser",user.getUserId());
        res.put("lastCreateTime",new Date());
        res.put("name",res.get("name"));
        res.put("alias", res.get("alias"));
        res.put("typeKey", res.get("typeKey"));
        res.put("unit", res.get("unit"));
        res.put("price", res.get("price"));
        res.put("updateTime",new Date());
        goodsDao.updateGoods(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject deleteGoods(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        goodsDao.deleteGoods(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

}
