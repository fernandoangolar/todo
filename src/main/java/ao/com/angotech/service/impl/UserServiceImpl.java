package ao.com.angotech.service.impl;

import ao.com.angotech.dto.user.UserRequest;
import ao.com.angotech.dto.user.UserResponse;
import ao.com.angotech.entity.User;
import ao.com.angotech.mapper.user.UserMapper;
import ao.com.angotech.repository.UserRepository;
import ao.com.angotech.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponse register(UserRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("E-mail já cadastrado...");
        }

        User entity = userMapper.toEntity(request);
        // criptografar a senha
        User saveEntity = userRepository.save(entity);

        return userMapper.toResponse(saveEntity);
    }
}
