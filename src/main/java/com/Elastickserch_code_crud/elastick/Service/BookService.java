package com.Elastickserch_code_crud.elastick.Service;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.FuzzyQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import com.Elastickserch_code_crud.elastick.entity.Book;
import com.Elastickserch_code_crud.elastick.entity.Author;
import com.Elastickserch_code_crud.elastick.repositary.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
private static final Logger logger= LoggerFactory.getLogger(BookService.class);
    private final ElasticsearchClient elasticsearchClient;
    private final String bookIndexName = "books";
    private final String authorIndexName = "authors";
private final BookRepository bookRepository;
    @Autowired
    public BookService(ElasticsearchClient elasticsearchClient, BookRepository bookRepository) {
        this.elasticsearchClient = elasticsearchClient;
        this.bookRepository = bookRepository;
    }
    // crete shareding
    public boolean createIndex(String indexName, int shards, int replicas) throws IOException {
        // Check if the index already exists
        boolean indexExists = elasticsearchClient.indices().exists(e -> e.index(indexName)).value();

        if (indexExists) {
            return false; // Index already exists
        }

        // Create the index with specified settings
        CreateIndexRequest createIndexRequest = CreateIndexRequest.of(i -> i
                .index(indexName)
                .settings(s -> s
                        .numberOfShards(String.valueOf(shards))
                        .numberOfReplicas(String.valueOf(replicas))
                )
        );

        // Create the index
        elasticsearchClient.indices().create(createIndexRequest);
        return true; // Index created successfully
    }
    public Book addBook(Book book) throws IOException {
      return bookRepository.save(book);
    }

    // Book methods
//    public IndexResponse addBook(Book book) throws IOException {
//        IndexRequest<Book> request = IndexRequest.of(i -> i
//                .index(Book.BOOK_INDEX_NAME)
//                .id(book.getId())
//                .document(book)
//        );
//        return elasticsearchClient.index(request);
//    }

    public Optional<Book> getBook(String id) {
        try {
            GetResponse<Book> response = elasticsearchClient.get(g -> g.index(Book.BOOK_INDEX_NAME).id(id), Book.class);
            return Optional.ofNullable(response.source());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public DeleteResponse deleteBook(String id) throws IOException {
        return elasticsearchClient.delete(d -> d.index(Book.BOOK_INDEX_NAME).id(id));
    }

    // Author methods
    public IndexResponse addAuthor(Author author) throws IOException {
        IndexRequest<Author> request = IndexRequest.of(i -> i
                .index(authorIndexName)
                .id(author.getId())
                .document(author)
        );
        return elasticsearchClient.index(request);
    }

    public Optional<Author> getAuthor(String id) {
        try {
            GetResponse<Author> response = elasticsearchClient.get(g -> g.index(authorIndexName).id(id), Author.class);
            return Optional.ofNullable(response.source());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    // Fuzzy search for books by title
    public List<Book> fuzzySearchByTitle(String title) throws IOException {
        try {

            // Create a fuzzy query
            /**
             * FuzzyQuery: This is used to create a query that allows for approximate matching of the title field in the index.
             * field("title"): Specifies the field to search.
             * value(FieldValue.of(title)): Sets the value to search, which is the title provided by the user.
             * fuzziness("AUTO"): Enables Elasticsearch to automatically determine how "fuzzy" the search should be based on the length of the input.
             */
            FuzzyQuery fuzzyQuery = FuzzyQuery.of(f -> f
                    .field("title") // Field to search
                    .value(FieldValue.of(title))   // Value to search
                    .fuzziness("AUTO") // Automatically determine fuzziness level
            );

            // Create search request
            /**
             * SearchRequest: This object represents the entire search request sent to Elasticsearch.
             * index(bookIndexName): Specifies the index (in this case, "books") where the search will be performed.
             * query(Query.of(q -> q.fuzzy(fuzzyQuery))): This attaches the fuzzy query to the search request.
             */
            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index(bookIndexName) // Use the books index
                    .query(Query.of(q -> q
                            .fuzzy(fuzzyQuery) // Set fuzzy query
                    )) // Fix placement of the fuzzy query
            );

            // Execute search and return results
            /**
             * elasticsearchClient.search(...): Sends the search request to Elasticsearch and receives the response.
             */
            SearchResponse<Book> response = elasticsearchClient.search(searchRequest, Book.class);
            /**
             * response.hits().hits(): Retrieves the list of hits (matching documents) from the search response.
             * stream().map(hit -> hit.source()): Converts each hit to its corresponding Book object.
             * collect(Collectors.toList()): Collects the Book objects into a list and returns it.
             */
            return response.hits().hits().stream()
                    .map(hit -> hit.source())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("Error during fuzzy search by title '{}': {}", title, e.getMessage());
            throw e; // Rethrow the exception after logging
        }


    }}