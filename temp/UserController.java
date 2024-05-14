package com.example.myspringboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.myspringboot.entity.User;
import com.example.myspringboot.mapper.UserMapper;
import com.example.myspringboot.service.UserService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@EnableTransactionManagement

@RequestMapping("/user")//url成对//每个方法要求路径不同

public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping//插入
    //新增或更新
    public boolean save(@RequestBody User user) {
        return userService.saveUser(user);

    }

    //查询所有数据
    @GetMapping("/")//依赖于RestController
    public List<User> findAll() {
        return userService.list();
    }

    //删除数据
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return userService.removeById(id);
    }
    //批量删除
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return userService.removeByIds(ids);
    }
    //分页查询
    //@RequestParam接收 pageNum，pageSize
//    @GetMapping("/page")//接口路径
//    public Map<String, Object> findPage(@RequestParam Integer pageNum,
//                                        @RequestParam Integer pageSize,
//                                        @RequestParam String username,
//                                        @RequestParam String phone) {
//        pageNum = (pageNum - 1) * pageSize;
//        username="%"+username+"%";
//        phone="%"+phone+"%";
//        List<User> data = userMapper.selectPage(pageNum, pageSize,username,phone);
//        Integer total = userMapper.selectTotal(username,phone);
//        Map<String, Object> res = new HashMap<>();
//        res.put("data", data);
//        res.put("total", total);
//        return res;
//    }

    //分页查询-mybatis-plus实现
    @GetMapping("/page")//接口路径
    public IPage<User> findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String username,//查询条件可以缺省
                                @RequestParam(defaultValue = "") String phone
//                                @RequestParam(defaultValue = "") String address
    ) {
        IPage<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(Strings.isNotEmpty(username), "username", username);//数据库本身条件搜索不支持null
        queryWrapper.like(Strings.isNotEmpty(phone), "phone", phone);
//        queryWrapper.like(Strings.isNotEmpty(address), "address", address);
        return userService.page(page, queryWrapper);


    }

}



@GetMapping("/page")
    public Page<User> findPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam(defaultValue = "") String username,//查询条件可以缺省
                               @RequestParam(defaultValue = "") String phone
//                                @RequestParam(defaultValue = "") String address
    ) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.orderByDesc("id");
        queryWrapper.like(Strings.isNotEmpty(username), "username", username);//数据库本身条件搜索不支持null
        queryWrapper.like(Strings.isNotEmpty(phone), "phone", phone);
//        queryWrapper.like(Strings.isNotEmpty(address), "address", address);
        return userService.page(new Page<>(pageNum, pageSize), queryWrapper);
    }

