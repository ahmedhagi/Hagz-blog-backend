package com.hagz.blog.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hagz.blog.model.User;

/*
User repository for persisting and accessing user data from the role entity
*/
public interface UserRepository  extends JpaRepository<User, Long > {

    /**
     * Returns User if exists given a valid username
     * @param username - valid a username
     * @return user associated with username or return nall
    */
    Optional<User> findByUsername(String username);

    /**
     * Returns a boolean value if User exists given a valid username
     * @param username  username that potentially exists in the entity
     * @return True if user with the given email exists or False
     */
    Boolean existsByUsername(String username);

    /**
     * Returns a boolean value if User exists given a valid email
     * @param email email that potentially exists in the entity
     * @return True if user with the given email exists or False
     */
    Boolean existsByEmail(String email);


}
