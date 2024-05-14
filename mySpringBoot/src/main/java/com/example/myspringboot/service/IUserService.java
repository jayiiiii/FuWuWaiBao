package com.example.myspringboot.service;

import com.example.myspringboot.controller.dto.UserDTO;
import com.example.myspringboot.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wjy
 * @since 2023-03-08
 */
public interface IUserService extends IService<User> {

    UserDTO login(UserDTO userDTO);

    User register(UserDTO userDTO);
}
