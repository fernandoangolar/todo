package ao.com.angotech.service;

import ao.com.angotech.dto.user.UserRequest;
import ao.com.angotech.dto.user.UserResponse;
import ao.com.angotech.entity.User;

import java.util.List;

public interface UserService {

    UserResponse register(UserRequest request);
    UserResponse findById(Long id);
    User findByEmail(String email);
}
