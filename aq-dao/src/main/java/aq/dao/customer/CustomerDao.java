package aq.dao.customer;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoCustomer")
public interface CustomerDao {

    //获取客户主数据
    List<Map<String,Object>> selectCustomerList(Map<String,Object> map);

    //插入客户数据
    int insertCustomer(Map<String,Object> map);

    //更新客户信息
    int updateCustomer(Map<String,Object> map);

    //删除/恢复客户信息
    int deleteCustomer(Map<String,Object> map);

}
