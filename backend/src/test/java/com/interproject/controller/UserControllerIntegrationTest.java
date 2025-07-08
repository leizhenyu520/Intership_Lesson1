package com.interproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interproject.entity.User;
import com.interproject.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private final String TEST_PASSWORD = "Password123!";

    @BeforeEach
    void setUp() {
        // 清理测试数据
        userMapper.delete(null);

        // 创建测试用户
        testUser = new User();
        testUser.setUsername("integrationTestUser");
        testUser.setEmail("integration@example.com");
        testUser.setPassword(passwordEncoder.encode(TEST_PASSWORD));
        testUser.setVip(false);
        userMapper.insert(testUser);
    }

    @Test
    void testLoginWithUsername() throws Exception {
        // 构建登录请求体
        String loginJson = "{\"username\":\"" + testUser.getUsername() + "\",\"password\":\"" + TEST_PASSWORD + "\"}";

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(testUser.getUsername())))
                .andExpect(jsonPath("$.email", is(testUser.getEmail())))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void testLoginWithEmail() throws Exception {
        // 构建登录请求体
        String loginJson = "{\"email\":\"" + testUser.getEmail() + "\",\"password\":\"" + TEST_PASSWORD + "\"}";

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(testUser.getUsername())))
                .andExpect(jsonPath("$.email", is(testUser.getEmail())));
    }

    @Test
    void testLoginWithInvalidCredentials() throws Exception {
        String invalidLoginJson = "{\"username\":\"" + testUser.getUsername() + "\",\"password\":\"WrongPassword\"}";

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidLoginJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("用户名或密码错误")));
    }

    @Test
    void testRegisterNewUser() throws Exception {
        User newUser = new User();
        newUser.setUsername("newIntegrationUser");
        newUser.setEmail("new@example.com");
        newUser.setPassword(TEST_PASSWORD);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(newUser.getUsername())))
                .andExpect(jsonPath("$.email", is(newUser.getEmail())))
                .andExpect(jsonPath("$.password").doesNotExist());

        // 验证用户确实被保存到数据库
        User savedUser = userMapper.selectByUsername(newUser.getUsername());
        assertNotNull(savedUser);
        assertTrue(passwordEncoder.matches(TEST_PASSWORD, savedUser.getPassword()));
    }

    @Test
    void testRegisterDuplicateUsername() throws Exception {
        User duplicateUser = new User();
        duplicateUser.setUsername(testUser.getUsername()); // 使用已存在的用户名
        duplicateUser.setEmail("different@example.com");
        duplicateUser.setPassword(TEST_PASSWORD);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("用户名已存在")));
    }

    @Test
    void testGetUserInfo() throws Exception {
        mockMvc.perform(get("/api/users/" + testUser.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testUser.getId().intValue())))
                .andExpect(jsonPath("$.username", is(testUser.getUsername())))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void testGetUserInfoNotFound() throws Exception {
        mockMvc.perform(get("/api/users/9999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("用户不存在")));
    }
}