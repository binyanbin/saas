package com.bzw.api.module.main.biz;

import com.bzw.api.module.base.dao.EmployeeMapper;
import com.bzw.api.module.base.model.Employee;
import com.bzw.api.module.base.model.EmployeeExample;
import com.bzw.common.system.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yanbin
 */
@Service
public class EmployeeQueryBiz {

    @Autowired
    private EmployeeMapper employeeMapper;

    public Employee getEmployee(Long id){
        return employeeMapper.selectByPrimaryKey(id);
    }

    public List<Employee> listEmployeeByUserId(Long userId){
        EmployeeExample employeeExample = new EmployeeExample();
        employeeExample.createCriteria().andUserIdEqualTo(userId).andStatusIdEqualTo(Status.Valid.getValue());
        return employeeMapper.selectByExample(employeeExample);
    }

    public List<Employee> listEmployeeByOpenId(String openId){
        EmployeeExample employeeExample = new EmployeeExample();
        employeeExample.createCriteria().andWechatIdEqualTo(openId).andStatusIdEqualTo(Status.Valid.getValue());
        return employeeMapper.selectByExample(employeeExample);
    }



}
