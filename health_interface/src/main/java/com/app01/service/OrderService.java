package com.app01.service;

import com.app01.entity.Result;
import com.app01.pojo.Order;

import java.util.Map;

public interface OrderService {

    Result submitOrder(Map map) throws Exception;

    Map findOrderById(int id);
}
