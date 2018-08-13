package aq.dao.contract;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoContract")
public interface ContractDao {

    //获取合同数据
    List<Map<String,Object>> selectContracList(Map<String,Object> map);

    //新增合同
    int insertContract(Map<String,Object> map);

    //修改合同
    int updateContract(Map<String,Object> map);

    //删除/恢复合同信息
    int deleteContract(Map<String,Object> map);

    //获取TABS数据
    List<Map<String,Object>> selectContracTabs(Map<String,Object> map);

    //新增合同收支明细
    int insertContractExpenses(Map<String,Object> map);

    //获取收支明细数据
    List<Map<String,Object>> selectContracExpenses(Map<String,Object> map);


}
