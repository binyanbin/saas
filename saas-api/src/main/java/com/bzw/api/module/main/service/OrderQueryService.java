package com.bzw.api.module.main.service;

import com.bzw.api.module.main.biz.BranchQueryBiz;
import com.bzw.api.module.main.biz.OrderQueryBiz;
import com.bzw.api.module.main.biz.RoomQueryBiz;
import com.bzw.api.module.main.dto.OrderDTO;
import com.bzw.api.module.main.dto.OrderDetailDTO;
import com.bzw.api.module.main.enums.OrderDetailState;
import com.bzw.api.module.main.enums.OrderState;
import com.bzw.api.module.base.model.Branch;
import com.bzw.api.module.base.model.Order;
import com.bzw.api.module.base.model.OrderDetail;
import com.bzw.api.module.base.model.Room;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author yanbin
 */
@Service
public class OrderQueryService {

    @Autowired
    private OrderQueryBiz orderQueryBiz;

    @Autowired
    private BranchQueryBiz branchQueryBiz;

    @Autowired
    private RoomQueryBiz roomQueryBiz;

    public OrderDTO getOrderDto(Long orderId) {
        OrderDTO result = new OrderDTO();
        Order order = orderQueryBiz.getOrder(orderId);
        if (order == null) {
            return null;
        }
        if (order.getBranchId()!=null) {
            Branch branch = branchQueryBiz.getBranch(order.getBranchId());
            List<OrderDetail> orderDetails = orderQueryBiz.listOrderDetail(orderId);
            result.setStateId(order.getBizStatusId());
            result.setStateName(OrderState.parse(order.getBizStatusId()).getDesc());
            result.setBranchName(branch.getName());
            result.setPrice(order.getPrice());
            result.setId(order.getId());
            result.setDetails(mapToOrderDetailDto(orderDetails));
            return result;
        }
        else {
            return null;
        }
    }

    public Order getOrder(Long orderId){
        return orderQueryBiz.getOrder(orderId);
    }

    public OrderDTO getOrderByRoomId(Long roomId) {
        Room room = roomQueryBiz.getRoom(roomId);
        if (room != null && room.getOrderId() != null) {
            return getOrderDto(room.getOrderId());
        } else {
            return null;
        }
    }

    public List<OrderDetailDTO> listOrderDetail(Long orderId) {
        return mapToOrderDetailDto(orderQueryBiz.listOrderDetail(orderId));
    }

    public List<OrderDTO> listOrderByUserId(Long userId) {
        List<Order> orders = orderQueryBiz.listOrderByUserId(userId);
        if (CollectionUtils.isEmpty(orders)) {
            return Lists.newArrayList();
        }
        return listOrderDTO(orders);
    }

    public List<OrderDTO> listOrderByOpenId(String openId) {
        List<Order> orders = orderQueryBiz.listOrderByOpenId(openId);
        if (CollectionUtils.isEmpty(orders)) {
            return Lists.newArrayList();
        }
        return listOrderDTO(orders);
    }

    private List<OrderDTO> listOrderDTO(List<Order> orders) {
        List<OrderDTO> result = Lists.newArrayList();
        List<Long> orderIds = Lists.newArrayList();
        for (Order order : orders) {
            orderIds.add(order.getId());
        }
        List<OrderDetail> orderDetails = orderQueryBiz.listOrderDetail(orderIds);
        Map<Long, List<OrderDetail>> mapOrderDetail = Maps.newHashMap();
        if (CollectionUtils.isEmpty(orderDetails)) {
            return result;
        }
        orderDetails.forEach(t -> {
            if (mapOrderDetail.containsKey(t.getOrderId())) {
                List<OrderDetail> orderDetailList = mapOrderDetail.get(t.getOrderId());
                orderDetailList.add(t);
            } else {
                mapOrderDetail.put(t.getOrderId(), Lists.newArrayList(t));
            }
        });
        for (Order order : orders) {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(order.getId());
            orderDTO.setStateId(order.getBizStatusId());
            orderDTO.setStateName(OrderState.parse(order.getBizStatusId()).getDesc());
            orderDTO.setBranchName(orderDetails.get(0).getBranchName());
            orderDTO.setPrice(order.getPrice());
            List<OrderDetail> orderDetailList = mapOrderDetail.get(order.getId());
            if (!CollectionUtils.isEmpty(orderDetailList)) {
                orderDTO.setDetails(mapToOrderDetailDto(orderDetailList));
                result.add(orderDTO);
            }
        }
        return result;
    }

    private List<OrderDetailDTO> mapToOrderDetailDto(List<OrderDetail> orderDetails) {
        List<OrderDetailDTO> detailDTOList = Lists.newArrayList();
        for (OrderDetail orderDetail : orderDetails) {
            OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
            orderDetailDTO.setId(orderDetail.getId());
            orderDetailDTO.setBookTime(orderDetail.getBookTime());
            orderDetailDTO.setPrice(orderDetail.getPrice());
            orderDetailDTO.setProjectId(orderDetail.getProjectId());
            orderDetailDTO.setProjectName(orderDetail.getProjectName());
            orderDetailDTO.setRoomId(orderDetail.getRoomId());
            orderDetailDTO.setRoomName(orderDetail.getRoomName());
            orderDetailDTO.setTechnicianId(orderDetail.getTechnicianId());
            orderDetailDTO.setTechnicianName(orderDetail.getTechnicianName());
            orderDetailDTO.setStateId(orderDetail.getBizStatusId());
            orderDetailDTO.setStateName(OrderDetailState.parse(orderDetail.getBizStatusId()).getDesc());
            detailDTOList.add(orderDetailDTO);
        }
        return detailDTOList;
    }

}
