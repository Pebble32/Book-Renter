package com.adam.book.services.converters;

import com.adam.book.controllers.dtos.FeedbackRequest;
import com.adam.book.controllers.dtos.FeedbackResponse;
import com.adam.book.repositories.entities.BookEntity;
import com.adam.book.repositories.entities.FeedbackEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedbackConverter {

    public FeedbackEntity toFeedback(FeedbackRequest request) {
        return FeedbackEntity.builder()
                .feedbackRating(request.rating())
                .comment(request.comment())
                .book(BookEntity.builder()
                        .id(request.bookId())
                        .build()
                )
                .build();
    }

    public FeedbackResponse toFeedbackResponse(FeedbackEntity feedbackEntity, Long id) {
        return FeedbackResponse.builder()
                .rating(feedbackEntity.getFeedbackRating())
                .comment(feedbackEntity.getComment())
                .ownFeedback(Objects.equals(feedbackEntity.getCreatedBy(), id))
                .build();
    }
}
