package aq.service.contract;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;


/**
 * Created by ywb on 2017-02-23.
 */
public interface ContractService extends BaseService {

    //查询商品
    JsonObject queryContractList(JsonObject jsonObject);

    //新增合同
    JsonObject insertContract(JsonObject jsonObject);

    //更新商品
    JsonObject updateContract(JsonObject jsonObject);

    //删除/恢复商品
    JsonObject deleteContract(JsonObject jsonObject);

    //获取合作伙伴明细
    JsonObject queryContractPartner(JsonObject jsonObject);

    //新增合同合作伙伴明细
    JsonObject insertContractPartner(JsonObject jsonObject);

    //查询商品
    JsonObject queryContractSubtList(JsonObject jsonObject);

    //新增合同
    JsonObject insertContractSub(JsonObject jsonObject);

    //新增合同
    JsonObject insertContractExpnses(JsonObject jsonObject);

    //获取合作收支明细
    JsonObject queryContractExpnses(JsonObject jsonObject);




}
