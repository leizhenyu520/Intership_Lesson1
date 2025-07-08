package com.interproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.interproject.entity.User;
import com.interproject.vo.LoginVO;
import com.interproject.vo.ResultVO;

public interface UserService extends IService<User> {
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱
     * @return 登录结果（包含token和用户信息）
     */
    ResultVO<LoginVO> login(String username, String password, String email);

    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    ResultVO<User> getUserById(Integer userId);

    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册结果
     */
    ResultVO<String> register(User user);
}