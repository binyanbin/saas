package com.bzw.api.module.basic.service;

import com.bzw.api.module.basic.biz.*;
import com.bzw.api.module.basic.dto.*;
import com.bzw.api.module.basic.enums.OrderState;
import com.bzw.api.module.basic.enums.ProjectType;
import com.bzw.api.module.basic.enums.RoomState;
import com.bzw.api.module.basic.enums.TechnicianState;
import com.bzw.api.module.basic.model.*;
import com.bzw.api.module.platform.model.User;
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
public class CustomerQueryService {

    @Autowired
    private RoomQueryBiz roomQueryBiz;

    @Autowired
    private OrderQueryBiz orderQueryBiz;

    @Autowired
    private ProjectQueryBiz projectQueryBiz;

    @Autowired
    private TechnicianQueryBiz technicianQueryBiz;

    @Autowired
    private UserQueryBiz userQueryBiz;

    public BranchDTO getBranchByRoomId(Long roomId) {
        Branch branch = roomQueryBiz.getBranchByRoomId(roomId);
        if (branch == null) {
            return null;
        } else {
            BranchDTO result = new BranchDTO();
            result.setId(branch.getId());
            result.setAddress(branch.getAddress());
            result.setName(branch.getName());
            result.setTelephone(branch.getTelephone());
            return result;
        }
    }

    public Boolean containPhone(String phone){
        List<User> users = userQueryBiz.listUserByPhone(phone);
        return !CollectionUtils.isEmpty(users);
    }

    public List<ProjectDTO> listProjectsByBranchId(Long branchId) {
        List<Project> projectList = projectQueryBiz.listProjectByBranchId(branchId);
        return mapToProjectDto(projectList);
    }

    public List<ProjectDTO> listProjectsByRoomId(Long roomId) {
        List<Project> projectList = projectQueryBiz.listProjectsByRoomId(roomId);
        return mapToProjectDto(projectList);
    }

    private List<ProjectDTO> mapToProjectDto(List<Project> projectList) {
        List<ProjectDTO> result = Lists.newArrayList();
        for (Project project : projectList) {
            ProjectDTO projectDTO = new ProjectDTO();
            projectDTO.setId(project.getId());
            projectDTO.setName(project.getName());
            projectDTO.setTypeName(ProjectType.parse(project.getType()).getDesc());
            projectDTO.setTypeId(project.getType());
            projectDTO.setPrice(project.getPrice());
            projectDTO.setDuration(project.getDuration());
            result.add(projectDTO);
        }
        return result;
    }

    public RoomDTO getRoom(Long roomId) {
        Room room = roomQueryBiz.getRoom(roomId);
        return mapToRoomDto(room);
    }

    public List<RoomDTO> listRoomsByBranchId(Long branchId) {
        List<Room> rooms = roomQueryBiz.listRoomByBranchId(branchId);
        return mapToRoomDTOs(rooms);
    }

    public List<RoomDTO> listRoomsByBranchId(Long branchId,Integer projectId) {
        Project project = projectQueryBiz.getProject(projectId);
        List<Room> rooms = roomQueryBiz.listRoomByBranchId(branchId,project.getType());
        return mapToRoomDTOs(rooms);
    }


    public List<RoomDTO> listRoomByProjectId(Integer projectId) {
        List<Room> rooms = roomQueryBiz.listRoomByProjectId(projectId);
        return mapToRoomDTOs(rooms);
    }

    private List<RoomDTO> mapToRoomDTOs(List<Room> rooms) {
        List<RoomDTO> result = Lists.newArrayList();
        for (Room room : rooms) {
            result.add(mapToRoomDto(room));
        }
        return result;
    }

    private RoomDTO mapToRoomDto(Room room){
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setName(room.getNumber());
        roomDTO.setBizStatusName(RoomState.parse(room.getBizStatusId()).getDesc());
        roomDTO.setBizStatusId(room.getBizStatusId());
        roomDTO.setHaveRestRoom(room.getHaveRestroom() == 1);
        roomDTO.setTypeId(room.getType());
        roomDTO.setTypeName(ProjectType.parse(room.getType()).getDesc());
        roomDTO.setBedNumber(room.getBedNumber());
        roomDTO.setStartTime(room.getStartTime());
        roomDTO.setOverTime(room.getOverTime());
        return roomDTO;
    }

    public List<TechnicianDTO> listTechnicianByProjectId(Integer projectId) {
        List<Technician> technicians = technicianQueryBiz.listTechnicianByProjectId(projectId);
        return mapToTechnicianDto(technicians);
    }

    public List<TechnicianDTO> listTechnicianByBranchId(Long branchId, int sort) {
        List<Technician> technicians = technicianQueryBiz.listTechnicianByBranchId(branchId, sort);
        return mapToTechnicianDto(technicians);
    }

    private List<TechnicianDTO> mapToTechnicianDto(List<Technician> technicians) {
        List<TechnicianDTO> result = Lists.newArrayList();
        for (Technician technician : technicians) {
            TechnicianDTO technicianDTO = new TechnicianDTO();
            technicianDTO.setAge(technician.getAge());
            technicianDTO.setDescription(technician.getDescription());
            technicianDTO.setName(technician.getName());
            technicianDTO.setPhone(technician.getPhone());
            technicianDTO.setStateName(TechnicianState.parse(technician.getBizStatusId()).getDesc());
            technicianDTO.setStateId(technician.getBizStatusId());
            technicianDTO.setId(technician.getId());
            technicianDTO.setJobNumber(technician.getJobNumber());
            technicianDTO.setPraise(technician.getPraise());
            technicianDTO.setOrderCount(technician.getOrderCount());
            result.add(technicianDTO);
        }
        return result;
    }

    public TechnicianDetailDTO getTechnicianDetail(Long technicianId) {
        TechnicianDetailDTO result = new TechnicianDetailDTO();
        Technician technician = technicianQueryBiz.getTechnicianById(technicianId);
        if (technician != null) {
            result.setAge(technician.getAge());
            result.setDescription(technician.getDescription());
            result.setName(technician.getName());
            result.setPhone(technician.getPhone());
            result.setStateName(TechnicianState.parse(technician.getBizStatusId()).getDesc());
            result.setStateId(technician.getBizStatusId());
            List<String> photos = technicianQueryBiz.listTechnicianPhotoById(technicianId);
            result.setPhotos(photos);
            List<Project> projects = projectQueryBiz.listProjectByTechnicianId(technicianId);
            List<String> spa = Lists.newArrayList();
            List<String> massage = Lists.newArrayList();

            for (Project project : projects) {
                if (project.getType().equals(ProjectType.spa.getValue())) {
                    spa.add(project.getName());
                } else {
                    massage.add(project.getName());
                }
            }
            result.setSpa(spa);
            result.setMassage(massage);
            return result;
        } else {
            return null;
        }
    }

    public OrderDTO getOrder(Long orderId) {
        OrderDTO result = new OrderDTO();
        Order order = orderQueryBiz.getOrder(orderId);
        if (order == null) {
            return null;
        }
        List<OrderDetail> orderDetails = orderQueryBiz.listOrderDetail(orderId);
        result.setStateId(order.getBizStatusId());
        result.setStateName(OrderState.parse(order.getBizStatusId()).getDesc());
        result.setBranchName(orderDetails.get(0).getBranchName());
        result.setPrice(order.getPrice());
        result.setId(order.getId());
        List<OrderDetailDTO> detailDTOList = Lists.newArrayList();
        mapToOrderDetailDto(orderDetails, detailDTOList);
        result.setDetails(detailDTOList);
        return result;
    }

    public OrderDTO getOrderByRoomId(Long roomId) {
        Room room = roomQueryBiz.getRoom(roomId);
        if (room.getOrderId() != null && room.getOrderId() > 0L) {
            return getOrder(room.getOrderId());
        } else {
            return null;
        }
    }

    public List<OrderDTO> listOrderByUserId(Long userId) {
        List<OrderDTO> result = Lists.newArrayList();
        List<Order> orders = orderQueryBiz.listOrderByUserId(userId);
        if (CollectionUtils.isEmpty(orders)) {
            return result;
        }
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
            List<OrderDetailDTO> detailDTOList = Lists.newArrayList();
            mapToOrderDetailDto(orderDetailList, detailDTOList);
            orderDTO.setDetails(detailDTOList);
            result.add(orderDTO);
        }
        return result;
    }

    private void mapToOrderDetailDto(List<OrderDetail> orderDetails, List<OrderDetailDTO> detailDTOList) {
        for (OrderDetail orderDetail : orderDetails) {
            OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
            orderDetailDTO.setBookTime(orderDetail.getBookTime());
            orderDetailDTO.setPrice(orderDetail.getPrice());
            orderDetailDTO.setProjectId(orderDetail.getProjectId());
            orderDetailDTO.setProjectName(orderDetail.getProjectName());
            orderDetailDTO.setRoomId(orderDetail.getRoomId());
            orderDetailDTO.setRoomName(orderDetail.getRoomName());
            orderDetailDTO.setTechnicianId(orderDetail.getTechnicianId());
            orderDetailDTO.setTechnicianName(orderDetail.getTechnicianName());
            detailDTOList.add(orderDetailDTO);
        }
    }

}
