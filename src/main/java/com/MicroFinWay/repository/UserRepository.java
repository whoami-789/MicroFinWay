package com.MicroFinWay.repository;

import com.MicroFinWay.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Можно добавить поиск по ИНН, коду, телефону и т.д. при необходимости
}
