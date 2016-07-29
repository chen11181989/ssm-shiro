package com.shiro.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.shiro.dao.UserMapper;
import com.shiro.model.User;
import com.shiro.model.UserExample;
import com.shiro.service.UserService;


/**
 * 用户Service实现类
 *
 * @author StarZou
 * @since 2014年7月5日 上午11:54:24
 */
@Service
public class UserServiceImpl  implements UserService {

    @Resource
    private UserMapper userMapper;

    public User authentication(User user) {
        return userMapper.authentication(user);
    }

    public User selectById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

  
    public User selectByUsername(String username) {
        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(username);
        final List<User> list = userMapper.selectByExample(example);
        return list.get(0);
    }

}
