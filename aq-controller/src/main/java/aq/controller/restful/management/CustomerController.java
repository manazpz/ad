package aq.controller.restful.management;

import aq.common.access.PermissionType;
import aq.common.annotation.Permission;
import aq.common.util.HttpUtil;
import aq.service.customer.CustomerService;
import aq.service.system.SystemService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/customer")
public class CustomerController extends aq.controller.restful.System {
    @Resource
    protected CustomerService customerService;

    //添加客户
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.DATA, value = {"7496770D-6772-4CC1-9508-D07B9DD880AC"},name = {"客户-新增"})
    @ResponseBody
    public void insertCustomer(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        requestJson.addProperty("token",request.getHeader("X-Token"));
        writerJson(response,out,customerService.insertCustomer(requestJson));
    }

    //客户列表
    @ResponseBody
    @Permission(RequireLogin=true, PermissionType = PermissionType.DATA, value = {"7496770D-6772-4CC1-9508-D07B9DD880AB"},name = {"客户-查询"})
    @RequestMapping(value = "list", method=RequestMethod.GET)
    public void  customerList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception{
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,customerService.queryCustomerList(jsonObject));
    }

    //更新客户
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.DATA, value = {"7496770D-6772-4CC1-9508-D07B9DD880AA"},name = {"客户-更新"})
    @ResponseBody
    public void updateCustomer(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,customerService.updateCustomer(requestJson));
    }

    //删除客户
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.DATA, value = {"7496770D-6772-4CC1-9508-D07B9DD880AD"},name = {"客户-删除"})
    @ResponseBody
    public void deleteCustomer(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,customerService.deleteCustomer(requestJson));
    }

}
