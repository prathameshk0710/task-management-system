package com.spring.application.service;

import com.spring.application.dto.UserDto;
import com.spring.application.entity.Task;
import com.spring.application.entity.User;
import com.spring.application.exception.UserNotFoundException;
import com.spring.application.repository.IUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public UserDto addUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        return modelMapper.map(userRepository.save(user), UserDto.class);
    }

    public UserDto updateUser(Long userId, UserDto userDetails) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            user.setTimezone(userDetails.getTimezone());
            user.setIsActive(userDetails.getIsActive());
            return modelMapper.map(userRepository.save(user), UserDto.class);
        }
        throw new UserNotFoundException("User not found with id " + userId);
    }

    public String deleteUser(Long userId) {
        if(userRepository.existsById(userId)){
            userRepository.deleteById(userId);
            return "User deleted successfully";
        }
        throw new UserNotFoundException("User not found with id " + userId);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        if(!users.isEmpty()){
            List<UserDto> userDtoList = new ArrayList<UserDto>();
            for(User user : users){
                userDtoList.add(modelMapper.map(user, UserDto.class));
            }
            return userDtoList;
        }
        throw new UserNotFoundException("No users found");
    }

    public UserDto getUserById(Long UserId) {
        Optional<User> user = userRepository.findById(UserId);
        if(user.isPresent())
            return modelMapper.map(user.get(), UserDto.class);
        throw new UserNotFoundException("User not found with id " + UserId);
    }
}
