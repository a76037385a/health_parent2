package com.app.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.app.dao.MemberDao;
import com.app.dao.OrderDao;
import com.app.dao.ReportDao;
import com.app01.Utils.DateUtils.DateUtils;
import com.app01.entity.Result;
import com.app01.msg.MessageConstant;
import com.app01.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;


@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    ReportDao reportDao;
    @Autowired
    MemberDao memberDao;
    @Autowired
    OrderDao orderDao;

    @Override
    public Integer countUserByMonth(String date) {
        return reportDao.countUserByMonth(date);
    }

    @Override
    public List<Map> countByPackageName() {
        return reportDao.countByPackageName();
    }

    @Override
    public Result getBusinessReportData() throws Exception {


        /*
        reportDate:null,
        todayNewMember :0,//今日新增用户
        totalMember :0,//全部用户数量
        thisWeekNewMember :0,//本周新增用户
        thisMonthNewMember :0,//本月新增用户

        todayOrderNumber :0,//今日订单数
        thisWeekOrderNumber :0,//本周订单数量
        thisMonthOrderNumber :0,//本月订单数量

        todayVisitsNumber :0,//今日到诊数量
        thisWeekVisitsNumber :0,//本周到诊数量
        thisMonthVisitsNumber :0,//本月到诊数量

        hotSetmeal :[
            {name:'阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐',setmeal_count:200,proportion:0.222},
            {name:'阳光爸妈升级肿瘤12项筛查体检套餐',setmeal_count:200,proportion:0.222}
        ]

         */

        Map<String, Object> map = new HashMap<>();
        //reportDate:null,今日日期
        String reportDate = DateUtils.parseDate2String(new Date());

        //获取本周一的日期
        String FirstDayOfWeek = DateUtils.parseDate2String(DateUtils.getThisWeekMonday(new Date()));
        //获取本月的日期
        String getFirstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());

        /*
        用户统计
         */
        //todayNewMember :0,//今日新增用户
        Integer todayNewMember = memberDao.countRegisteByToday(reportDate);
        //totalMember :0,//全部用户数量
        Integer totalMember = memberDao.countAllmember();
        //thisWeekNewMember :0,//本周新增用户
        Integer thisWeekNewMember = memberDao.countThisWeekNewMember(FirstDayOfWeek);
        //thisMonthNewMember :0,//本月新增用户
        Integer thisMonthNewMember = memberDao.countThisMonthNewMember(getFirstDay4ThisMonth);

         /*
            订单统计
         */
        //todayOrderNumber :0,//今日订单数
        Integer todayOrderNumber = orderDao.countTodayOrders(reportDate);
        //thisWeekOrderNumber :0,//本周订单数量
        Integer thisWeekOrderNumber = orderDao.countThisWeekOrders(FirstDayOfWeek);
        //thisMonthOrderNumber :0,//本月订单数量
        Integer thisMonthOrderNumber = orderDao.countThisMonthOrders(getFirstDay4ThisMonth);

         /*
            到诊统计
         */
        //todayVisitsNumber :0,//今日到诊数量
        Integer todayVisitsNumber = orderDao.countTodayVisits(reportDate);
        //thisWeekVisitsNumber :0,//本周到诊数量
        Integer thisWeekVisitsNumber = orderDao.countThisWeekVisits(FirstDayOfWeek);
        //thisMonthVisitsNumber :0,//本月到诊数量
        Integer thisMonthVisitsNumber = orderDao.countThisMonthVisits(getFirstDay4ThisMonth);

         /*
         hotSetmeal :[
            {name:'阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐',setmeal_count:200,proportion:0.222},
            {name:'阳光爸妈升级肿瘤12项筛查体检套餐',setmeal_count:200,proportion:0.222}
        ]
          */
        List<Map> hotSetmeal = orderDao.hotSetmeal();

        //包装
        map.put("reportDate", reportDate);
        map.put("todayNewMember", todayNewMember);
        map.put("totalMember", totalMember);
        map.put("thisWeekNewMember", thisWeekNewMember);
        map.put("thisMonthNewMember", thisMonthNewMember);

        map.put("todayOrderNumber", todayOrderNumber);
        map.put("thisWeekOrderNumber", thisWeekOrderNumber);
        map.put("thisMonthOrderNumber", thisMonthOrderNumber);

        map.put("todayVisitsNumber", todayVisitsNumber);
        map.put("thisWeekVisitsNumber", thisWeekVisitsNumber);
        map.put("thisMonthVisitsNumber", thisMonthVisitsNumber);

        map.put("hotSetmeal", hotSetmeal);


        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
    }

}
