package com.Elastickserch_code_crud.elastick.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = Author.Author_INDEX_NAME)

public class Author {
    public static final String Author_INDEX_NAME = "auther";

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

}