package com.app.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.app.dao.OrderSettingDao;
import com.app01.entity.Result;
import com.app01.pojo.Order;
import com.app01.pojo.OrderSetting;
import com.app01.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService{

    @Autowired
    OrderSettingDao orderSettingDao;

    @Override
    public void addOrderSetting(List<OrderSetting> list) {
        if(list != null && list.size()>0){
            for (OrderSetting orderSetting : list) {
                Integer dateCount = orderSettingDao.findOrderSettingByDate(orderSetting.getOrderDate());
                System.out.println(dateCount);
                if (dateCount>0){
                    orderSettingDao.updateOrderSetting(orderSetting);
                }else{
                    orderSettingDao.addOrderSetting(orderSetting);
                }
            }
        }
    }

    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        String startDay= date + "-1";
        String endDay = date + "-31";
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(startDay,endDay);
        List<Map> maps = new ArrayList<>();
        if(list != null && list.size()>0){
            for (OrderSetting orderSetting : list) {
                Map map = new HashMap<>();
                map.put("id",orderSetting.getId());
                map.put("date",orderSetting.getOrderDate().getDate());
                map.put("number",orderSetting.getNumber());
                map.put("reservations",orderSetting.getReservations());
                maps.add(map);
            }
        }

        return maps;
    }

    @Override
    @Transactional
    public void setOrderByNumber(OrderSetting orderByNumber) {
        List<OrderSetting> list = new ArrayList<>();
        list.add(orderByNumber);
        addOrderSetting(list);
    }


}
