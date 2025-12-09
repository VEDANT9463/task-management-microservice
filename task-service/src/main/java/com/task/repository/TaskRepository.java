package com.task.repository;

import com.task.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserId(Long userId);

    Optional<Task> findByIdAndDeletedFalse(Long id);

    Page<Task> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);

    Page<Task> findByUserIdAndCompletedAndDeletedFalse(Long userId, boolean completed, Pageable pageable);

    @Query("""
           SELECT t FROM Task t
           WHERE t.deleted = false
             AND t.userId = :userId
             AND (
                 LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                 OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
             )
           """)
    Page<Task> searchTasks(@Param("userId") Long userId,
                           @Param("keyword") String keyword,
                           Pageable pageable);

    List<Task> findByUserIdAndDeletedFalse(Long userId);
}
