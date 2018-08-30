package aq.service.contract;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;


/**
 * Created by ywb on 2017-02-23.
 */
public interface ContractService extends BaseService {

    //查询合同列表
    JsonObject queryContractList(JsonObject jsonObject);

    //查询合同列表
    JsonObject queryContractParList(JsonObject jsonObject);

    //新增合同
    JsonObject insertContract(JsonObject jsonObject);

    //更新商品
    JsonObject updateContract(JsonObject jsonObject);

    //删除/恢复商品
    JsonObject deleteContract(JsonObject jsonObject);

    //获取合作伙伴明细
    JsonObject queryContractPartner(JsonObject jsonObject);

    //删除合作伙伴明细
    JsonObject deleteContractPartner(JsonObject jsonObject);

    //更新合作伙伴明细
    JsonObject updateContractPartner(JsonObject jsonObject);

    //新增合同合作伙伴明细
    JsonObject insertContractPartner(JsonObject jsonObject);

    //查询子合同
    JsonObject queryContractSubtList(JsonObject jsonObject);

    //查询子合同详情
    JsonObject queryContractSubtListMain(JsonObject jsonObject);

    //新增合同
    JsonObject insertContractExpnses(JsonObject jsonObject);

    //获取合同收支明细
    JsonObject queryContractExpnses(JsonObject jsonObject);

    //修改合同收支明细
    JsonObject updateContractExpnses(JsonObject jsonObject);

    //修改合同收支明细
    JsonObject deleteContractExpnses(JsonObject jsonObject);

    //获取合作收支明细
    JsonObject queryContractGoodList(JsonObject jsonObject);

    //获取合作收支明细
    JsonObject deleteContractGoodList(JsonObject jsonObject);

    //查询合同附件列表
    JsonObject queryContractAttaList(JsonObject jsonObject);

    //刪除附件
    JsonObject deleteAtta(JsonObject jsonObject);




}
