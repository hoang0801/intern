package service.user_service.service;



import service.user_service.dto.ResponseDto;
import service.user_service.dto.RoleDto;
import service.user_service.dto.SearchDto;

import java.util.List;


public interface RoleService {

    void create(RoleDto roleDTO);

    void update(RoleDto roleDTO);

    void delete(Integer id);

    ResponseDto<List<RoleDto>> find(SearchDto searchDTO);
}
