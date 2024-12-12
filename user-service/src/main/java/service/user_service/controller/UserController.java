package service.user_service.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.user_service.dto.ResponseDto;
import service.user_service.dto.SearchDto;
import service.user_service.dto.UserDto;
import service.user_service.jwt.JwtTokenService;
import service.user_service.service.UserService;

import java.io.IOException;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    private final JwtTokenService jwtTokenService;

    public UserController(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

//    @PostMapping("/validate")
//    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
//        boolean isValid = jwtTokenService.validateToken(token);
//        return ResponseEntity.ok(isValid);
//    }

    @PostMapping("/add")
    public ResponseDto<UserDto> create(@RequestBody @Valid UserDto userDTO) throws IOException {
        userService.create(userDTO);
        return ResponseDto.<UserDto>builder().code(String.valueOf(HttpStatus.OK.value())).data(userDTO).build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto<Void> delete(@PathVariable(value = "id") int id) {
        userService.delete(id);
        return ResponseDto.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }
    @GetMapping("/get-user/{id}")
    public ResponseDto<UserDto> getById(@PathVariable("id") int id){
        return ResponseDto.<UserDto>builder().data(userService.getById(id)).build();
    }

    @PostMapping("/search")
    public ResponseDto<List<UserDto>> search(@RequestBody @Valid SearchDto searchDTO) {
        return userService.find(searchDTO);
    }

    @PutMapping("/update")
    public ResponseDto<Void> update(@Valid @RequestBody UserDto userDTO) {
        userService.update(userDTO);
        return ResponseDto.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }
    }
