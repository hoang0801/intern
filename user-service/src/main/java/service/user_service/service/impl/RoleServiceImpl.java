package service.user_service.service.impl;

import jakarta.persistence.NoResultException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.user_service.dto.ResponseDto;
import service.user_service.dto.RoleDto;
import service.user_service.dto.SearchDto;
import service.user_service.entity.Role;
import service.user_service.repo.RoleRepository;
import service.user_service.service.RoleService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepo;


    @Override
    @Transactional
    public void create(RoleDto roleDTO) {
        ModelMapper mapper = new ModelMapper();
        Role role = mapper.map(roleDTO, Role.class);
        roleRepo.save(role);
        roleDTO.setId(role.getId()); // trả về ID sau khi tạo
    }

    @Override
    @Transactional
    public void update(RoleDto roleDTO) {
        Role role = roleRepo.findById(roleDTO.getId()).orElseThrow(NoResultException::new);
        role.setName(roleDTO.getName());
        roleRepo.save(role);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        roleRepo.deleteById(id);
    }


    @Override
    public ResponseDto<List<RoleDto>> find(SearchDto searchDTO) {
        List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList).stream()
                .map(order -> {
                    if (order.getOrder().equals(SearchDto.ASC))
                        return Sort.Order.asc(order.getProperty());

                    return Sort.Order.desc(order.getProperty());
                }).collect(Collectors.toList());

        Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

        Page<Role> page = roleRepo.searchByName(searchDTO.getValue(), pageable);

        @SuppressWarnings("unchecked")
        ResponseDto<List<RoleDto>> responseDTO = new ModelMapper().map(page, ResponseDto.class);
        responseDTO.setData(page.get().map(role -> convert(role)).collect(Collectors.toList()));
        return responseDTO;
    }

    private RoleDto convert(Role role) {
        return new ModelMapper().map(role, RoleDto.class);
    }

}
