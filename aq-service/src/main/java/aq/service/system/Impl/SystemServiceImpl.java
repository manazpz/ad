package aq.service.system.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.serializer.SerializerRtn;
import aq.common.util.*;
import aq.dao.system.SystemDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.system.Func;
import aq.service.system.SystemService;
import com.aliyun.oss.common.utils.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.*;
import com.google.inject.spi.StaticInjectionRequest;
import com.sun.corba.se.spi.ior.IdentifiableFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceSystem")
@DyncDataSource
public class SystemServiceImpl extends BaseServiceImpl  implements SystemService  {

    @Resource
    private SystemDao sysDao;

    @Override
    public JsonObject queryLogin(JsonObject jsonObject){
        Map<String,Object> map = new HashMap<>();
        Rtn rtn = new Rtn("System");
        JsonObject data = new JsonObject();
        if (jsonObject.isJsonObject()){
            String userName = jsonObject.get("username").getAsString().trim();
            String password = jsonObject.get("password").getAsString().trim();
            if (StringUtil.isEmpty(userName)||StringUtil.isEmpty(password)){
                rtn.setCode(498);
                rtn.setMessage("用户名或密码不允许为空！");
            }else {
                map.put("userName",userName);
                List<Map<String,Object>> mapList = sysDao.selectSysLogin(map);
                if (mapList.size()<=0){
                    rtn.setCode(497);
                    rtn.setMessage("用户不存在！");
                }else {
                    //验证密码
                    String encryptPwd = MD5.getMD5String(password),
                           okPwd = mapList.get(0).get("PASSWORD").toString();
                    if (encryptPwd.equals(okPwd)){
                        LocalDateTime localDateTime = DateTime.addTime(new Date(), ChronoUnit.HOURS, 1);
                        String token = UUIDUtil.getUUID();
                        String refreshToken = UUIDUtil.getUUID();
                        map.clear();
                        map.put("administratorId",mapList.get(0).get("ID"));
                        List<Map<String, Object>> tokenList = sysDao.selectSysToken(map);
                        if(tokenList.size()>0){
                            map.clear();
                            map.put("administratorId",tokenList.get(0).get("administratorId"));
                            map.put("exptime",localDateTime);
                            map.put("token",token);
                            map.put("refreshToken",refreshToken);
                            map.put("refresh",tokenList.get(0).get("refreshToken"));
                            sysDao.updateSytToken(map);
                        }else {
                            map.clear();
                            map.put("administratorId",mapList.get(0).get("ID"));
                            map.put("exptime",localDateTime);
                            map.put("token",token);
                            map.put("refreshToken",refreshToken);
                            sysDao.insertSytToken(map);
                        }
                        rtn.setCode(200);
                        rtn.setMessage("验证通过！");
                        data = GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(map)).getAsJsonObject();
                    }else {
                        rtn.setCode(496);
                        rtn.setMessage("密码错误！");
                    }
                }
            }

        }else {
            rtn.setCode(499);
            rtn.setMessage("参数格式不正确！");
        }
        rtn.setData(data);
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject refreshToken(JsonObject jsonObject) {
        Rtn rtn = new Rtn("System");
        JsonObject data = new JsonObject();
        LocalDateTime localDateTime = DateTime.addTime(new Date(), ChronoUnit.HOURS, 1);
        String token = UUIDUtil.getUUID();
        String refreshToken = UUIDUtil.getUUID();
        Map map = new HashMap();
        map.clear();
        map.put("refresh",jsonObject.get("refreshToken").getAsString());
        List<Map> list = sysDao.selectSysToken(map);
        if(list.size()<=0){
            rtn.setCode(50002);
            rtn.setMessage("刷新token不符合!");
        }else {
            map.clear();
            map.put("administratorId",list.get(0).get("administratorId"));
            map.put("exptime",localDateTime);
            map.put("token",token);
            map.put("refreshToken",refreshToken);
            map.put("refresh",jsonObject.get("refreshToken").getAsString());
            sysDao.updateSytToken(map);
            data = GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(map)).getAsJsonObject();
            rtn.setCode(200);
            rtn.setMessage("success");
        }
        rtn.setData(data);
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject queryUserInfo(JsonObject jsonObject) {
        Rtn rtn = new Rtn("System");
        jsonObject.addProperty("service","System");
        JsonObject user = verifyToken(jsonObject,(map)->{
            return sysDao.selectSysToken(map);
        },(map)->{
            return sysDao.selectUserInfo(map);
        });
        if(!StringUtil.isEmpty(user.get("code"))){
            return user;
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        rtn.setData(user);
        return Func.functionRtnToJsonObject.apply(rtn);
    }






















    @Override
    public JsonObject queryUser(JsonObject jsonObject) {
        jsonObject.addProperty("service","System");
        return query(jsonObject,(map)->{
            return sysDao.queryUser(map);
        });
    }

    @Override
    public JsonObject insertUser(JsonObject jsonObject) {
        return null;
    }

    @Override
    public JsonObject updateUser(JsonObject jsonObject) {
        return null;
    }

    @Override
    public JsonObject deleteUser(JsonObject jsonObject) {
        return null;
    }

    @Override
    public JsonObject queryOrganization(JsonObject jsonObject) {
      jsonObject.addProperty("service","System");
      return   query(jsonObject,(map)->{
          return sysDao.queryOrganization(map);
        });
    }

    @Override
    public JsonObject queryOrganizationById(JsonObject jsonObject) {
        Rtn rtn = new Rtn(jsonObject.get("service").getAsString());
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        ArrayList list = new ArrayList();
        Map<String,Object> map = new HashMap<>();
        map.put("id",jsonObject.get("id").getAsString());
        List<Map<String,Object>> mapList = sysDao.queryOrganization(map);

        if (mapList.size()>0){
            List<Map<String,Object>> mapListC = null;
            List<Map<String,Object>> mapListD = null;
            List<Map<String,Object>> mapListP = null;
            map.clear();
            list.clear();
            String orgId = mapList.get(0).get("ORGID").toString(),parentId = null;
            switch (mapList.get(0).get("ORGTYPE").toString()){
                case "1":  //公司  上级机构
                    list.add("1");
                    map.put("orgType",list);
                    map.put("parentId","");
                    mapListC = sysDao.queryOrganization(map);
                    break;
                case "2":  //部门
                    //公司
                    list.add("1");
                    map.put("orgType",list);
                    mapListC = sysDao.queryOrganization(map);
                    //上级机构
                    list.clear();
                    list.add("2");
                    map.put("orgType",list);
                    map.put("orgId",orgId);

                    break;
                case "4":  //岗位
                    orgId = mapList.get(0).get("ORGID").toString();
                    break;
                case "8":  //人员
//                    map.clear();
//                    list.add("1");
//                    map.put("orgType",list);
//                    List<Map<String,Object>> mapListC = sysDao.queryOrganization(map);
//                    jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(mapListC)).getAsJsonArray();
//                    data.add("items_0",jsonArray);

                    break;
            }

//            map.clear();
//            list.clear();
//            list.add("2");
//            map.put("orgType",list);
//            map.put("parentId",mapList.get(0).get("ORGID"));
//            List<Map<String,Object>> mapListD = sysDao.queryOrganization(map);
//            jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(mapListD)).getAsJsonArray();
//            data.add("items_1",jsonArray);
//
//            map.clear();
//            list.clear();
//            list.add("4");
//            map.put("orgType",list);
//            map.put("parentId",mapList.get(0).get("PARENTID"));
//            List<Map<String,Object>> mapListP = sysDao.queryOrganization(map);
//            jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(mapListP)).getAsJsonArray();
//            data.add("items_2",jsonArray);

        }else {
            rtn.setCode(497);
            rtn.setMessage("用户不存在！");
        }
        rtn.setFrom(jsonObject.get("from")==null?"":jsonObject.get("from").getAsString());
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject insertOrganization(JsonObject jsonObject) {
        return null;
    }

    @Override
    public JsonObject updateOrganization(JsonObject jsonObject) {
        return null;
    }

    @Override
    public JsonObject deleteOrganization(JsonObject jsonObject) {
        return null;
    }

}
