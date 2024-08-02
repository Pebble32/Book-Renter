package com.adam.book.services;

import com.adam.book.controllers.dtos.BookRequest;
import com.adam.book.controllers.dtos.BookResponse;
import com.adam.book.repositories.BookRepository;
import com.adam.book.repositories.entities.BookEntity;
import com.adam.book.repositories.entities.UserEntity;
import com.adam.book.repositories.entities.common.PageResponse;
import com.adam.book.services.converters.BookConverter;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.adam.book.controllers.dtos.BookSpecification.withOwnerId;

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

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        UserEntity userEntity = ((UserEntity) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookEntity> bookEntities = bookRepository.findAllDisplayableBooks(pageable, userEntity.getId());
        List<BookResponse> bookResponses = bookEntities.stream()
                .map(bookConverter::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                bookEntities.getNumber(),
                bookEntities.getSize(),
                bookEntities.getTotalElements(),
                bookEntities.getTotalPages(),
                bookEntities.isFirst(),
                bookEntities.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        UserEntity userEntity = ((UserEntity) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookEntity> bookEntities = bookRepository.findAll(withOwnerId(userEntity.getId()), pageable);
        List<BookResponse> bookResponses = bookEntities.stream()
                .map(bookConverter::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                bookEntities.getNumber(),
                bookEntities.getSize(),
                bookEntities.getTotalElements(),
                bookEntities.getTotalPages(),
                bookEntities.isFirst(),
                bookEntities.isLast()
        );
    }
}
