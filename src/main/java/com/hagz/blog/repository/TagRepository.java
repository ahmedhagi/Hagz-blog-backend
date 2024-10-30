package com.hagz.blog.repository;



import com.hagz.blog.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * Returns tag if exists given a valid tag name
     * @param name - valid a tag name
     * @return tag associated with role name or return nall
     */
    Optional<Tag> findByName(String name);

    

}
