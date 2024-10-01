package com.spring.application.service;

import com.spring.application.dto.UserDto;
import com.spring.application.entity.User;
import com.spring.application.exception.UserNotFoundException;
import com.spring.application.repository.IUserRepository;
import com.spring.application.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDto = TestUtils.getUserDto1();
    }

    @Test
    void testAddUser() {
        User user = TestUtils.getUser1();

        when(modelMapper.map(any(UserDto.class), eq(User.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(any(User.class), eq(UserDto.class))).thenReturn(userDto);

        UserDto createdUser = userService.addUser(userDto);

        assertEquals("Bhanu", createdUser.getFirstName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser() {
        User existingUser = TestUtils.getUser1();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(modelMapper.map(any(User.class), eq(UserDto.class))).thenReturn(userDto);
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        UserDto updatedUser = userService.updateUser(1L, userDto);

        assertEquals("Bhanu", updatedUser.getFirstName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(1L, userDto);
        });

        assertEquals("User not found with id 1", exception.getMessage());
    }

    @Test
    void testDeleteUser() {
        when(userRepository.existsById(anyLong())).thenReturn(true);

        String response = userService.deleteUser(1L);

        assertEquals("User deleted successfully", response);
        verify(userRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteUserNotFound() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(1L);
        });

        assertEquals("User not found with id 1", exception.getMessage());
    }

    @Test
    void testGetAllUsers() {
        User user1 = TestUtils.getUser1();
        User user2 = TestUtils.getUser2();

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        when(modelMapper.map(any(User.class), eq(UserDto.class))).thenReturn(userDto);

        List<UserDto> users = userService.getAllUsers();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetAllUsersNoUsersFound() {
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getAllUsers();
        });

        assertEquals("No users found", exception.getMessage());
    }

    @Test
    void testGetUserById() {
        User user = TestUtils.getUser1();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(modelMapper.map(any(User.class), eq(UserDto.class))).thenReturn(userDto);

        UserDto foundUser = userService.getUserById(1L);

        assertEquals("Bhanu", foundUser.getFirstName());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(1L);
        });

        assertEquals("User not found with id 1", exception.getMessage());
    }
}
