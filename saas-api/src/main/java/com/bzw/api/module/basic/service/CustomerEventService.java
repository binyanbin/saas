package com.bzw.api.module.basic.service;

import com.bzw.api.module.basic.biz.*;
import com.bzw.api.module.basic.enums.*;
import com.bzw.api.module.basic.model.*;
import com.bzw.api.module.basic.param.OrderParam;
import com.bzw.api.module.platform.model.User;
import com.bzw.common.content.WebSession;
import com.bzw.common.content.WebSessionManager;
import com.bzw.common.exception.api.UserLoginFailException;
import com.bzw.common.exception.api.WechatLoginFailException;
import com.bzw.common.sequence.SeqType;
import com.bzw.common.sequence.SequenceService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author yanbin
 */
@Service
public class CustomerEventService {

    @Autowired
    private RoomEventBiz roomEventBiz;

    @Autowired
    private RoomQueryBiz roomQueryBiz;

    @Autowired
    private ProjectQueryBiz projectQueryBiz;

    @Autowired
    private TechnicianQueryBiz technicianQueryBiz;

    @Autowired
    private BranchQueryBiz branchQueryBiz;

    @Autowired
    private TenantQueryBiz tenantQueryBiz;

    @Autowired
    private OrderQueryBiz orderQueryBiz;

    @Autowired
    private UserEventBiz userEventBiz;

    @Autowired
    private UserQueryBiz userQueryBiz;

    @Autowired
    private OrderEventBiz orderEventBiz;

    @Autowired
    private EmployeeQueryBiz employeeQueryBiz;

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private RecordChangeEventBiz recordChangeEventBiz;

    @Autowired
    private WebSessionManager webSessionManager;


    public boolean updateRoomState(Long roomId, Integer statusId, Long employeeId) {
        Room room = roomQueryBiz.getRoom(roomId);
        if (room.getBizStatusId().equals(statusId)) {
            return false;
        }
        Date now = new Date();
        Employee employee = employeeQueryBiz.getEmployee(employeeId);
        RoomExample roomExample = new RoomExample();
        roomExample.createCriteria().andIdEqualTo(roomId).andVersionIdEqualTo(room.getVersionId());
        Room updateRoom = new Room();
        updateRoom.setStatusId(statusId);
        updateRoom.setModifiedTime(now);
        updateRoom.setModifiedId(employee.getId());
        updateRoom.setVersionId(room.getVersionId() + 1);
        List<Integer> statusIds = Lists.newArrayList();
        statusIds.add(RoomState.free.getValue());
        statusIds.add(RoomState.cleaning.getValue());
        statusIds.add(RoomState.pause.getValue());
        if (statusIds.contains(statusId)) {
            updateRoom.setOrderId(0L);
        }
        int result = roomEventBiz.updateRoom(updateRoom,roomId,room.getVersionId());
        if (result > 0) {
            RecordChange recordChange = new RecordChange();
            recordChange.setId(sequenceService.newKey(SeqType.recordChange));
            recordChange.setChnageDate(now);
            recordChange.setContent(employee.getName() + "把房态[" + RoomState.parse(room.getBizStatusId()).getDesc() + "]修改为:[" + RoomState.parse(statusId).getDesc() + "]");
            recordChange.setDocumentId(room.getId());
            recordChange.setTenantId(room.getTenantId());
            recordChange.setType(RecordChangeType.room.getValue());
            recordChangeEventBiz.add(recordChange);
            return true;
        } else {
            return false;
        }
    }

    public WebSession login(String code, String password) {
        List<User> users = userQueryBiz.listLoginUser(code,password);
        if (CollectionUtils.isEmpty(users)) {
            throw new UserLoginFailException();
        }
        User user = users.get(0);
        List<Employee> employees = employeeQueryBiz.listEmployeeByUserId(user.getId());
        if (CollectionUtils.isEmpty(employees)) {
            throw new UserLoginFailException();
        }
        Employee employee = employees.get(0);
        WebSession webSession = new WebSession();
        webSession.setTenantId(employee.getTenantId());
        webSession.setRoleName(RoleType.parse(employee.getRoleType()).getDesc());
        webSession.setUserId(user.getId());
        webSession.setEmployeeId(employee.getId());
        webSession.setRoleType(employee.getRoleType());
        webSession.setName(employee.getName());
        webSession.setBranchId(employee.getBranchId());

        Branch branch = branchQueryBiz.getBranch(employee.getBranchId());
        webSession.setBranchName(branch.getName());
        Tenant tenant = tenantQueryBiz.getTenant(branch.getId());
        webSession.setTenantName(tenant.getName());
        webSession.setOpenId(employee.getWechatId());
        user.setLastLoginDate(new Date());
        userEventBiz.updateUser(user);
        return webSessionManager.add(webSession, webSessionManager.newId(user.getId()), webSessionManager.newSecretKey());
    }

    public WebSession openIdLogin(String openId) {
        List<Employee> employees = employeeQueryBiz.listEmployeeByOpenId(openId);
        if (CollectionUtils.isEmpty(employees)) {
            throw new WechatLoginFailException();
        }
        Employee employee = employees.get(0);
        List<User> users = userQueryBiz.listUserById(employee.getUserId());
        if (CollectionUtils.isEmpty(users)) {
            throw new WechatLoginFailException();
        }
        User user = users.get(0);
        WebSession webSession = new WebSession();
        webSession.setTenantId(employee.getTenantId());
        webSession.setRoleName(RoleType.parse(employee.getRoleType()).getDesc());
        webSession.setUserId(user.getId());
        webSession.setEmployeeId(employee.getId());
        webSession.setRoleType(employee.getRoleType());
        webSession.setName(employee.getName());
        webSession.setBranchId(employee.getBranchId());
        Branch branch = branchQueryBiz.getBranch(employee.getBranchId());
        webSession.setBranchName(branch.getName());
        Tenant tenant = tenantQueryBiz.getTenant(branch.getTenantId());
        webSession.setTenantName(tenant.getName());
        user.setLastLoginDate(new Date());
        userEventBiz.updateUser(user);
        return webSessionManager.add(webSession, webSessionManager.newId(user.getId()), webSessionManager.newSecretKey());
    }

    public boolean bindOpenId(Long employeeId, String openId) {
        return userEventBiz.bindOpenId(employeeId, openId);
    }

    public Long addOrder(List<OrderParam> orderParams) {
        Long orderId = sequenceService.newKey(SeqType.order);
        return addOrder(orderId, orderParams, false);
    }

    private Long addOrder(Long orderId, List<OrderParam> orderParams, boolean isUpdate) {
        Date now = new Date();
        Room room = roomQueryBiz.getRoom(orderParams.get(0).getRoomId());
        if (room == null) {
            return null;
        }
        Long tenantId = room.getTenantId();
        Long branchId = room.getBranchId();
        Order order = new Order();
        if (!isUpdate) {
            order.setId(orderId);
            order.setTenantId(tenantId);
            order.setBranchId(branchId);
            order.setCreatedTime(now);
            order.setBizStatusId(OrderState.non_payment.getValue());
        } else {
            order = orderQueryBiz.getOrder(orderId);
        }
        List<Long> detailIds = sequenceService.newKeys(SeqType.orderDetail, orderParams.size());
        List<Integer> projectIds = Lists.newArrayList();
        List<Long> technicianIds = Lists.newArrayList();
        List<Long> roomIds = Lists.newArrayList();
        for (OrderParam orderParam : orderParams) {
            projectIds.add(orderParam.getProjectId());
            technicianIds.add(orderParam.getTechnicianId());
            roomIds.add(orderParam.getRoomId());
        }
        List<Project> projects = projectQueryBiz.listProjectByIds(projectIds);
        Map<Integer, Project> mapProject = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(projects)) {
            projects.forEach(t -> mapProject.put(t.getId(), t));
        }

        List<Technician> technicians = technicianQueryBiz.listTechnicianByIds(technicianIds);
        Map<Long, Technician> mapTechnician = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(technicians)) {
            technicians.forEach(t -> mapTechnician.put(t.getId(), t));
        }

        List<Room> rooms = roomQueryBiz.listRoomByIds(roomIds);
        Map<Long, Room> mapRoom = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(rooms)) {
            rooms.forEach(t -> mapRoom.put(t.getId(), t));
        }

        BigDecimal totalPrice = new BigDecimal(0);
        List<OrderDetail> orderDetails = Lists.newArrayList();
        for (int i = 0; i < orderParams.size(); i++) {
            if (mapProject.containsKey(orderParams.get(i).getProjectId()) &&
                    mapTechnician.containsKey(orderParams.get(i).getTechnicianId()) &&
                    mapRoom.containsKey(orderParams.get(i).getRoomId())) {
                Project project = mapProject.get(orderParams.get(i).getProjectId());
                Technician technician = mapTechnician.get(orderParams.get(i).getTechnicianId());
                Room room1 = mapRoom.get(orderParams.get(i).getRoomId());
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setBizStatusId(OrderDetailState.booked.getValue());
                orderDetail.setTenantId(room1.getTenantId());
                orderDetail.setBranchId(branchId);
                orderDetail.setId(detailIds.get(i));
                orderDetail.setBookTime(now);
                orderDetail.setOrderId(order.getId());
                orderDetail.setRoomId(orderParams.get(i).getRoomId());
                orderDetail.setProjectId(orderParams.get(i).getProjectId());
                orderDetail.setTechnicianId(orderParams.get(i).getTechnicianId());
                orderDetail.setBranchName(room.getBranchName());
                orderDetail.setPrice(project.getPrice());
                orderDetail.setProjectName(project.getName());
                orderDetail.setTechnicianName(technician.getName());
                orderDetail.setRoomName(room1.getNumber());
                orderDetail.setTypeId(OrderType.CustomerReservation.getValue());
                orderDetails.add(orderDetail);
                totalPrice = totalPrice.add(project.getPrice());
            }
        }
        order.setPrice(totalPrice);
        if (!isUpdate) {
            orderEventBiz.add(order);
        } else {
            order = new Order();
            order.setId(orderId);
            order.setPrice(totalPrice);
            order.setModifiedTime(now);
            orderEventBiz.update(order);
        }
        orderEventBiz.addOrderDetails(orderDetails);
        room.setOrderId(orderId);
        roomEventBiz.updateRoom(room);
        return order.getId();
    }

    public Long modifyOrder(Long orderId, List<OrderParam> orderParams) {
        orderEventBiz.deleteById(orderId);
        return addOrder(orderId, orderParams, true);
    }
}
