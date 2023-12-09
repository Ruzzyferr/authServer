package com.example.authserver.service;

import com.example.authserver.config.JwtService;
import com.example.authserver.dto.LoginBackDto;
import com.example.authserver.dto.LoginDto;
import com.example.authserver.dto.UserDto;
import com.example.authserver.dto.UserSaveRequestDto;
import com.example.authserver.entity.User;
import com.example.authserver.mapper.UserMapper;
import com.example.authserver.repository.UserRepository;
import com.example.authserver.util.Encryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    Encryptor encryptor = com.example.authserver.util.Encryptor.getInstance();

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;



    public UserService(UserMapper userMapper, UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    public UserDto save(UserSaveRequestDto dto) {

        User user = userMapper.toEntityFromSaveRequestDto(dto);

        user.setPassword(encryptor.generateSecurePassword(user.getPassword()));

        user = userRepository.save(user);

        UserDto getDto = userMapper.toDto(user);

        getDto.setPassword(encryptor.gerDecryptedPassword(getDto.getPassword()));

        return getDto;

    }


    public LoginBackDto login( LoginDto dto) {

        LoginBackDto loginBackDto = new LoginBackDto();

        Optional<User> gecici = userRepository.findByUsername(dto.getUsername());

        String decryptedPassword = encryptor.gerDecryptedPassword(gecici.get().getPassword());

        if (dto.getPassword().equals(decryptedPassword)) {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));


            loginBackDto.setId(gecici.get().getId());
            loginBackDto.setRole(gecici.get().getRole());
            loginBackDto.setToken(jwtService.generateToken(dto.getUsername(), userRepository.findByUsername(
                    dto.getUsername()).get().getRole().toString()));

            return loginBackDto;
        }
        return null;
    }


    public List<UserDto> listAllKullanici() {

        return userMapper.toDtoListFromEntity(userRepository.findAllByIsOnlineIsTrue());

    }
}

