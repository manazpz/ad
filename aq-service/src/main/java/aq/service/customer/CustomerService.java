package aq.service.customer;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;


/**
 * Created by ywb on 2017-02-23.
 */
public interface CustomerService extends BaseService {

    //查询客户主数据
    JsonObject queryCustomerList(JsonObject jsonObject);

    //新增客户主数据
    JsonObject insertCustomer(JsonObject jsonObject);

    //更新客户主数据
    JsonObject updateCustomer(JsonObject jsonObject);

    //删除/恢复客户信息
    JsonObject deleteCustomer(JsonObject jsonObject);

}
