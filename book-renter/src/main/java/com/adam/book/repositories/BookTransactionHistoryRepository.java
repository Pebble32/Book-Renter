package com.adam.book.repositories;

import com.adam.book.repositories.entities.BookTransactionHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistoryEntity, Long> {
    @Query("""
           SELECT history
           FROM BookTransactionHistoryEntity history
           WHERE history.user.id = :userId
           """)
    Page<BookTransactionHistoryEntity> findAllBorrowedBooks(Pageable pageable, Long userId);

    @Query("""
           SELECT history
           FROM BookTransactionHistoryEntity history
           WHERE history.book.owner.id = :userId
           """)
    Page<BookTransactionHistoryEntity> findAllReturnedBooks(Pageable pageable, Long userId);
}
