package aq.dao.contract;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoContract")
public interface ContractDao {

    //获取合同数据
    List<Map<String,Object>> selectContracList(Map<String,Object> map);

    //获取合同数据
    List<Map<String,Object>> selectContractParList(Map<String,Object> map);

    //新增合同
    int insertContract(Map<String,Object> map);

    //新增合同
    int insertGoods(Map<String,Object> map);


    //修改合同
    int updateContract(Map<String,Object> map);

    //删除/恢复合同信息
    int deleteContract(Map<String,Object> map);

    //新增合同收支明细
    int insertContractExpenses(Map<String,Object> map);

    //編輯合同收支明細
    int updateContractExpenses(Map<String,Object> map);

    //获取收支明细数据
    List<Map<String,Object>> selectContracExpenses(Map<String,Object> map);

    //获取合同合伙人明细
    List<Map<String,Object>> selectContracPartner(Map<String,Object> map);

    //删除合同合伙人明细
    int deleteContractPartner(Map<String,Object> map);

    //新增合同合伙人明细
    int insertContractPartner(Map<String,Object> map);

    //获取子合同数据
    List<Map<String,Object>> selectContractSubList(Map<String,Object> map);

    //获取子合同详情
    List<Map<String,Object>> selectContractSubListMain(Map<String,Object> map);

    //获取子合同合明细
    List<Map<String,Object>> selectContracSub(Map<String,Object> map);

    //新增父子合同关系表
    int insertContractSub(Map<String,Object> map);


    //修改父子合同关系表
    int updateContractPartner(Map<String,Object> map);


    //删除父子合同关系表
    int deleteContracExpenses(Map<String,Object> map);

    //获取合同收支明细
    List<Map<String,Object>> selectContractExpnses(Map<String,Object> map);

    //新增附件
    int insertAttachment(Map<String,Object> map);

    //获取商品信息
    List<Map<String,Object>> selectContractGoodList(Map<String,Object> map);


    //删除合同商品信息
    int deleteContractGoodList(Map<String,Object> map);

    //删除合同商品信息
    int insertDelAudit(Map<String,Object> map);

    //删除申请查询
    List<Map<String,Object>> selectdelAuditList(Map<String,Object> map);

    //删除申请审批
    int updateAudit(Map<String,Object> map);

}
