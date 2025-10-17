package com.MicroFinWay.service;

import com.MicroFinWay.dto.UserDTO;
import com.MicroFinWay.model.User;
import com.MicroFinWay.model.enums.UserType;
import com.MicroFinWay.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


/**
 * Service class for managing user-related operations such as creating
 * users and retrieving user details.
 */
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

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByType(String type) {
        UserType userType = UserType.valueOf(type.toUpperCase());
        return userRepository.findByUserType(userType);
    }

    @Transactional
    public User update(Long id, User updated) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + id));

        // Общие поля
        existing.setFullName(updated.getFullName());
        existing.setPhoneMobile(updated.getPhoneMobile());
        existing.setAddress(updated.getAddress());
        existing.setUserType(updated.getUserType());
        existing.setNotes(updated.getNotes());
        existing.setStatus(updated.getStatus());
        existing.setUpdatedAt(LocalDateTime.now());

        // Для физ. лица
        if (updated.getUserType() != null && updated.getUserType().name().equalsIgnoreCase("INDIVIDUAL")) {
            existing.setPassportSeries(updated.getPassportSeries());
            existing.setPassportNumber(updated.getPassportNumber());
            existing.setPassportIssuedDate(updated.getPassportIssuedDate());
            existing.setPassportIssuedBy(updated.getPassportIssuedBy());
            existing.setBirthDate(updated.getBirthDate());
            existing.setPersonalIdentificationNumber(updated.getPersonalIdentificationNumber());
            existing.setGender(updated.getGender());
            existing.setEmploymentStatus(updated.getEmploymentStatus());
            existing.setSocialSecurityNumber(updated.getSocialSecurityNumber());
        }

        // Для юр. лица
        if (updated.getUserType() != null && updated.getUserType().name().equalsIgnoreCase("LEGAL")) {
            existing.setShortName(updated.getShortName());
            existing.setOkonx(updated.getOkonx());
            existing.setRegistrationNumber(updated.getRegistrationNumber());
            existing.setRegistrationDate(updated.getRegistrationDate());
            existing.setDirectorName(updated.getDirectorName());
            existing.setAccountantName(updated.getAccountantName());
            existing.setOwnershipType(updated.getOwnershipType());
            existing.setOpf(updated.getOpf());
            existing.setLegalAddress(updated.getLegalAddress());
            existing.setDirectorPassportSeries(updated.getDirectorPassportSeries());
            existing.setDirectorPassportNumber(updated.getDirectorPassportNumber());
            existing.setDirectorBirthDate(updated.getDirectorBirthDate());
            existing.setDirectorIdentificationNumber(updated.getDirectorIdentificationNumber());
        }

        // Банковские реквизиты
        existing.setBankCardNumber(updated.getBankCardNumber());
        existing.setBankCardExpirationDate(updated.getBankCardExpirationDate());
        existing.setBankCardHolderName(updated.getBankCardHolderName());
        existing.setBankCardType(updated.getBankCardType());
        existing.setBankName(updated.getBankName());
        existing.setBankBic(updated.getBankBic());
        existing.setBankCorrespondentAccount(updated.getBankCorrespondentAccount());

        // География
        existing.setCity(updated.getCity());
        existing.setDistrict(updated.getDistrict());
        existing.setRegion(updated.getRegion());
        existing.setCityCode(updated.getCityCode());
        existing.setDistrictCode(updated.getDistrictCode());
        existing.setRegionCode(updated.getRegionCode());
        existing.setKatm_sir(updated.getKatm_sir());

        return userRepository.save(existing);
    }
}
