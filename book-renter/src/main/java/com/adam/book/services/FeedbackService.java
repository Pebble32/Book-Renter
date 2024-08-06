package com.adam.book.services;

import com.adam.book.controllers.dtos.FeedbackRequest;
import com.adam.book.controllers.dtos.FeedbackResponse;
import com.adam.book.repositories.BookRepository;
import com.adam.book.repositories.FeedbackRepository;
import com.adam.book.repositories.entities.BookEntity;
import com.adam.book.repositories.entities.FeedbackEntity;
import com.adam.book.repositories.entities.UserEntity;
import com.adam.book.repositories.entities.common.PageResponse;
import com.adam.book.services.converters.FeedbackConverter;
import com.adam.book.services.handlers.OperationNotPermittedException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final BookRepository bookRepository;
    private final FeedbackConverter feedbackConverter;
    private final FeedbackRepository feedbackRepository;

    public Long saveFeedback(FeedbackRequest request, Authentication connectedUser) {
        BookEntity bookEntity = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID::" + request.bookId()));
        if (bookEntity.isArchived() || !bookEntity.isSharable()) {
            throw new OperationNotPermittedException("Can't give feedback to archived book");
        }
        UserEntity userEntity = ((UserEntity) connectedUser.getPrincipal());
        if (Objects.equals(userEntity.getId(), bookEntity.getOwner().getId())) {
            throw new OperationNotPermittedException("Can't give feedback to your own book");
        }
        FeedbackEntity feedbackEntity = feedbackConverter.toFeedback(request);

        return feedbackRepository.save(feedbackEntity).getId();
    }

    public PageResponse<FeedbackResponse> findAllFeedbackByBook(Long bookId, int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        UserEntity userEntity = ((UserEntity) connectedUser.getPrincipal());
        Page<FeedbackEntity> feedbackEntities = feedbackRepository.findAllByBookId(bookId, pageable);
        List<FeedbackResponse> feedbackResponses = feedbackEntities.stream()
                .map(f -> feedbackConverter.toFeedbackResponse(f, userEntity.getId()))
                .toList();

        return new PageResponse<>(
                feedbackResponses,
                feedbackEntities.getNumber(),
                feedbackEntities.getSize(),
                feedbackEntities.getTotalElements(),
                feedbackEntities.getTotalPages(),
                feedbackEntities.isFirst(),
                feedbackEntities.isLast()
        );
    }
}
