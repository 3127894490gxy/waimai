package com.waimai.repository;

import com.waimai.entity.User;
import com.waimai.common.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    List<User> findByRole(UserRole role);
    Optional<User> findByIdAndRole(Long id, UserRole role);
    List<User> findByNicknameContaining(String nickname);
    List<User> findByPhone(String phone);
    boolean existsByPhone(String phone);
}
