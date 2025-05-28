package com.MicroFinWay.service;

import com.MicroFinWay.dto.UserDTO;
import com.MicroFinWay.model.User;
import com.MicroFinWay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDTO createUser(UserDTO userDTO) {
        // Найти последнего клиента
        Optional<User> lastUser = userRepository.findTopByOrderByIdDesc();
        String nextKod;
        if (lastUser.isPresent()) {
            String lastKod = lastUser.get().getKod();
            int lastNumber = Integer.parseInt(lastKod.substring(4));
            nextKod = String.format("9900%04d", lastNumber + 1);
        } else {
            nextKod = "99000001";
        }

        // Создание User из DTO
        User user = new User();
        user.setFullName(userDTO.getFullName());
        user.setPhoneMobile(userDTO.getPhoneMobile());
        user.setAddress(userDTO.getAddress());
        user.setUserType(userDTO.getUserType());
        user.setKod(nextKod);
        user.setCreatedAt(java.time.LocalDateTime.now());

        User savedUser = userRepository.save(user);

        // Возвращаем DTO
        return toUserDTO(savedUser);
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));
        return toUserDTO(user);
    }

    private UserDTO toUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setKod(user.getKod());
        dto.setFullName(user.getFullName());
        dto.setPhoneMobile(user.getPhoneMobile());
        dto.setAddress(user.getAddress());
        dto.setUserType(user.getUserType());
        return dto;
    }

}
