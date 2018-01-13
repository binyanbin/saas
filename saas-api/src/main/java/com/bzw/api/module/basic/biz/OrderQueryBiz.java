package com.bzw.api.module.basic.biz;

import com.bzw.api.module.basic.dao.OrderDetailMapper;
import com.bzw.api.module.basic.dao.OrderMapper;
import com.bzw.api.module.basic.model.Order;
import com.bzw.api.module.basic.model.OrderDetail;
import com.bzw.api.module.basic.model.OrderDetailExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderQueryBiz {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;


    public Order getOrder(Long orderId){
        return orderMapper.selectByPrimaryKey(orderId);
    }

    public List<OrderDetail> listOrderDetail(Long orderId){
        OrderDetailExample orderDetailExample = new OrderDetailExample();
        orderDetailExample.createCriteria().andOrderIdEqualTo(orderId);
        return orderDetailMapper.selectByExample(orderDetailExample);
    }

}
