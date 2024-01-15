package com.example.onlinemarket.domain.order.mapper;

import com.example.onlinemarket.domain.order.domain.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper {

    Long insert(OrderDetail orderDetail);
}
