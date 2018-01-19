package com.bzw.api.module.basic.biz;

import com.bzw.api.module.platform.dao.UserMapper;
import com.bzw.api.module.platform.model.User;
import com.bzw.api.module.platform.model.UserExample;
import com.bzw.common.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yanbin
 */
@Service
public class UserQueryBiz {

    @Autowired
    private UserMapper userMapper;

    public List<User> listLoginUser(String code,String password){
        UserExample userExample = new UserExample();
        userExample.createCriteria().andCodeEqualTo(code).andPasswordEqualTo(password).andStatusIdEqualTo(Status.Valid.getValue());
        return userMapper.selectByExample(userExample);
    }

    public List<User> listUserById(Long id){
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdEqualTo(id).andStatusIdEqualTo(Status.Valid.getValue());
        return userMapper.selectByExample(userExample);
    }
}
