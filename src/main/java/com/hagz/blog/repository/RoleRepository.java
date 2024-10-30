package com.hagz.blog.repository;

import java.util.Optional;

import com.hagz.blog.model.ERole;
import com.hagz.blog.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
Role repository for persisting and accessing role data from the role entity
*/
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Returns Role if exists given a valid role name
     * @param name - valid a role name
     * @return role associated with role name or return nall
     */
    Optional<Role> findByName(ERole name);
}
