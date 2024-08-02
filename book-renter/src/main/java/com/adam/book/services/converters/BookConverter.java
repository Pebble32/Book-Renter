package com.adam.book.services.converters;

import com.adam.book.controllers.dtos.BookRequest;
import com.adam.book.controllers.dtos.BookResponse;
import com.adam.book.repositories.entities.BookEntity;
import org.springframework.stereotype.Service;

@Service
public class BookConverter {


    public BookEntity toBook(BookRequest bookRequest) {
        return BookEntity.builder()
                .id(bookRequest.id())
                .title(bookRequest.title())
                .author(bookRequest.authorName())
                .synopsis(bookRequest.synopsis())
                .isbn(bookRequest.isbn())
                .archived(false)
                .sharable(bookRequest.shareable())
                .build();
    }

    public BookResponse toBookResponse(BookEntity bookEntity) {
        return BookResponse.builder()
                .id(bookEntity.getId())
                .title(bookEntity.getTitle())
                .author(bookEntity.getAuthor())
                .synopsis(bookEntity.getSynopsis())
                .isbn(bookEntity.getIsbn())
                .archived(bookEntity.isArchived())
                .shareable(bookEntity.isSharable())
                .owner(bookEntity.getOwner().getFullName())
                //.cover()
                .build();
    }
}
