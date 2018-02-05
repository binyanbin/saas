package com.bzw.api.module.main.service;

import com.bzw.api.module.base.dao.*;
import com.bzw.api.module.base.model.*;
import com.bzw.api.module.main.enums.ProjectType;
import com.bzw.api.module.main.enums.RoleType;
import com.bzw.api.module.main.enums.RoomState;
import com.bzw.api.module.main.enums.TechnicianState;
import com.bzw.common.enums.Status;
import com.bzw.common.sequence.SeqType;
import com.bzw.common.sequence.SequenceService;
import com.bzw.common.utils.Sha256;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author yanbin
 */
@Service
public class InitService {

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private TechnicianMapper technicianMapper;

    @Autowired
    private TenantMapper tenantMapper;

    @Autowired
    private BranchMapper branchMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private TechnicianProjectMapper technicianProjectMapper;

    @Autowired
    private UserMapper userMapper;

    public void initUser() {

        Date now = new Date();
        Tenant tenant = new Tenant();
        Long tenantId = sequenceService.newKey(SeqType.tenant);
        tenant.setId(tenantId);
        tenant.setCreatedTime(now);
        tenant.setModifiedTime(now);
        tenant.setName("测试");
        tenant.setPhone("13755144076");
        tenant.setStatusId(Status.Valid.getValue());
        tenantMapper.deleteByExample(null);
        tenantMapper.insert(tenant);


        Branch branch = new Branch();
        Long branchId = sequenceService.newKey(SeqType.branch);
        branch.setId(branchId);
        branch.setCreatedId(0L);
        branch.setCreatedTime(now);
        branch.setModifiedId(0L);
        branch.setModifiedTime(now);
        branch.setName("长沙测试店");
        branch.setStatusId(Status.Valid.getValue());
        branch.setTenantId(tenantId);
        branchMapper.deleteByExample(null);
        branchMapper.insert(branch);

        String[] projectNames = {"云梦油压398", "养生指压298", "88足浴", "108足浴"};
        Integer[] prices = {398, 298, 88, 108};
        List<Long> projectIds = sequenceService.newKeys(SeqType.project, 4);
        projectMapper.deleteByExample(null);
        List<Project> projects = Lists.newArrayList();
        for (int i = 0; i < projectNames.length; i++) {
            Project project = new Project();
            project.setId(projectIds.get(i).intValue());
            project.setBranchId(branchId);
            project.setTenantId(tenantId);
            project.setName(projectNames[i]);
            project.setStatusId(Status.Valid.getValue());
            project.setPrice(new BigDecimal(prices[i]));
            project.setBranchName(branch.getName());
            if (i<=1) {
                project.setType(ProjectType.spa.getValue());
            } else {
                project.setType(ProjectType.footMassage.getValue());
            }
            projects.add(project);
            projectMapper.insert(project);
        }

        String[] mobiles = {
                "15073715313", "18674435113", "14785296311", "13874820742", "15574983900",
                "18061448856", "13787165388",
        };
        String[] names = {
                "吕凯", "钟双玉", "张翠萍", "任峥", "雷洪波", "沈兰萍", "陈总"
        };
        List<Long> userIds = sequenceService.newKeys(SeqType.User, 7);
        List<Long> employeeIds = sequenceService.newKeys(SeqType.employee, 7);

        userMapper.deleteByExample(null);
        employeeMapper.deleteByExample(null);
        for (int i = 0; i < userIds.size(); i++) {
            User user = new User();
            user.setId(userIds.get(i));
            user.setCode(mobiles[i]);
            user.setPassword(Sha256.encrypt("888888"));
            user.setCreatedDate(now);
            user.setCreatedId(0L);
            user.setModifiedDate(now);
            user.setModifiedId(0L);
            user.setStatusId(Status.Valid.getValue());
            user.setPhone(mobiles[i]);
            user.setName(names[i]);
            userMapper.insert(user);
            Employee employee = new Employee();
            employee.setId(employeeIds.get(i));
            employee.setTenantId(tenantId);
            employee.setBranchId(branchId);
            employee.setName(names[i]);
            employee.setPhone(mobiles[i]);
            employee.setSex("female");
            employee.setStatusId(Status.Valid.getValue());
            employee.setIsBranchSuper(Byte.parseByte("0"));
            employee.setIsTenantSupper(Byte.parseByte("0"));
            employee.setUserId(user.getId());
            employee.setSex("女");
            employee.setRoleType(RoleType.reception.getValue());
            employee.setBranchName(branch.getName());
            employeeMapper.insert(employee);
        }

        String[] technicianNames = {"田琴","张霞","刘婷","范范"};
        List<Long> technicianIds = sequenceService.newKeys(SeqType.technician,4);
        technicianMapper.deleteByExample(null);
        for (int i=0;i<technicianNames.length;i++) {
            Technician technician = new Technician();
            technician.setId(technicianIds.get(i));
            technician.setAge(18);
            technician.setStatusId(Status.Valid.getValue());
            technician.setCreatedId(0L);
            technician.setCreatedDate(now);
            technician.setBizStatusId(TechnicianState.free.getValue());
            technician.setEmployeeId(employeeIds.get(0));
            technician.setModifiedDate(now);
            technician.setModifiedId(0L);
            technician.setName(technicianNames[i]);
            technician.setPhone(mobiles[0]);
            technician.setDescription("");
            technician.setTenantId(tenantId);
            technician.setBranchId(branchId);
            technician.setBranchName(branch.getName());
            technicianMapper.insert(technician);

            List<Long> technicianProjectIds = sequenceService.newKeys(SeqType.technicianProject,2);
            TechnicianProject technicianProject = new TechnicianProject();
            if (i<=1){
                for (int j=0;j<technicianProjectIds.size();j++) {
                    technicianProject.setId(technicianProjectIds.get(j));
                    technicianProject.setBranchId(branchId);
                    technicianProject.setTechnicianId(technician.getId());
                    technicianProject.setTenantId(tenantId);
                    technicianProject.setProjectId(projects.get(j).getId());
                }
            } else {
                for (int j=0;j<technicianProjectIds.size();j++) {
                    technicianProject.setId(technicianProjectIds.get(j));
                    technicianProject.setBranchId(branchId);
                    technicianProject.setTechnicianId(technician.getId());
                    technicianProject.setTenantId(tenantId);
                    technicianProject.setProjectId(projects.get(j+2).getId());
                }
            }
            technicianProjectMapper.insert(technicianProject);
        }


        String[] roomNum = {"508", "518", "528", "558", "658", "668", "678"};

        List<Long> roomIds = sequenceService.newKeys(SeqType.room, 7);
        roomMapper.deleteByExample(null);
        for (int i = 0; i < roomNum.length; i++) {
            Room room = new Room();
            room.setId(roomIds.get(i));
            room.setTenantId(tenantId);
            room.setBranchId(branchId);
            room.setBizStatusId(RoomState.free.getValue());
            room.setCreatedId(0L);
            room.setCreatedTime(now);
            room.setDescription("");
            room.setHaveRestroom(Byte.parseByte("1"));
            room.setModifiedId(0L);
            room.setModifiedTime(now);
            room.setName(roomNum[i]);
            room.setStatusId(Status.Valid.getValue());
            room.setBranchName(branch.getName());
            if (i <= 3) {
                room.setType(ProjectType.footMassage.getValue());
            } else {
                room.setType(ProjectType.spa.getValue());
            }
            room.setVersionId(0);
            roomMapper.insert(room);
        }
    }
}
