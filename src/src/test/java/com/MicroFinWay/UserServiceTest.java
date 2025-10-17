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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_ShouldGenerateKodAndSaveUser() {
        // arrange: создаем входной DTO
        UserDTO userDTO = new UserDTO();
        userDTO.setFullName("Иван Иванов");
        userDTO.setPhoneMobile("998901112233");
        userDTO.setPhoneHome("998712345678");
        userDTO.setAddress("Ташкент");
        userDTO.setUserType(UserType.valueOf("INDIVIDUAL"));
        userDTO.setNotes("Новый клиент");
        userDTO.setStatus(1);
        userDTO.setInn("123456789");
        userDTO.setPassportSeries("AA");
        userDTO.setPassportNumber("1234567");
        userDTO.setPassportIssuedDate(LocalDate.of(2015, 6, 1));
        userDTO.setPassportIssuedBy("ОВД Юнусабад");
        userDTO.setBirthDate(LocalDate.of(1990, 1, 15));
        userDTO.setPersonalIdentificationNumber("12345678901234");

        // инициализация, как будто это первый пользователь
        when(userRepository.findTopByOrderByIdDesc()).thenReturn(Optional.empty());

        // мокаем возврат сохраненного пользователя
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // act
        UserDTO result = userService.createUser(userDTO);

        // assert
        assertNotNull(result);
        assertEquals("Иван Иванов", result.getFullName());
        assertEquals("99000001", result.getKod());  // ожидаем первый сгенерированный код
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_ShouldGenerateNextKodFromLastUser() {
        // arrange
        User lastUser = new User();
        lastUser.setKod("99001234");
        when(userRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(lastUser));

        UserDTO userDTO = new UserDTO();
        userDTO.setFullName("Василий Петров");
        userDTO.setPhoneMobile("998901122334");
        userDTO.setPhoneHome("998712345678");
        userDTO.setAddress("Самарканд");
        userDTO.setUserType(UserType.INDIVIDUAL);
        userDTO.setNotes("Следующий клиент");
        userDTO.setStatus(1);
        userDTO.setInn("987654321");
        userDTO.setPassportSeries("BB");
        userDTO.setPassportNumber("7654321");
        userDTO.setPassportIssuedDate(LocalDate.of(2012, 5, 10));
        userDTO.setPassportIssuedBy("ОВД Чиланзар");
        userDTO.setBirthDate(LocalDate.of(1985, 12, 5));
        userDTO.setPersonalIdentificationNumber("99887766554433");

        // мок возврата сохраненного пользователя
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // act
        UserDTO result = userService.createUser(userDTO);

        // assert
        assertNotNull(result);
        assertEquals("99001235", result.getKod());  // проверка генерации кода
        assertEquals("Василий Петров", result.getFullName());
        assertEquals("998901122334", result.getPhoneMobile());
        assertEquals("Самарканд", result.getAddress());
        assertEquals(UserType.INDIVIDUAL, result.getUserType());

        verify(userRepository, times(1)).save(any(User.class));
    }

}
