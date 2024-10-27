package com.Elastickserch_code_crud.elastick.repositary;

import com.Elastickserch_code_crud.elastick.entity.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BookRepository extends ElasticsearchRepository<Book, String> {
    // Custom query methods can be defined here if needed
}
