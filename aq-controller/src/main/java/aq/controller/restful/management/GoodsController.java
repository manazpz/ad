package aq.controller.restful.management;

import aq.common.access.PermissionType;
import aq.common.annotation.Permission;
import aq.common.util.HttpUtil;
import aq.service.customer.CustomerService;
import aq.service.goods.GoodsService;
import aq.service.system.SystemService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/goods")
public class GoodsController extends aq.controller.restful.System {
    @Resource
    protected GoodsService goodsService;

    //商品列表
    @ResponseBody
    @RequestMapping(value = "list", method=RequestMethod.GET)
    @Permission(RequireLogin=true, PermissionType = PermissionType.DATA, value = {"7496770D-6772-4CC1-9508-D07B9DD880EA"},name = {"商品-查询"})
    public void  goodsList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception{
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,goodsService.queryGoodsList(jsonObject));
    }

    //商品下拉框
    @ResponseBody
    @RequestMapping(value = "goodslist", method=RequestMethod.GET)
    public void  dropGoodsList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception{
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,goodsService.queryGoodsList(jsonObject));
    }

    //新增商品
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.ACTION, value = {"7496770D-6772-4CC1-9508-D07B9DD880EB"},name = {"商品-新增"})
    @ResponseBody
    public void createGoods(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,goodsService.insertGoods(requestJson));
    }

    //更新客户
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.ACTION, value = {"7496770D-6772-4CC1-9508-D07B9DD880EC"},name = {"商品-更新"})
    @ResponseBody
    public void updateGoods(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,goodsService.updateGoods(requestJson));
    }

    //删除/恢复客户
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.ACTION, value = {"7496770D-6772-4CC1-9508-D07B9DD880ED"},name = {"商品-删除"})
    @ResponseBody
    public void deleteGoods(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,goodsService.deleteGoods(requestJson));
    }

}
