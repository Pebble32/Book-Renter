package com.adam.book.repositories.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class FeedbackEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Double feedbackRating;

    private String comment;


    /*
    Information About Entity changes and creation
     */
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime creationDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime modificationDate;
    @CreatedBy
    @Column(updatable = false, nullable = false)
    private Long createdBy;
    @LastModifiedBy
    @Column(insertable = false)
    private Long modifiedBy;
}
