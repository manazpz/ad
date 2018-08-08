package aq.controller.restful.management;

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
    public void  goodsList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception{
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        jsonObject.addProperty("token",request.getHeader("X-Token"));
        writerJson(response,out,goodsService.queryGoodsList(jsonObject));
    }


    //新增商品
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    @ResponseBody
    public void createGoods(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        requestJson.addProperty("token",request.getHeader("X-Token"));
        writerJson(response,out,goodsService.insertGoods(requestJson));
    }

    //更新客户
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public void updateGoods(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        requestJson.addProperty("token",request.getHeader("X-Token"));
        writerJson(response,out,goodsService.updateGoods(requestJson));
    }

    //更新客户
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public void deleteGoods(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        requestJson.addProperty("token",request.getHeader("X-Token"));
        writerJson(response,out,goodsService.deleteGoods(requestJson));
    }

}
