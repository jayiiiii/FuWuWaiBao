package com.example.myspringboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myspringboot.entity.User;
import com.example.myspringboot.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserService extends ServiceImpl<UserMapper,User> {
    public boolean saveUser(User user) {
    return saveOrUpdate(user);

    }
   /* @Autowired
    private UserMapper userMapper;
    public int save(User user){
        if(user.getId()==null){//user没有id，则表示是新增
            return userMapper.insert(user);
        }else{//否则为更新
           return  userMapper.update(user);
        }
    }*/
}
