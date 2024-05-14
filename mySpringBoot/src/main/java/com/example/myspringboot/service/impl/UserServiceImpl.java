package com.example.myspringboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.myspringboot.common.Constants;
import com.example.myspringboot.controller.dto.UserDTO;
import com.example.myspringboot.entity.User;
import com.example.myspringboot.exception.ServiceException;
import com.example.myspringboot.mapper.UserMapper;
import com.example.myspringboot.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myspringboot.utils.TokenUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wjy
 * @since 2023-03-08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final Log LOG = Log.get();

    @Override
    public UserDTO login(UserDTO userDTO) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        queryWrapper.eq("password", userDTO.getPassword());
        User one;
        // 处理异常情况
        try {
             one = getOne(queryWrapper);//从数据库查询用户信息

        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        } if (one != null) {
                BeanUtil.copyProperties(one, userDTO, true);
            String token=TokenUtils.genToken(one.getId().toString(),one.getPassword());
                userDTO.setToken(token);
            return userDTO;
            } else {
                throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
            }

    }
    @Override
    public User register(UserDTO userDTO){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        User one ;
        try {
            one = getOne(queryWrapper);//从数据库查询用户信息

        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        if(one==null){
            one =new User();
            BeanUtil.copyProperties(userDTO, one, true);
            save(one);
        }else{
            throw new ServiceException(Constants.CODE_600,"用户名已存在");
        }
        return one;
    }
}