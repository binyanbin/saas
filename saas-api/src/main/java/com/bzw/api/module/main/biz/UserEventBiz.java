package com.bzw.api.module.main.biz;

import com.bzw.api.module.base.dao.EmployeeMapper;
import com.bzw.api.module.base.model.Employee;
import com.bzw.api.module.base.dao.UserMapper;
import com.bzw.api.module.base.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yanbin
 */
@Service
public class UserEventBiz {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmployeeMapper employeeMapper;


    public boolean bindOpenId(Long employeeId, String openId){
        Employee employee = employeeMapper.selectByPrimaryKey(employeeId);
        employee.setWechatId(openId);
        return employeeMapper.updateByPrimaryKeySelective(employee)>0;
    }

    public void updateUser(User user){
        userMapper.updateByPrimaryKeySelective(user);
    }
}
