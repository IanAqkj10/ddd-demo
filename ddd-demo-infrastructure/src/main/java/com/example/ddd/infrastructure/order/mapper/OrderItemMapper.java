package com.example.ddd.infrastructure.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ddd.infrastructure.order.dataobject.OrderItemDO;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItemDO> {

    @Delete("delete from t_order_item where order_id = #{orderId}")
    void deleteByOrderId(String orderId);

    @Select("select id, order_id, product_id, product_name, quantity, sale_price, subtotal_amount from t_order_item where order_id = #{orderId}")
    List<OrderItemDO> selectByOrderId(String orderId);
}
