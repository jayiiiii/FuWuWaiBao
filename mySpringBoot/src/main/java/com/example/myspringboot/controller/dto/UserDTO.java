package com.example.myspringboot.controller.dto;

import lombok.Data;

@Data
/*接收前端登录请求*/
public class UserDTO {
    private String username;
    private String password;
    private String nickname;
    private String token;
}
