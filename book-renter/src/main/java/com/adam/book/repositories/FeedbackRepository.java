package com.adam.book.repositories;

import com.adam.book.repositories.entities.FeedbackEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long> {

    @Query("""
           SELECT feedback 
           FROM FeedbackEntity feedback
           WHERE feedback.book.id = :bookId
           """)
    Page<FeedbackEntity> findAllByBookId(Long bookId, Pageable pageable);
}
