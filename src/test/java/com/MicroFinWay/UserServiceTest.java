package com.MicroFinWay;

import com.MicroFinWay.dto.UserDTO;
import com.MicroFinWay.model.User;
import com.MicroFinWay.model.enums.UserType;
import com.MicroFinWay.repository.UserRepository;
import com.MicroFinWay.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_Success() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFullName("John Doe");
        userDTO.setPhoneMobile("+123456789");
        userDTO.setAddress("Some Street");
        userDTO.setUserType(UserType.valueOf("Физическое лицо"));

        User lastUser = new User();
        lastUser.setId(10L);
        lastUser.setKod("99000010");

        when(userRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(lastUser));

        User savedUser = new User();
        savedUser.setId(11L);
        savedUser.setKod("99000011");
        savedUser.setFullName("John Doe");
        savedUser.setPhoneMobile("+123456789");
        savedUser.setAddress("Some Street");
        savedUser.setUserType(UserType.valueOf("Юридическое лицо"));

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDTO result = userService.createUser(userDTO);

        assertNotNull(result);
        assertEquals("99000011", result.getKod());
        assertEquals("John Doe", result.getFullName());

        verify(userRepository).findTopByOrderByIdDesc();
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUserById_Success() {
        User user = new User();
        user.setId(1L);
        user.setKod("99000001");
        user.setFullName("Alice Smith");
        user.setPhoneMobile("+987654321");
        user.setAddress("Another Street");
        user.setUserType(UserType.valueOf("Физическое лицо"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("99000001", result.getKod());
        assertEquals("Alice Smith", result.getFullName());

        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(1L));

        verify(userRepository).findById(1L);
    }
}

