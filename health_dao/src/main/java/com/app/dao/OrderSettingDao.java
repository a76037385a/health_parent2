package com.app.dao;

import com.app01.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {

    void addOrderSetting(OrderSetting orderSetting);
    void updateOrderSetting(OrderSetting orderSetting);
    Integer findOrderSettingByDate(Date date);

    List<OrderSetting> getOrderSettingByMonth(@Param("startDay") String startDay,@Param("endDay") String endDay);


    void updateReservations(OrderSetting orderSetting);
}
