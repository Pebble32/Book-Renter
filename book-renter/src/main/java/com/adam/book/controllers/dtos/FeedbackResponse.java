package com.adam.book.controllers.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponse {
    private Double rating;
    private String comment;
    private boolean ownFeedback;
}
