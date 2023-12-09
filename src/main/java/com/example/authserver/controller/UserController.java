package com.example.authserver.controller;

import com.example.authserver.config.JwtService;
import com.example.authserver.dto.LoginBackDto;
import com.example.authserver.dto.LoginDto;
import com.example.authserver.dto.UserDto;
import com.example.authserver.dto.UserSaveRequestDto;
import com.example.authserver.entity.User;
import com.example.authserver.repository.UserRepository;
import com.example.authserver.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {


    private final UserService userService;
    private final UserRepository userRepository;
    @Autowired
    private JwtService jwtService;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/save")
    public ResponseEntity<UserDto> save(@RequestBody UserSaveRequestDto dto) {

        UserDto userDto = userService.save(dto);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginBackDto> login(@RequestBody LoginDto dto) {
        Optional<User> gecici = Optional.ofNullable(userRepository.findByUsername(dto.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        ));

        LoginBackDto loginBackDto = userService.login(dto);


        return new ResponseEntity<>(loginBackDto, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " ifadesini çıkartıyoruz
            jwtService.invalidateToken(token); // Tokenı geçersiz kılma
            return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid token", HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/activeusers")
    public ResponseEntity<List<UserDto>> getAllKullanici(HttpServletRequest request) throws Exception {

        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " ifadesini çıkartıyoruz
            if (jwtService.tokenValidate(token)) {

                // Token'dan rolü çıkartarak işlem yapma
                String role = jwtService.getRoleToken(token);

                if (role.equalsIgnoreCase("ROLE_ADMIN")) {
                    // OGRENCI rolüne sahip kullanıcı için id çıkartma
                    return new ResponseEntity<>(userService.listAllKullanici(), HttpStatus.OK);
                } else {
                    throw new Exception();
                }


            } else {
                // Token doğrulama hatası veya token süresi dolmuşsa işlem yapabilirsiniz
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } else {
            // Token başlıkta yok veya uygun formatta değilse işlem yapabilirsiniz
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



}
