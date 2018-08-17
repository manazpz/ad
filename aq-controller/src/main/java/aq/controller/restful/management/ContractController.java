package aq.controller.restful.management;

import aq.common.access.PermissionType;
import aq.common.annotation.Permission;
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
    @RequestMapping(value = "list", method=RequestMethod.GET)
    @Permission(RequireLogin=true, PermissionType = PermissionType.DATA, value = {"7496770D-6772-4CC2-9508-D08B8DD880DB"},name = {"合同-查询"})
    @ResponseBody
    public void  contractList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception{
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,contractService.queryContractList(jsonObject));
    }

    //新增合同
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.ACTION, value = {"7496770D-6772-4CC1-9508-D07B6DD890DC"},name = {"合同-新增"})
    @ResponseBody
    public void createContract(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,contractService.insertContract(requestJson));
    }

    //更新合同
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.ACTION, value = {"7496770D-6772-4CC1-9508-D07B8DD880DA"},name = {"合同-更新"})
    @ResponseBody
    public void updateContract(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,contractService.updateContract(requestJson));
    }

    //删除/恢复合同
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.ACTION, value = {"7496770D-6772-4CC1-9508-D07B8DD880DD"},name = {"合同-删除"})
    @ResponseBody
    public void deleteContract(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,contractService.deleteContract(requestJson));
    }

    //获取合同合作伙伴
    @RequestMapping(value = "/queryPartner",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.DATA, value = {"7496770D-6772-4CC2-9508-D08B8DD880DB"},name = {"合同-查询"})
    @ResponseBody
    public void queryContractPartner(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out)throws Exception{
        writerJson(response,out,contractService.queryContractPartner(requestJson));
    }

    //新增合同合作伙伴明细
    @RequestMapping(value = "/insertPartner",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.ACTION, value = {"7496770D-6772-4CC1-9508-D07B6DD890DC"},name = {"合同-新增"})
    @ResponseBody
    public void createContractPartner(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,contractService.insertContractPartner(requestJson));
    }

    //子合同列表
    @RequestMapping(value = "subList", method=RequestMethod.GET)
    @Permission(RequireLogin=true, PermissionType = PermissionType.DATA, value = {"7496770D-6772-4CC2-9508-D08B8DD880DB"},name = {"合同-查询"})
    @ResponseBody
    public void  contractSubList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception{
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,contractService.queryContractSubtList(jsonObject));
    }

    //新增子合同合
    @RequestMapping(value = "/insertSub",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.ACTION, value = {"7496770D-6772-4CC1-9508-D07B6DD890DC"},name = {"合同-新增"})
    @ResponseBody
    public void  createContractSub(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception{
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,contractService.insertContractSub(jsonObject));
    }


    //新增合同合作伙伴明细
    @RequestMapping(value = "/insertExpnses",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.ACTION, value = {"7496770D-6772-4CC1-9508-D07B6DD890DC"},name = {"合同-新增"})
    @ResponseBody
    public void createContractExpnses(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,contractService.insertContractExpnses(jsonObject));
    }

    //获取合同收支明细
    @RequestMapping(value = "/queryExpnses",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.DATA, value = {"7496770D-6772-4CC2-9508-D08B8DD880DB"},name = {"合同-查询"})
    @ResponseBody
    public void queryContractExpnses(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out)throws Exception{
        writerJson(response,out,contractService.queryContractExpnses(requestJson));
    }

}
