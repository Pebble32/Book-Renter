package com.adam.book.repositories.entities;

import com.adam.book.repositories.entities.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackEntity extends BaseEntity {

    private Double feedbackRating;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookEntity book;
}
