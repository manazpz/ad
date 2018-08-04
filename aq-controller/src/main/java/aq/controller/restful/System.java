package aq.controller.restful;

import aq.common.other.Rtn;
import aq.common.util.HttpUtil;
import aq.common.util.StringUtil;
import aq.service.system.Func;
import aq.service.system.SystemService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/sys")
public class System extends Base {

    @Resource
    protected SystemService systemService;

    //登录
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public void login(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
         writerJson(response,out,systemService.queryLogin(requestJson));
    }

    //登录
    @RequestMapping(value = "/loginOut",method = RequestMethod.POST)
    @ResponseBody
    public void loginOut(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        Rtn rtn = new Rtn("System");
        rtn.setCode(200);
        rtn.setMessage("登出成功！");
        writerJson(response,out, Func.functionRtnToJsonObject.apply(rtn));
    }

    //刷新token
    @RequestMapping(value = "/refreshToken",method = RequestMethod.POST)
    @ResponseBody
    public void refreshToken(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,systemService.refreshToken(requestJson));
    }

    //region 用户
    @RequestMapping(value = "/user/info",method = RequestMethod.GET)
    public void userInfo(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,systemService.queryUserInfo(HttpUtil.getParameterMap(request)));
    }
    //endregion












    //region 用户
    //查询
    @RequestMapping(value = "/user/get/{param}",method = RequestMethod.GET)
    @ResponseBody
    public void userGet(@PathVariable(value = "param") String requestStr, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,systemService.queryUser(StringUtil.toJsonObject(requestStr)));
    }

    //新增
    @RequestMapping(value = "/user/put",method = RequestMethod.POST)
    @ResponseBody
    public void userPut(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,systemService.insertUser(requestJson));
    }

    //修改
    @RequestMapping(value = "/user/post",method = RequestMethod.POST)
    @ResponseBody
    public void userPost(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,systemService.updateUser(requestJson));
    }

    //删除
    @RequestMapping(value = "/user/delete",method = RequestMethod.POST)
    @ResponseBody
    public void userDelete(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,systemService.deleteUser(requestJson));
    }
    //endregion

    //region 组织
    //查询
    @RequestMapping(value = "/organization/get/{param}",method = RequestMethod.GET)
    @ResponseBody
    public void organizationGet(@PathVariable(value = "param") String requestStr, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
            writerJson(response,out,systemService.queryOrganization(StringUtil.toJsonObject(requestStr)));
    }

    //组织-查询-根据ID
    @RequestMapping(value = "/organization/expand/get/{param}",method = RequestMethod.GET)
    @ResponseBody
    public void organizationGetById(@PathVariable(value = "param") String param, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,systemService.queryOrganizationById(StringUtil.toJsonObject(param)));
    }
    //endregion


}
