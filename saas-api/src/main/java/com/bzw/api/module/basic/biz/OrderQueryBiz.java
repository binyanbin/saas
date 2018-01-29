package com.bzw.api.module.basic.biz;

import com.bzw.api.module.basic.dao.OrderDetailMapper;
import com.bzw.api.module.basic.dao.OrderMapper;
import com.bzw.api.module.basic.model.Order;
import com.bzw.api.module.basic.model.OrderDetail;
import com.bzw.api.module.basic.model.OrderDetailExample;
import com.bzw.api.module.basic.model.OrderExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @author yanbin
 */
@Service
public class OrderQueryBiz {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;


    public Order getOrder(Long orderId){
        return orderMapper.selectByPrimaryKey(orderId);
    }

    public OrderDetail getOrderDetail(Long detailId){
        return orderDetailMapper.selectByPrimaryKey(detailId);
    }

    public List<OrderDetail> listOrderDetail(Long orderId){
        OrderDetailExample orderDetailExample = new OrderDetailExample();
        orderDetailExample.createCriteria().andOrderIdEqualTo(orderId);
        return orderDetailMapper.selectByExample(orderDetailExample);
    }

    public List<Order> listOrderByUserId(Long userId){
        OrderExample orderExample = new OrderExample();
        orderExample.createCriteria().andUserIdEqualTo(userId);
        return orderMapper.selectByExample(orderExample);
    }

    public List<Order> listOrderByOpenId(String openId){
        OrderExample orderExample = new OrderExample();
        orderExample.createCriteria().andWechatIdEqualTo(openId);
        return orderMapper.selectByExample(orderExample);
    }

    public List<OrderDetail> listOrderDetail(List<Long> detailIds){
        OrderDetailExample orderDetailExample = new OrderDetailExample();
        orderDetailExample.createCriteria().andOrderIdIn(detailIds);
        return orderDetailMapper.selectByExample(orderDetailExample);
    }

}
