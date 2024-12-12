package service.user_service.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import service.user_service.dto.ResponseDto;
import service.user_service.dto.RoleDto;
import service.user_service.dto.SearchDto;
import service.user_service.service.RoleService;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/role/add")
    public ResponseDto<RoleDto> create(@RequestBody @Valid RoleDto roleDto) throws IOException {
        roleService.create(roleDto);
        return ResponseDto.<RoleDto>builder().code(String.valueOf(HttpStatus.OK.value())).data(roleDto).build();
    }

    @DeleteMapping("/role/delete/{id}")
    public ResponseDto<Void> delete(@PathVariable(value = "id") int id) {
        roleService.delete(id);
        return ResponseDto.<Void>builder().message("done") .code(String.valueOf(HttpStatus.OK.value())).build();
    }
    @PostMapping("/role/search")
    public ResponseDto<List<RoleDto>> search(@RequestBody @Valid SearchDto searchDTO) {
        return roleService.find(searchDTO);
    }
    @PutMapping("/role/update")
    public ResponseDto<Void> update(@Valid @RequestBody RoleDto roleDTO) {
        roleService.update(roleDTO);
        return ResponseDto.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }

}
