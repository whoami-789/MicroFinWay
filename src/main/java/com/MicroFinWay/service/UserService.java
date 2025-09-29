package com.MicroFinWay.service;

import com.MicroFinWay.dto.UserDTO;
import com.MicroFinWay.model.User;
import com.MicroFinWay.repository.ClientSearchRepository;
import com.MicroFinWay.repository.UserRepository;
import com.MicroFinWay.search.ClientIndex;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


/**
 * Service class for managing user-related operations such as creating
 * users and retrieving user details.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ClientSearchRepository clientSearchRepository;

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
        BeanUtils.copyProperties(userDTO, user);
        user.setKod(nextKod);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());



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

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        reindexAllClients();
    }

    public void reindexAllClients() {
        var users = userRepository.findAll();
        var indexList = users.stream()
                .map(u -> new ClientIndex(
                        u.getKod(),
                        u.getFullName(),
                        u.getPassportNumber(),
                        u.getPhoneMobile()
                ))
                .toList();
        clientSearchRepository.saveAll(indexList);
    }

}
