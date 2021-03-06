package aq.controller.restful.management;

import aq.common.access.PermissionType;
import aq.common.annotation.Permission;
import aq.common.other.Rtn;
import aq.common.util.*;
import aq.service.contract.ContractService;
import aq.service.customer.CustomerService;
import aq.service.goods.GoodsService;
import aq.service.system.Func;
import aq.service.system.SystemService;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;


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
        if(request.getAttribute("flag")== "admin"){
            jsonObject.addProperty("flag","admin");
        }
        writerJson(response,out,contractService.queryContractList(jsonObject));
    }
    //子合同列表
    @RequestMapping(value = "parlist", method=RequestMethod.GET)
    @Permission(RequireLogin=true, PermissionType = PermissionType.DATA, value = {"7496770D-6772-4CC2-9508-D08B8DD880DB"},name = {"合同-查询"})
    @ResponseBody
    public void  contractParList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception{
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,contractService.queryContractParList(jsonObject));
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
    @RequestMapping(value = "/queryPartner",method = RequestMethod.GET)
    @Permission(RequireLogin=true, PermissionType = PermissionType.DATA, value = {"7496770D-6772-4CC2-9508-D08B8DD880DB"},name = {"合同-查询"})
    @ResponseBody
    public void  queryContractPartner(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception{
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        if(request.getAttribute("flag")== "admin"){
            jsonObject.addProperty("flag","admin");
        }
        writerJson(response,out,contractService.queryContractPartner(jsonObject));
    }

    //删除合同合作伙伴
    @RequestMapping(value = "/deletePartner",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.ACTION, value = {"7496770D-6772-4CC1-9508-D07B8DD880DD"},name = {"合同-删除"})
    @ResponseBody
    public void deleteContractPartner(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out)throws Exception{
        writerJson(response,out,contractService.deleteContractPartner(requestJson));
    }

    //修改合同合作伙伴
    @RequestMapping(value = "/updatePartner",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.ACTION, value = {"7496770D-6772-4CC1-9508-D07B8DD880DA"},name = {"合同-更新"})
    @ResponseBody
    public void updatePartner(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out)throws Exception{
        writerJson(response,out,contractService.updateContractPartner(requestJson));
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
    //子合同详情
    @RequestMapping(value = "subListMain", method=RequestMethod.GET)
    @Permission(RequireLogin=true, PermissionType = PermissionType.DATA, value = {"7496770D-6772-4CC2-9508-D08B8DD880DB"},name = {"合同-查询"})
    @ResponseBody
    public void  contractSubListMain(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception{
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,contractService.queryContractSubtListMain(jsonObject));
    }

    //新增合同收支明细
    @RequestMapping(value = "/insertExpnses",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.ACTION, value = {"7496770D-6772-4CC1-9508-D07B6DD890DC"},name = {"合同-新增"})
    @ResponseBody
    public void createContractExpnses(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,contractService.insertContractExpnses(requestJson));
    }

    //获取合同收支明细
    @RequestMapping(value = "/queryExpnses",method = RequestMethod.GET)
    @Permission(RequireLogin=true, PermissionType = PermissionType.DATA, value = {"7496770D-6772-4CC2-9508-D08B8DD880DB"},name = {"合同-查询"})
    @ResponseBody
    public void queryContractExpnses(HttpServletRequest request, HttpServletResponse response, PrintWriter out)throws Exception{
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,contractService.queryContractExpnses(jsonObject));
    }


    //修改合同收支明细
    @RequestMapping(value = "/updateExpnss",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.ACTION, value = {"7496770D-6772-4CC1-9508-D07B8DD880DA"},name = {"合同-更新"})
    @ResponseBody
    public void updateExpnss(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,contractService.updateContractExpnses(requestJson));
    }


    //删除合同收支明细
    @RequestMapping(value = "/deletePay",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.ACTION, value = {"7496770D-6772-4CC1-9508-D07B8DD880DD"},name = {"合同-删除"})
    @ResponseBody
    public void deletePay(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,contractService.deleteContractExpnses(requestJson));
    }


    //获取合同商品明细
    @RequestMapping(value = "/contractGoodList",method = RequestMethod.GET)
    @Permission(RequireLogin=true, PermissionType = PermissionType.DATA, value = {"7496770D-6772-4CC2-9508-D08B8DD880DB"},name = {"合同-查询"})
    @ResponseBody
    public void queryContractGoodList(HttpServletRequest request, HttpServletResponse response, PrintWriter out)throws Exception{
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,contractService.queryContractGoodList(jsonObject));
    }


    //获取合同商品明细
    @RequestMapping(value = "/deleteGoods",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.DATA, value = {"7496770D-6772-4CC2-9508-D08B8DD880DB"},name = {"合同-查询"})
    @ResponseBody
    public void deleteGoods(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out)throws Exception{
        writerJson(response,out,contractService.deleteContractGoodList(requestJson));
    }


    //上传附件
    @RequestMapping(value = "/uploadFile",method = RequestMethod.POST)
    @ResponseBody
    public void uploadFile(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        Rtn rtn = new Rtn("User");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile haedImg = multipartRequest.getFile("file");
        FileUpload fileUpload = new FileUpload();
        String suffix = haedImg.getOriginalFilename().substring(haedImg.getOriginalFilename().lastIndexOf(".") + 1) ;
        String name = haedImg.getOriginalFilename().substring(0,haedImg.getOriginalFilename().lastIndexOf(".") );
        String url = fileUpload.upload(haedImg, "", suffix,"", request);
        String size = FileUpload.FormetFileSize(haedImg.getSize());
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("url", url);
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("size", size);
        jsonObject.addProperty("suffix", suffix);
        rtn.setCode(200);
        rtn.setMessage("success");
        rtn.setData(jsonObject);
        writerJson(response,out, Func.functionRtnToJsonObject.apply(rtn));
    }

    //删除附件
    @RequestMapping(value = "/deleteAttachment",method = RequestMethod.POST)
    @ResponseBody
    public void deleteAttachment(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        Rtn rtn = new Rtn("Contract");
        Map<String,Object> res = GsonHelper.getInstance().fromJson(requestJson,Map.class);
        List mapfile = (List) res.get("file");
        for(int i = 0; i < mapfile.size(); i++){
            File file = new File(mapfile.get(i).toString());
            if (file.isFile() && file.exists()) {
                file.delete();
            }
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        writerJson(response,out, Func.functionRtnToJsonObject.apply(rtn));
    }


    //删除附件
    @RequestMapping(value = "/deleteAtta",method = RequestMethod.POST)
    @Permission(RequireLogin=true, PermissionType = PermissionType.ACTION, value = {"7496770D-6772-4CC1-9508-D07B8DD880DD"},name = {"合同-删除"})
    @ResponseBody
    public void deleteAtta(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,contractService.deleteAtta(requestJson));
    }

    //获取合同附件列表
    @RequestMapping(value = "/queryAtta",method = RequestMethod.GET)
    @Permission(RequireLogin=true, PermissionType = PermissionType.DATA, value = {"7496770D-6772-4CC2-9508-D08B8DD880DB"},name = {"合同-查询"})
    @ResponseBody
    public void queryAtta(HttpServletRequest request, HttpServletResponse response, PrintWriter out)throws Exception{
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,contractService.queryContractAttaList(jsonObject));
    }

    //读取资源文件
    @RequestMapping(value = "/getReasourse",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> getReasourse(HttpServletRequest request, HttpServletResponse response)throws Exception{
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        String fileName = jsonObject.get("name").getAsString() + "." + jsonObject.get("extend").getAsString();
        File file = new File(jsonObject.get("url").getAsString());

        HttpServletRequest requests = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String header = requests.getHeader("User-Agent").toUpperCase();
        HttpStatus status = HttpStatus.CREATED;
        try {
            if (header.contains("MSIE") || header.contains("TRIDENT") || header.contains("EDGE")) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
                fileName = fileName.replace("+", "%20");    // IE下载文件名空格变+号问题
                status = HttpStatus.OK;
            } else {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            }
        } catch (UnsupportedEncodingException e) {}

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentLength(file.length());
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, status);
    }

    //删除附件
    @RequestMapping(value = "/createCancelApp",method = RequestMethod.POST)
    @ResponseBody
    public void createCancelApp(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,contractService.insertCancelApp(requestJson));
    }

    //查询删除申请
    @RequestMapping(value = "/delAuditList",method = RequestMethod.GET)
    @ResponseBody
    public void delAuditList(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,contractService.selectdelAuditList(jsonObject));
    }

    //删除申请审批
    @RequestMapping(value = "/updateAudit",method = RequestMethod.POST)
    @ResponseBody
    public void updateAudit(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,contractService.updateAudit(requestJson));
    }
}
