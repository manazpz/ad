package aq.controller.restful.management;

import aq.common.util.HttpUtil;
import aq.service.contract.ContractService;
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
@RequestMapping("/contract")
public class ContractController extends aq.controller.restful.System {
    @Resource
    protected ContractService contractService;

    //合同列表
    @ResponseBody
    @RequestMapping(value = "list", method=RequestMethod.GET)
    public void  contractList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception{
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        jsonObject.addProperty("token",request.getHeader("X-Token"));
        writerJson(response,out,contractService.queryContractList(jsonObject));
    }

    //新增合同
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    @ResponseBody
    public void createContract(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        requestJson.addProperty("token",request.getHeader("X-Token"));
        writerJson(response,out,contractService.insertContract(requestJson));
    }

    //更新合同
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public void updateContract(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        requestJson.addProperty("token",request.getHeader("X-Token"));
        writerJson(response,out,contractService.updateContract(requestJson));
    }

    //删除/恢复合同
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public void deleteContract(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        requestJson.addProperty("token",request.getHeader("X-Token"));
        writerJson(response,out,contractService.deleteContract(requestJson));
    }

}
