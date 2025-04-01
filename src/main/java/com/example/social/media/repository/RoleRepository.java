package com.example.social.media.repository;

import com.example.social.media.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role , String> {
    Set<Role> findRoleByName(String user);
}
