package com.adam.book.services;

import com.adam.book.controllers.dtos.BookRequest;
import com.adam.book.controllers.dtos.BookResponse;
import com.adam.book.controllers.dtos.BorrowedBookResponse;
import com.adam.book.repositories.BookRepository;
import com.adam.book.repositories.BookTransactionHistoryRepository;
import com.adam.book.repositories.entities.BookEntity;
import com.adam.book.repositories.entities.BookTransactionHistoryEntity;
import com.adam.book.repositories.entities.UserEntity;
import com.adam.book.repositories.entities.common.PageResponse;
import com.adam.book.services.converters.BookConverter;
import com.adam.book.services.handlers.OperationNotPermittedException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static com.adam.book.controllers.dtos.BookSpecification.withOwnerId;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookConverter bookConverter;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final FileStorageService fileStorageService;

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

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        UserEntity userEntity = ((UserEntity) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistoryEntity> allBorrowedBooks = bookTransactionHistoryRepository.findAllBorrowedBooks(pageable, userEntity.getId());
        List<BorrowedBookResponse> bookResponses = allBorrowedBooks.stream()
                .map(bookConverter::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        UserEntity userEntity = ((UserEntity) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistoryEntity> allBorrowedBooks = bookTransactionHistoryRepository.findAllReturnedBooks(pageable, userEntity.getId());
        List<BorrowedBookResponse> bookResponses = allBorrowedBooks.stream()
                .map(bookConverter::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public Long updateShareableStatus(long bookId, Authentication connectedUser) {
        BookEntity bookEntity = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID:: " + bookId));
        UserEntity userEntity = ((UserEntity) connectedUser.getPrincipal());
        if (!Objects.equals(bookEntity.getOwner().getId(), userEntity.getId())) {
            throw new OperationNotPermittedException("Only owner can update shareable status");
        }
        bookEntity.setSharable(!bookEntity.isSharable());
        bookRepository.save(bookEntity);
        return bookId;
    }

    public Long updateArchivedStatus(long bookId, Authentication connectedUser) {
        BookEntity bookEntity = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID:: " + bookId));
        UserEntity userEntity = ((UserEntity) connectedUser.getPrincipal());
        if (!Objects.equals(bookEntity.getOwner().getId(), userEntity.getId())) {
            throw new OperationNotPermittedException("Only owner can update archived status");
        }
        bookEntity.setArchived(!bookEntity.isArchived());
        bookRepository.save(bookEntity);
        return bookId;
    }

    public Long borrowBook(Long bookId, Authentication connectedUser) {
        BookEntity bookEntity = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID::" + bookId));

        if (bookEntity.isArchived() || !bookEntity.isSharable()) {
            throw new OperationNotPermittedException("Can't borrow book that is archived or not shareable");
        }
        UserEntity userEntity = ((UserEntity) connectedUser.getPrincipal());
        if (Objects.equals(bookEntity.getOwner().getId(), userEntity.getId())) {
            throw new OperationNotPermittedException("Can't borrow your own book");
        }
        final boolean isBorrowed = bookTransactionHistoryRepository.isBorrowedByUser(bookId, userEntity.getId());
        if (isBorrowed) {
            throw new OperationNotPermittedException("Requested book not available");
        }
        BookTransactionHistoryEntity bookTransactionHistoryEntity = BookTransactionHistoryEntity.builder()
                .user(userEntity)
                .book(bookEntity)
                .returned(false)
                .returnApproved(false)
                .build();

        return bookTransactionHistoryRepository.save(bookTransactionHistoryEntity).getId();
    }

    public Long returnBorrowedBook(Long bookId, Authentication connectedUser) {
        BookEntity bookEntity = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID::" + bookId));
        if (bookEntity.isArchived() || !bookEntity.isSharable()) {
            throw new OperationNotPermittedException("Can't return book that is archived or not shareable");
        }
        UserEntity userEntity = ((UserEntity) connectedUser.getPrincipal());
        if (Objects.equals(bookEntity.getOwner().getId(), userEntity.getId())) {
            throw new OperationNotPermittedException("Can't return your own book");
        }
        BookTransactionHistoryEntity bookTransactionHistoryEntity = bookTransactionHistoryRepository.findByBookIdAndUserId(bookId, userEntity.getId())
                .orElseThrow(()-> new OperationNotPermittedException("You did not borrow this book"));
        bookTransactionHistoryEntity.setReturned(true);
        return bookTransactionHistoryRepository.save(bookTransactionHistoryEntity).getId();
    }

    public Long returnApprovedBorrowedBook(Long bookId, Authentication connectedUser) {
        BookEntity bookEntity = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID::" + bookId));
        if (bookEntity.isArchived() || !bookEntity.isSharable()) {
            throw new OperationNotPermittedException("Can't return book that is not archived");
        }
        UserEntity userEntity = ((UserEntity) connectedUser.getPrincipal());
        if (Objects.equals(bookEntity.getOwner().getId(), userEntity.getId())) {
            throw new OperationNotPermittedException("Can't return your own book");
        }
        BookTransactionHistoryEntity bookTransactionHistoryEntity = bookTransactionHistoryRepository.findByBookIdAndOwnerId(bookId, userEntity.getId())
                .orElseThrow(() -> new OperationNotPermittedException("Can not approved return on not returned book"));
        bookTransactionHistoryEntity.setReturnApproved(true);
        return bookTransactionHistoryRepository.save(bookTransactionHistoryEntity).getId();
    }

    public void uploadBookCover(MultipartFile file, Authentication connectedUser, Long bookId) {
        BookEntity bookEntity = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID::" + bookId));
        UserEntity userEntity = ((UserEntity) connectedUser.getPrincipal());
        var bookCover = fileStorageService.saveFile(file, userEntity.getId());
        bookEntity.setBookCover(bookCover);
        bookRepository.save(bookEntity);
    }
}
