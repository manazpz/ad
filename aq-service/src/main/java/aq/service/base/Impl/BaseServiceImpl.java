package aq.service.base.Impl;

import aq.common.other.Rtn;
import aq.common.util.DateTime;
import aq.common.util.GsonHelper;
import aq.common.util.StringUtil;
import aq.service.base.BaseService;
import aq.service.system.Func;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.*;
import java.util.function.Function;

public class BaseServiceImpl implements BaseService {

    @Override
    public JsonObject query(JsonObject jsonObject, Function<Map<String,Object>, List<Map<String, Object>>> func) {
        Rtn rtn = new Rtn(jsonObject.get("service").getAsString());
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        data.addProperty("total",0);
        data.add("items",jsonArray);
        Map<String,Object> map = new HashMap<>();
        Integer pageNum = StringUtil.isEmpty(jsonObject.get("pageNum"))?1:Integer.parseInt(jsonObject.get("pageNum").getAsString());
        Integer pageSize = jsonObject.get("pageSize") == null ? 1 : Integer.parseInt(jsonObject.get("pageSize").getAsString());
        PageHelper.startPage(pageNum,pageSize);
        map = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        if (map.get("list")!=null){
            List list = new ArrayList();
            Map<String,Object> mapList = new HashMap<>();
            mapList = (Map)map.get("list");
            for (String key : mapList.keySet()){
                list = Arrays.asList(mapList.get(key).toString().split(","));
                map.put(key,list);
            }
        }
        if (map.get("map")!=null){
            Map<String,Object> _mapList = new HashMap<>();
            _mapList = (Map)map.get("map");
            for (String _key : _mapList.keySet()){
                map.put(_key,_mapList.get(_key));
            }
        }
        PageInfo pageInfo = new PageInfo(func.apply(map));
        rtn.setCode(200);
        rtn.setMessage("success");
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(pageInfo.getList())).getAsJsonArray();
        data.addProperty("total",pageInfo.getTotal());
        data.addProperty("pageNum",pageNum);
        data.addProperty("pageSize",pageSize);
        data.addProperty("paginatorId",StringUtil.isEmpty(jsonObject.get("paginatorId"))?"":jsonObject.get("paginatorId").getAsString());
        data.add("items",jsonArray);
        rtn.setFrom(jsonObject.get("from")==null?"":jsonObject.get("from").getAsString());
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    public Boolean isTokenExpire(Map map) {
        Date date1 = new Date();
        Date date2 = (Date) map.get("exptime");
        Boolean aBoolean = DateTime.compareDate(date1, date2);
        return aBoolean;
    }

    @Override
    public JsonObject verifyToken(JsonObject jsonObject, Function<Map<String, Object>, List<Map<String, Object>>> func, Function<Map<String, Object>, List<Map<String, Object>>> user) {
        Rtn rtn = new Rtn(jsonObject.get("service").getAsString());
        Map<String,Object> map = new HashMap<>();
        JsonObject data = new JsonObject();
        map = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        if (StringUtil.isEmpty(jsonObject.get("token"))) {
            rtn.setCode(50002);
            rtn.setMessage("token不符合!");
            return Func.functionRtnToJsonObject.apply(rtn);
        }
        List<Map<String, Object>> tokens = func.apply(map);
        if(tokens.size() <= 0 || StringUtil.isEmpty(jsonObject.get("token"))){
            rtn.setCode(50002);
            rtn.setMessage("token不符合!");
            return Func.functionRtnToJsonObject.apply(rtn);
        }
        if(isTokenExpire(tokens.get(0))) {
            rtn.setCode(50001);
            rtn.setMessage("error");
            return Func.functionRtnToJsonObject.apply(rtn);
        }
        if(user != null) {
            map.clear();
            map.put("token",jsonObject.get("token").getAsString());
            List<Map<String, Object>> users = user.apply(map);
            if(users.size() <= 0) {
                rtn.setCode(60003);
                rtn.setMessage("当前登录者不存在!");
                return Func.functionRtnToJsonObject.apply(rtn);
            }
            data =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(users.get(0))).getAsJsonObject();
        }
        return  data;
    }
}
