package ao.com.angotech.service;

import ao.com.angotech.dto.user.UserRequest;
import ao.com.angotech.dto.user.UserResponse;

public interface UserService {

    UserResponse register(UserRequest request);
}
