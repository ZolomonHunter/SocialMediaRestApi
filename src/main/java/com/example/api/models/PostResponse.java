package com.example.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostResponse {
    private Integer id;
    private String header;
    private String description;
    private String imageUrl;
}
