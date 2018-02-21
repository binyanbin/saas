package com.bzw.api.module.main.biz;

import com.bzw.api.module.base.dao.EmployeeFunctionMapper;
import com.bzw.api.module.base.dao.EmployeeMapper;
import com.bzw.api.module.base.dao.UserMapper;
import com.bzw.api.module.base.model.Employee;
import com.bzw.api.module.base.model.EmployeeFunction;
import com.bzw.api.module.base.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yanbin
 */
@Service
public class UserEventBiz {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmployeeFunctionMapper employeeFunctionMapper;


    public boolean bindOpenId(Long employeeId, String openId) {
        Employee employee = employeeMapper.selectByPrimaryKey(employeeId);
        employee.setWechatId(openId);
        return employeeMapper.updateByPrimaryKeySelective(employee) > 0;
    }

    public boolean updateUser(User user) {
        return userMapper.updateByPrimaryKeySelective(user) > 0;
    }

    public boolean updateEmployee(Employee employee) {
        return employeeMapper.updateByPrimaryKey(employee)>0;
    }

    public void addUser(User user) {
        userMapper.insert(user);
    }

    public void addEmployee(Employee employee) {
        employeeMapper.insert(employee);
    }

    public void addEmployeeFunction(List<EmployeeFunction> employeeFunctionList) {
        for (EmployeeFunction employeeFunction : employeeFunctionList) {
            employeeFunctionMapper.insert(employeeFunction);
        }
    }


}
