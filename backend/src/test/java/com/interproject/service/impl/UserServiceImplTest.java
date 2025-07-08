package com.interproject.service.impl;

import com.interproject.entity.User;
import com.interproject.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setVip(false);
    }

    @Test
    void testLogin_Success_WithUsername() {
        when(userMapper.selectByUsername("testUser")).thenReturn(testUser);
        when(passwordEncoder.matches("rawPassword", "encodedPassword")).thenReturn(true);

        User result = userService.login("testUser", null, "rawPassword");

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        assertNull(result.getPassword()); // 密码应被脱敏
    }

    @Test
    void testLogin_Success_WithEmail() {
        when(userMapper.selectByEmail("test@example.com")).thenReturn(testUser);
        when(passwordEncoder.matches("rawPassword", "encodedPassword")).thenReturn(true);

        User result = userService.login(null, "test@example.com", "rawPassword");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testLogin_Failure_UserNotFound() {
        when(userMapper.selectByUsername("nonExistentUser")).thenReturn(null);
        when(userMapper.selectByEmail("nonexistent@example.com")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.login("nonExistentUser", null, "password");
        });
    }

    @Test
    void testLogin_Failure_WrongPassword() {
        when(userMapper.selectByUsername("testUser")).thenReturn(testUser);
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.login("testUser", null, "wrongPassword");
        });
    }

    @Test
    void testGetUserById_Success() {
        when(userMapper.selectById(1L)).thenReturn(testUser);

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertNull(result.getPassword()); // 密码应被脱敏
    }

    @Test
    void testGetUserById_NotFound() {
        when(userMapper.selectById(999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserById(999L);
        });
    }

    @Test
    void testRegister_Success() {
        when(userMapper.selectByUsername("newUser")).thenReturn(null);
        when(userMapper.selectByEmail("new@example.com")).thenReturn(null);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userMapper.insert(any(User.class))).thenReturn(1);

        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setEmail("new@example.com");
        newUser.setPassword("newPassword");

        User result = userService.register(newUser);

        assertNotNull(result);
        assertEquals("newUser", result.getUsername());
        assertNull(result.getPassword());
    }

    @Test
    void testRegister_Failure_UsernameExists() {
        when(userMapper.selectByUsername("existingUser")).thenReturn(testUser);

        User newUser = new User();
        newUser.setUsername("existingUser");
        newUser.setEmail("new@example.com");
        newUser.setPassword("password");

        assertThrows(IllegalArgumentException.class, () -> {
            userService.register(newUser);
        });
    }
}