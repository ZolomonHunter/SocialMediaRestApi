package com.example.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {
    @Id
    @GeneratedValue
    private Integer id;
    @NotEmpty
    private String header;
    private String description;
    @ManyToOne
    private User owner;
    private String imageUrl;
    @CreationTimestamp(source = SourceType.DB)
    private Instant createdOn;
}
