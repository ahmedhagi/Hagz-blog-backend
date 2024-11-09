package com.hagz.blog.repository;



import com.hagz.blog.model.Tag;
import com.hagz.blog.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    Optional<Topic> findByNameIgnoreCase(String name);

    /**
     * Returns List of tags of the selected topic
     * @param topicName - name of topic
     * @return list of tags associated with topic
    **/
    @Query("SELECT tg FROM Topic tp JOIN tp.tags tg WHERE tp.name = :topicName ")
    List<Tag> getTagsFromTopic(@Param("topicName") String topicName);

}
