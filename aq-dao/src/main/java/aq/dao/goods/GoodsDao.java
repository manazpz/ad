package aq.dao.goods;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoGoods")
public interface GoodsDao {

    //获取商品数据
    List<Map<String,Object>> selectGoodsList(Map<String,Object> map);

    //新增商品
    int insertGoods(Map<String,Object> map);

    //修改商品
    int updateGoods(Map<String,Object> map);

    //删除/恢复商品信息
    int deleteGoods(Map<String,Object> map);
}
