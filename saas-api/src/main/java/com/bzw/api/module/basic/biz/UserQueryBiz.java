package com.bzw.api.module.basic.biz;

import com.bzw.api.module.basic.dao.UserMapper;
import com.bzw.api.module.basic.model.User;
import com.bzw.api.module.basic.model.UserExample;
import com.bzw.common.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author yanbin
 */
@Service
public class UserQueryBiz {

    @Autowired
    private UserMapper userMapper;

    public List<User> listLoginUser(String code, String password) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andCodeEqualTo(code).andPasswordEqualTo(password).andStatusIdEqualTo(Status.Valid.getValue());
        return userMapper.selectByExample(userExample);
    }

    public User getUser(Long id) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdEqualTo(id).andStatusIdEqualTo(Status.Valid.getValue());
        List<User> users = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(users)) {
            return null;
        } else {
            return users.get(0);
        }
    }

    public List<User> listUserByPhone(String phone) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andPhoneEqualTo(phone).andStatusIdEqualTo(Status.Valid.getValue());
        return userMapper.selectByExample(userExample);
    }
}
