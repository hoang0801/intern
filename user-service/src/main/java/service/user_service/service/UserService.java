package service.user_service.service;


import service.user_service.dto.ResponseDto;
import service.user_service.dto.SearchDto;
import service.user_service.dto.UserDto;
import service.user_service.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void create(UserDto userDTO);

    void update(UserDto userDTO);
    UserDto getById(int id);

    void delete(Integer id);
    User findByUsername(String username);

    UserDto getLoginCurrent();

    ResponseDto<List<UserDto>> find(SearchDto searchDTO);
    Optional<UserDto> findById(Integer id);

}
