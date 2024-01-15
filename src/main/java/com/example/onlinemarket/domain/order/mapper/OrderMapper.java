package com.example.onlinemarket.domain.order.mapper;

import com.example.onlinemarket.domain.order.domain.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    Long insert(Order order);
}
