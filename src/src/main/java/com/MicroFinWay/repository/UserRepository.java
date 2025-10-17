package com.MicroFinWay.repository;

import com.MicroFinWay.model.User;
import com.MicroFinWay.model.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u ORDER BY u.id DESC LIMIT 1")
    Optional<User> findTopByOrderByIdDesc();

    Optional<User> findByKod(String kod);

    List<User> findByUserType(UserType userType);
}
