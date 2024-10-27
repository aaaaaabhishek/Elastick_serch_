package com.Elastickserch_code_crud.elastick.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.lang.annotation.Documented;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = Book.BOOK_INDEX_NAME)
public class Book {
    public static final String BOOK_INDEX_NAME = "books";

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("authorId")
    private String authorId;
}
