package aq.controller.restful.management;

import aq.common.util.HttpUtil;
import aq.service.system.UserService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/user")
public class UserController extends aq.controller.restful.System {
    @Resource
    protected UserService userService;

    //添加用户
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    @ResponseBody
    public void insertUser(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        requestJson.addProperty("token",request.getHeader("X-Token"));
        writerJson(response,out,userService.insertUserInfo(requestJson));
    }

    //用户列表
    @ResponseBody
    @RequestMapping(value = "list", method=RequestMethod.GET)
    public void  userList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception{
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        jsonObject.addProperty("token",request.getHeader("X-Token"));
        writerJson(response,out,userService.queryUserInfo(jsonObject));
    }

    //更新用户
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public void updateUser(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        requestJson.addProperty("token",request.getHeader("X-Token"));
        writerJson(response,out,userService.updateUserInfo(requestJson));
    }

    //删除用户
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public void deleteUser(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        requestJson.addProperty("token",request.getHeader("X-Token"));
        writerJson(response,out,userService.deleteUser(requestJson));
    }

}
