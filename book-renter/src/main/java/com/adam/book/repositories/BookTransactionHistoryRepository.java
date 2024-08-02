package com.adam.book.repositories;

import com.adam.book.repositories.entities.BookTransactionHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

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

    @Query("""
           SELECT (COUNT(*) > 0) AS isBorrowed
           FROM BookTransactionHistoryEntity history
           WHERE history.user.id = :userId
           AND history.book.id = :bookId
           AND history.returnApproved = false
           """)
    boolean isBorrowedByUser(Long bookId, Long userId);

    @Query("""
          SELECT history
          FROM BookTransactionHistoryEntity history
          WHERE history.user.id = :userId
          AND history.book.id = :bookId
          AND history.returned = false
          AND history.returnApproved = false
          """)
    Optional<BookTransactionHistoryEntity> findByBookIdAndUserId(Long bookId, Long userId);


    @Query("""
          SELECT history
          FROM BookTransactionHistoryEntity history
          WHERE history.book.owner.id = :userId
          AND history.book.id = :bookId
          AND history.returned = true
          AND history.returnApproved = false
          """)
    Optional<BookTransactionHistoryEntity> findByBookIdAndOwnerId(Long bookId, Long userId);
}
