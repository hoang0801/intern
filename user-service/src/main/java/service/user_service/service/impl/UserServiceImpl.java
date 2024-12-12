package service.user_service.service.impl;

import jakarta.persistence.NoResultException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.user_service.dto.ResponseDto;
import service.user_service.dto.SearchDto;
import service.user_service.dto.UserDto;
import service.user_service.entity.User;
import service.user_service.repo.UserRepository;
import service.user_service.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Override
    public void create(UserDto userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Tên tài khoản đã tồn tại: " + userDTO.getUsername());
        }
        User user = new ModelMapper().map(userDTO, User.class);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void update(UserDto userDTO) {
        User user = userRepository.findById(userDTO.getId()).orElseThrow(NoResultException::new);
        user.setName(userDTO.getName());
        user.setBirthdate(userDTO.getBirthdate());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setAddress(userDTO.getAddress());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy người dùng với ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDto getLoginCurrent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        Integer userId = Integer.valueOf(authentication.getName());

        return userRepository.findById(userId)
                .map(this::convert)
                .orElseThrow(NoResultException::new);
    }

    public User findByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<UserDto> findById(Integer id) {
        return userRepository.findById(id)
                .map(this::convert);
    }

    @Override
    public UserDto getById(int id) {
        User user = userRepository.findById(id).orElseThrow(NoResultException::new);
        return convert(user);
    }

    @Override
    public ResponseDto<List<UserDto>> find(SearchDto searchDTO) {
        List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList).stream()
                .map(order -> {
                    if (order.getOrder().equals(SearchDto.ASC))
                        return Sort.Order.asc(order.getProperty());

                    return Sort.Order.desc(order.getProperty());
                }).collect(Collectors.toList());

        Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

        Page<User> page = userRepository.searchByName(searchDTO.getValue(), pageable);

        @SuppressWarnings("unchecked")
        ResponseDto<List<UserDto>> responseDTO = new ModelMapper().map(page, ResponseDto.class);
        responseDTO.setData(page.get().map(user -> convert(user)).collect(Collectors.toList()));
        return responseDTO;
    }

    private UserDto convert(User user) {
        return new ModelMapper().map(user, UserDto.class);
    }

}
