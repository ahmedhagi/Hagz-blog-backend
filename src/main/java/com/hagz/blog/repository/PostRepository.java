package com.hagz.blog.repository;

import com.hagz.blog.model.Post;
import java.util.List;
import java.util.Optional;

import com.hagz.blog.model.Tag;
import com.hagz.blog.payload.response.PostCardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.hagz.blog.model.Comment;
import org.springframework.stereotype.Repository;

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

    @Query("SELECT DISTINCT new com.hagz.blog.payload.response.PostCardDTO(" +
            "p.id, p.slug, p.imageUrl, t.name, p.shortDesc, p.username, " +
            "CAST(COUNT(DISTINCT c.id) AS long)) " +
            "FROM Post p " +
            "LEFT JOIN p.topic t " +
            "LEFT JOIN p.tags tag " +
            "LEFT JOIN p.comments c " +
            "WHERE (:username IS NULL OR p.username = :username) " +
            "AND (:topicName IS NULL OR t.name = :topicName) " +
            "AND (:tagName IS NULL OR tag.name = :tagName) " +
            "GROUP BY p.id, p.slug, p.imageUrl, t.name, p.shortDesc, p.username " +
            "ORDER BY p.createdOn DESC")
    Page<PostCardDTO> findPostCardsWithFilters(
            @Param("username") String username,
            @Param("topicName") String topicName,
            @Param("tagName") String tagName,
            Pageable pageable
    );

    /**
     * Find related posts based on shared tags.
     * Returns posts ordered by number of matching tags (relevance).
     * Excludes the current post.
     */
    @Query("SELECT DISTINCT p FROM Post p " +
            "JOIN p.tags t " +
            "WHERE t.id IN (" +
            "  SELECT t2.id FROM Post p2 " +
            "  JOIN p2.tags t2 " +
            "  WHERE p2.id = :postId" +
            ") " +
            "AND p.id != :postId " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(t.id) DESC")
    List<Post> findRelatedPostsByTags(@Param("postId") Long postId);

    /**
     * Find related post IDs only (for two-step fetch to avoid GROUP BY conflicts)
     * Step 1: Get IDs sorted by relevance
     */
    @Query("SELECT DISTINCT p.id FROM Post p " +
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
                                        org.springframework.data.domain.Pageable pageable);

    /**
     * Step 2: Fetch full posts with all associations loaded
     * Avoids N+1 queries by eagerly loading comments, tags, and topic
     */
    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN FETCH p.comments " +
            "LEFT JOIN FETCH p.tags " +
            "LEFT JOIN FETCH p.topic " +
            "WHERE p.id IN :postIds")
    List<Post> findPostsWithAssociationsByIds(@Param("postIds") List<Long> postIds);



}
