package com.interproject.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.interproject.entity.User;
import com.interproject.mapper.UserMapper;
import com.interproject.service.UserService;
import com.interproject.vo.LoginVO;
import com.interproject.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResultVO<LoginVO> login(String username, String password, String email) {
        // 1. 查询用户（支持用户名或邮箱登录）
        User user = userMapper.selectByUsernameAndPassword(username, password);
        if (user == null) {
            user = userMapper.selectByEmail(email);
            if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
                return ResultVO.fail("用户名/邮箱或密码错误");
            }
        }

        // 2. 生成JWT令牌（实际项目中需集成JWT工具类）
        String token = generateToken(user.getId());

        // 3. 封装返回结果
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUser(user);
        return ResultVO.success(loginVO);
    }

    @Override
    public ResultVO<User> getUserById(Integer userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return ResultVO.fail("用户不存在");
        }
        // 脱敏处理：隐藏密码
        user.setPassword(null);
        return ResultVO.success(user);
    }

    @Override
    public ResultVO<String> register(User user) {
        // 1. 检查用户名和邮箱是否已存在
        User existingUser = userMapper.selectByUsernameAndPassword(user.getUsername(), null);
        if (existingUser != null) {
            return ResultVO.fail("用户名已存在");
        }
        if (userMapper.selectByEmail(user.getEmail()) != null) {
            return ResultVO.fail("邮箱已被注册");
        }

        // 2. 密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 3. 设置默认值
        user.setIsVip(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // 4. 保存用户
        int rows = userMapper.insert(user);
        return rows > 0 ? ResultVO.success("注册成功") : ResultVO.fail("注册失败");
    }

    /**
     * 生成JWT令牌（实际项目中需替换为真实JWT生成逻辑）
     */
    private String generateToken(Integer userId) {
        // 简化实现，实际项目中需使用JWT工具类生成带签名的令牌
        return "JWT-" + userId + "-" + System.currentTimeMillis();
    }
}