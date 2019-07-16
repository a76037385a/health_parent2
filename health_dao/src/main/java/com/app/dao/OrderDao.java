package com.app.dao;

import com.app01.pojo.Order;
import com.app01.pojo.OrderSetting;
import org.aspectj.weaver.ast.Or;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderDao {

    OrderSetting findOrderNumberByDate(Date date);

    List<Order> findOrderByMemberId(Order order);

    Order findMemberOrderByOrder(Order order);

    void addNewOrder(Order order);

    Map findOrderById(int id);

    Integer countTodayOrders(String date);
    Integer countThisWeekOrders(String weekFirstDay);
    Integer countThisMonthOrders(String monthFirstDay);


    Integer countTodayVisits(String date);
    Integer countThisWeekVisits(String weekFirstDay);
    Integer countThisMonthVisits(String monthFirstDay);

    List<Map> hotSetmeal();



}
