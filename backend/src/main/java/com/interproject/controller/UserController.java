package com.interproject.controller;

import com.interproject.entity.User;
import com.interproject.service.UserService;
import com.interproject.vo.LoginVO;
import com.interproject.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录接口
     */
    @PostMapping("/login")
    public ResultVO<LoginVO> login(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email) {
        return userService.login(username, password, email);
    }

    /**
     * 用户注册接口
     */
    @PostMapping("/register")
    public ResultVO<String> register(@RequestBody User user) {
        return userService.register(user);
    }

    /**
     * 获取用户信息接口
     */
    @GetMapping("/{userId}")
    public ResultVO<User> getUserInfo(@PathVariable Integer userId) {
        return userService.getUserById(userId);
    }
}