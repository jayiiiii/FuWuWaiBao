package com.example.myspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author wjy
 * @since 2023-03-08
 */
@Getter
@Setter
  @TableName("sys_user")
@ToString

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * id	
     */
      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 用户名
     */
      private String username;

      /**
     * 密码	
     */
      private String password;

      /**
     * 昵称
     */
      private String nickname;

      /**
     * 邮箱
     */
      private String email;

      /**
     * 电话
     */
      private String phone;

      /**
     * 地址
     */
      private String address;

      /**
     * 建立时间
     */
      private Date createTime;


}
