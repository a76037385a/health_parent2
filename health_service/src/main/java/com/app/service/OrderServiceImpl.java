package com.app.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.app.dao.MemberDao;
import com.app.dao.OrderDao;
import com.app.dao.OrderSettingDao;
import com.app01.Utils.DateUtils.DateUtils;
import com.app01.entity.Result;
import com.app01.msg.MessageConstant;
import com.app01.pojo.Member;
import com.app01.pojo.Order;
import com.app01.pojo.OrderSetting;
import com.app01.service.OrderService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderSettingDao orderSettingDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    MemberDao memberDao;


    @Override
    public Result submitOrder(Map map) throws Exception {

        //根据日期查询后台设置
        OrderSetting orderSetting = orderDao.findOrderNumberByDate(DateUtils.parseString2Date((String) map.get("orderDate")));
        //根据手机查询用户
        Member member = memberDao.findMemberByPhoneNumber((String) map.get("telephone"));
        //后台没有设置日期直接返回
        if(orderSetting == null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //后台预约已满直接返回
        if(orderSetting.getReservations() >= orderSetting.getNumber()){
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        //判断用户是否存在
        Result result = isRegiste(map,member,orderSetting);

        return result;
    }

    @Override
    public Map findOrderById(int id) {
        Map order = orderDao.findOrderById(id);
        return order;
    }


    //更新数量并新增预约
    public Integer addNewOrder(Map map, Integer memberId,OrderSetting orderSetting) throws Exception {
        //更新数量
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.updateReservations(orderSetting);

        //新增预定
        Order order = new Order();
        order.setMemberId(memberId);
        order.setOrderDate(DateUtils.parseString2Date((String) map.get("orderDate")));
        order.setOrderType((String) map.get("orderType"));
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setPackageId(Integer.parseInt((String) map.get("setmealId")));
        orderDao.addNewOrder(order);
        return order.getId();

    }

    public Result isRegiste(Map map, Member member,OrderSetting orderSetting) throws Exception {
        //用户不存在
        if(member == null){
            //添加用户,直接新增预约
            Integer newMemberId = goRegiste(map);
            Integer newOrderID = addNewOrder(map,newMemberId,orderSetting);
            return new Result(true,MessageConstant.ORDER_SUCCESS,newOrderID);
        }else{//用户存在

            Order order = new Order();
            //设置memberid
            order.setMemberId(member.getId());
            order.setOrderDate(DateUtils.parseString2Date((String) map.get("orderDate")));
            order.setPackageId(Integer.parseInt((String) map.get("setmealId")));
            order.setOrderType(null);
            order.setOrderStatus(null);

            //查询预约
            Order memberOrder = orderDao.findMemberOrderByOrder(order);
            if (memberOrder != null){
                //预约存在 直接返回
                return new Result(false,MessageConstant.HAS_ORDERED,memberOrder.getId());
            }else{
                //预约不存在 ,添加预约
                Integer newOrderid = addNewOrder(map,member.getId(),orderSetting);
                return new Result(true,MessageConstant.ORDER_SUCCESS,newOrderid);
            }

        }
    }

    //注册新用户
    public Integer goRegiste(Map map) throws Exception {
        Member member = new Member();
        member.setRegTime(DateUtils.parseString2Date((String) map.get("orderDate")));
        member.setPhoneNumber((String) map.get("telephone"));
        member.setIdCard((String) map.get("idCard"));
        member.setName((String) map.get("name"));
        member.setSex((String) map.get("sex"));
        memberDao.addMember(member);
        //返回新增memberID
        return member.getId();
    }


    //@Override
    //public Result submitOrder(Map map) throws Exception {//原版

    //System.out.println("get in order service");
    //String orderDate = (String) map.get("orderDate");
    //Date date = DateUtils.parseString2Date( orderDate );
    //String telephone = (String) map.get("telephone");

    //是否有此项设置
    //OrderSetting orderSetting = orderDao.findOrderNumberByDate(date);
    //if(orderSetting == null){
    //    return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
    //}

    //预约是否已满
    //if(orderSetting.getReservations() >= orderSetting.getNumber()){
    //    return new Result(false, MessageConstant.ORDER_FULL);
    //}

    //是否注册
    //Member member = memberDao.findMemberByPhoneNumber(telephone);
    //已经注册
    //if(member != null){
    //    //用户日期是否重复预约
    //    Integer setmealId = Integer.parseInt((String) map.get("setmealId")) ;
    //    Order order = new Order( member.getId(),date,null,null,setmealId);
    //    List<Order> orders = orderDao.findOrderByMemberId(order);
    //    if(orders.size()>0 || orders != null){
    //        return new Result(false,MessageConstant.HAS_ORDERED);
    //    }
    //}


    //未注册
    //Member member1 = new Member();
    //if(member == null){
    //    System.out.println("未注册");
    //    //继续运行
    //    //注册用户
    //    member1.setName((String) map.get("name"));
    //    member1.setPhoneNumber(telephone);
    //    member1.setRegTime(date);
    //    member1.setSex((String) map.get("sex"));
    //    member1.setIdCard((String) map.get("idCard"));
    //    memberDao.addMember(member1);
    //    System.out.println("addMember After id ="+member1.getId());
    //}
    //更新预约人数
    //orderSetting.setReservations(orderSetting.getReservations()+1);
    //orderSettingDao.updateReservations(orderSetting);

    //新增预约
    //方式二
    //addNewOrder(map,member,orderSetting);

    //方式一
    //Order order = new Order();
    //order.setMemberId(member1.getId());
    //order.setOrderDate(date);
    //order.setOrderType((String) map.get("orderType"));
    //order.setOrderStatus(Order.ORDERSTATUS_NO);
    //order.setPackageId(Integer.parseInt((String) map.get("setmealId")));
    //orderDao.addNewOrder(order);

    //return new Result(true,MessageConstant.ORDER_SUCCESS);
    //}




}
