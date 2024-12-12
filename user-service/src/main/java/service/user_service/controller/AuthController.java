package service.user_service.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import service.user_service.dto.AuthRequestDto;
import service.user_service.dto.ResponseDto;
import service.user_service.jwt.JwtTokenService;
import service.user_service.repo.RoleRepository;
import service.user_service.service.UserService;

import java.util.List;


@RestController
public class AuthController {


    @Autowired
    @Lazy
    private UserService userService;


    @Autowired
    JwtTokenService jwtTokenService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepo;

    @PostMapping("/login")
    public ResponseDto<String> login(@RequestBody AuthRequestDto authRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword())
        );

        List<String> authorities = authentication.getAuthorities().stream()
                .map(e -> e.getAuthority()).toList();

        return ResponseDto.<String>builder()
                .data(jwtTokenService.createToken(authRequestDTO.getUsername(), authorities))
                .message("Đăng nhập thành công")
                .build();
    }

    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        boolean isValid = jwtTokenService.validateToken(token.replace("Bearer ", ""));
        return ResponseEntity.ok(isValid);
    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(@RequestHeader("Authorization") String tokenHeader) {
//        String responseMessage = jwtTokenService.logout(tokenHeader);
//        return ResponseEntity.ok(responseMessage);
//    }
}