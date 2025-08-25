package ao.com.angotech.service.impl;

import ao.com.angotech.dto.user.UserRequest;
import ao.com.angotech.dto.user.UserResponse;
import ao.com.angotech.entity.User;
import ao.com.angotech.exception.ResourceNotFoundException;
import ao.com.angotech.mapper.user.UserMapper;
import ao.com.angotech.repository.UserRepository;
import ao.com.angotech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse register(UserRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("E-mail já cadastrado...");
        }

        User entity = userMapper.toEntity(request);
        entity.setPassword(passwordEncoder.encode(request.password()));
        User saveEntity = userRepository.save(entity);

        return userMapper.toResponse(saveEntity);
    }

    @Override
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException(String.format("Uusário com id %d não encontrado", id)) );

        return userMapper.toResponse(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow( () -> new ResourceNotFoundException(String.format("Uusário com id %d não encontrado", email) ));
    }
}
