package com.hagz.blog.repository;

import com.hagz.blog.model.Post;
import java.util.List;
import java.util.Optional;

import com.hagz.blog.model.Tag;
import com.hagz.blog.payload.response.PostCardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import com.hagz.blog.model.Comment;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Override
    Optional<Post> findById(Long aLong);

    @Query("SELECT new com.hagz.blog.payload.response.PostCardDTO(" +
            "p.id, p.slug, p.imageUrl, t.name, p.shortDesc, p.username, " +
            "COUNT(c.id)) " +
            "FROM Post p " +
            "LEFT JOIN p.topic t " +
            "LEFT JOIN p.comments c " +
            "GROUP BY p.id, p.slug, p.imageUrl, t.name, p.shortDesc, p.username " +
            "ORDER BY p.createdOn DESC")
    Page<PostCardDTO> findPostCards(Pageable pageable);

    @Query("SELECT new com.hagz.blog.payload.response.PostCardDTO(" +
            "p.id, p.slug, p.imageUrl, t.name, p.shortDesc, p.username, " +
            "CAST(COUNT(DISTINCT c.id) AS long)) " +
            "FROM Post p " +
            "LEFT JOIN p.topic t " +
            "LEFT JOIN p.tags tag " +
            "LEFT JOIN p.comments c " +
            "WHERE (:username IS NULL OR p.username = :username) " +
            "AND (:topicName IS NULL OR t.name = :topicName) " +
            "AND (:tagName IS NULL OR tag.name = :tagName) " +
            "GROUP BY p.id, p.slug, p.imageUrl, t.name, p.shortDesc, p.username, p.createdOn " +
            "ORDER BY p.createdOn DESC")
    Page<PostCardDTO> findPostCardsWithFilters(
            @Param("username") String username,
            @Param("topicName") String topicName,
            @Param("tagName") String tagName,
            Pageable pageable
    );

    /**
     * Find related post IDs only (for two-step fetch to avoid GROUP BY conflicts)
     * Step 1: Get IDs sorted by relevance
     */
    @Query("SELECT p.id FROM Post p " +
            "JOIN p.tags t " +
            "WHERE t.id IN (" +
            "  SELECT t2.id FROM Post p2 " +
            "  JOIN p2.tags t2 " +
            "  WHERE p2.id = :postId" +
            ") " +
            "AND p.id != :postId " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(t.id) DESC")
    List<Long> findRelatedPostIdsByTags(@Param("postId") Long postId,
                                        Pageable pageable);

    /**
     * Step 2: Fetch full posts with all associations loaded
     * Uses EntityGraph to avoid N+1 queries
     */
    @EntityGraph(attributePaths = {"comments", "tags", "topic"})
    @Query("SELECT p FROM Post p WHERE p.id IN :postIds")
    List<Post> findPostsWithAssociationsByIds(@Param("postIds") List<Long> postIds);



}
