package aq.service.goods.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.GsonHelper;
import aq.common.util.MD5;
import aq.common.util.StringUtil;
import aq.common.util.UUIDUtil;
import aq.dao.customer.CustomerDao;
import aq.dao.goods.GoodsDao;
import aq.dao.system.SystemDao;
import aq.dao.user.UserDao;
import aq.service.base.Impl.BaseServiceImpl;
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
@Service("serviceGoods")
@DyncDataSource
public class GoodsServiceImpl extends BaseServiceImpl  implements GoodsService {

    @Resource
    private SystemDao sysDao;
    @Resource
    private GoodsDao goodsDao;
    @Resource
    private UserDao userDao;

    @Override
    public JsonObject queryGoodsList(JsonObject jsonObject) {
        jsonObject.addProperty("service","Goods");
        JsonObject user = verifyToken(jsonObject,(map)->{
            return sysDao.selectSysToken(map);
        },null);
        if(!StringUtil.isEmpty(user.get("code"))){
            return user;
        }
        return query(jsonObject,(map)->{
            return goodsDao.selectGoodsList(map);
        });
    }

    @Override
    public JsonObject insertGoods(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        jsonObject.addProperty("service","Goods");
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
        res.put("status", res.get("status"));
        res.put("createUser",user.get("id").getAsString());
        res.put("lastCreateUser",user.get("id").getAsString());
        res.put("createTime",new Date());
        res.put("lastCreateTime",new Date());
        goodsDao.insertGoods(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject updateGoods(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        jsonObject.addProperty("service","Goods");
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
        res.put("id",res.get("goodsId"));
        res.put("lastCreateUser",user.get("id").getAsString());
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

    @Override
    public JsonObject deleteGoods(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Goods");
        jsonObject.addProperty("service","Goods");
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
        goodsDao.deleteGoods(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

}
