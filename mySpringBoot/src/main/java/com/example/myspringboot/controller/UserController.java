

//模板
package com.example.myspringboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.myspringboot.common.Constants;
import com.example.myspringboot.common.Result;
import com.example.myspringboot.controller.dto.UserDTO;
import com.example.myspringboot.service.impl.UserServiceImpl;
import com.example.myspringboot.utils.TokenUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.myspringboot.service.IUserService;
import com.example.myspringboot.entity.User;
import org.springframework.web.multipart.MultipartFile;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wjy
 * @since 2023-03-08
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    //自定义接口
    //注册
    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {//简单校验，判空
            return Result.error(Constants.CODE_400, "参数错误");
        }
        return Result.success(userService.register(userDTO));
    }

    //新增，更新
    @PostMapping
    public Result save(@RequestBody User user) {
        return Result.success(userService.saveOrUpdate(user));
    }

    //登录
    //@RequestBody：前端jason转后端对象
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {//简单校验，判空
            return Result.error(Constants.CODE_400, "参数错误");
        }
        UserDTO dto = userService.login(userDTO);

        return Result.success(dto);
    }

    //删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {

        return Result.success(userService.removeById(id));
    }

    //批量删除
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {

        return Result.success(userService.removeByIds(ids));
    }

    //查询全部
    @GetMapping
    public Result findAll() {
        return Result.success(userService.list());
    }

    //按照id查查询
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {

        return Result.success(userService.getById(id));
    }

    //个人信息获取(根据用户名)
    @GetMapping("/username/{username}")
    public Result findOne(@PathVariable String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return Result.success(userService.getOne(queryWrapper));
    }

    //分页查询
    @GetMapping("/page")
    public Result findPage(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(defaultValue = "") String username,//查询条件可以缺省
            @RequestParam(defaultValue = "") String phone
//                                @RequestParam(defaultValue = "") String address
    ) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        //queryWrapper.orderByDesc("id");
        queryWrapper.like(Strings.isNotEmpty(username), "username", username);//数据库本身条件搜索不支持null
        queryWrapper.like(Strings.isNotEmpty(phone), "phone", phone);
//        queryWrapper.like(Strings.isNotEmpty(address), "address", address);
//获取当前用户信息
        User current = TokenUtils.getCurrentUser();
      //  System.out.println("当前用户昵称======================" + current.getNickname());
        return Result.success(userService.page(new Page<>(pageNum, pageSize), queryWrapper));

    }

    /**
     * 导出接口
     **/
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<User> list = userService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名(不用则为英文模板)
             /*   writer.addHeaderAlias("username", "用户名");
                writer.addHeaderAlias("password", "密码");
                writer.addHeaderAlias("nickname", "昵称");
                writer.addHeaderAlias("email", "邮箱");
                writer.addHeaderAlias("phone", "电话");
                writer.addHeaderAlias("address", "地址");
                writer.addHeaderAlias("createTime", "创建时间");
              */

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }

    /**
     * excel 导入
     *
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<User> list = reader.readAll(User.class);
        userService.saveBatch(list);
        // 方式2：忽略表头的中文，直接读取表的内容（//导入表不必id和create_time，数据库自增）
               /* List<List<Object>> list = reader.read(1);//
                List<User> users = CollUtil.newArrayList();
                for (List<Object> row : list) {
                        User user = new User();
                        user.setUsername(row.get(0).toString());
                        user.setPassword(row.get(1).toString());
                        user.setNickname(row.get(2).toString());
                        user.setEmail(row.get(3).toString());
                        user.setPhone(row.get(4).toString());
                        user.setAddress(row.get(5).toString());
                        users.add(user);
                }
                  userService.saveBatch(users);
                  */
        //插入数据库
        //System.out.println(list);
        return Result.success(true);
    }

}
