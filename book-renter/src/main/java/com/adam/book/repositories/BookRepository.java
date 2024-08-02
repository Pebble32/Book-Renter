package com.adam.book.repositories;

import com.adam.book.repositories.entities.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    @Query("""
            SELECT book
            FROM BookEntity book
            WHERE book.archived = false
            AND book.sharable = true
            AND book.owner.id = :userId
            """)
    Page<BookEntity> findAllDisplayableBooks(Pageable pageable, Long userId);
}
