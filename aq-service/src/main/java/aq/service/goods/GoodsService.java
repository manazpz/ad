package aq.service.goods;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;


/**
 * Created by ywb on 2017-02-23.
 */
public interface GoodsService extends BaseService {

    //查询商品
    JsonObject queryGoodsList(JsonObject jsonObject);

    //新增商品
    JsonObject insertGoods(JsonObject jsonObject);

    //更新商品
    JsonObject updateGoods(JsonObject jsonObject);

    //删除/恢复商品
    JsonObject deleteGoods(JsonObject jsonObject);


}
