package com.adam.book.services;

import com.adam.book.controllers.dtos.BookRequest;
import com.adam.book.controllers.dtos.BookResponse;
import com.adam.book.repositories.BookRepository;
import com.adam.book.repositories.entities.BookEntity;
import com.adam.book.repositories.entities.UserEntity;
import com.adam.book.services.converters.BookConverter;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookConverter bookConverter;

    public Long save(BookRequest bookRequest, Authentication connectedUser) {
        UserEntity userEntity = ((UserEntity) connectedUser.getPrincipal());
        BookEntity bookEntity = bookConverter.toBook(bookRequest);
        bookEntity.setOwner(userEntity);
        return bookRepository.save(bookEntity).getId();
    }

    public BookResponse findById(Long bookId) {
        return bookRepository.findById(bookId)
                .map(bookConverter::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID:: " + bookId));
    }
}
