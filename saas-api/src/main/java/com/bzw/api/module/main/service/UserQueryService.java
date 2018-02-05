package com.bzw.api.module.main.service;

import com.bzw.api.module.base.model.Employee;
import com.bzw.api.module.base.model.User;
import com.bzw.api.module.main.biz.RoomQueryBiz;
import com.bzw.api.module.main.biz.UserQueryBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author yanbin
 */
@Service
public class UserQueryService {

    @Autowired
    private UserQueryBiz userQueryBiz;

    public List<User> listUsersByPhone(String phone) {
        return userQueryBiz.listUserByPhone(phone);
    }

    public Employee getEmployeeByUsers(List<User> users){
        List<Employee> employees = userQueryBiz.listEmployeeByUsers(users);
        if (!CollectionUtils.isEmpty(employees)){
            return employees.get(0);
        } else{
            return null;
        }
    }

}
