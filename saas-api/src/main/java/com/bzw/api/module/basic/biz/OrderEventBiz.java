package com.bzw.api.module.basic.biz;

import com.bzw.api.module.basic.dao.OrderDetailMapper;
import com.bzw.api.module.basic.dao.OrderMapper;
import com.bzw.api.module.basic.model.Order;
import com.bzw.api.module.basic.model.OrderDetail;
import com.bzw.api.module.basic.model.OrderDetailExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yanbin
 */
@Service
public class OrderEventBiz {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    public void add(Order order) {
        orderMapper.insert(order);
    }

    public void addOrderDetails(List<OrderDetail> orderDetails){
        for (OrderDetail orderDetail : orderDetails) {
            orderDetailMapper.insert(orderDetail);
        }
    }

    public void updateOrderDetails(List<OrderDetail> orderDetails){
        for (OrderDetail orderDetail :orderDetails){
            orderDetailMapper.updateByPrimaryKeySelective(orderDetail);
        }
    }

    public void updateOrderDetail(OrderDetail orderDetail){
        orderDetailMapper.updateByPrimaryKeySelective(orderDetail);
    }

    public void update(Order order){
        orderMapper.updateByPrimaryKeySelective(order);
    }

    public void deleteById(Long id) {
        OrderDetailExample orderDetailExample = new OrderDetailExample();
        orderDetailExample.createCriteria().andOrderIdEqualTo(id);
        orderDetailMapper.deleteByExample(orderDetailExample);
    }


}
