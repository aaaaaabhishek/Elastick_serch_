package com.Elastickserch_code_crud.elastick.controller;

import com.Elastickserch_code_crud.elastick.Service.BookService;
import com.Elastickserch_code_crud.elastick.entity.Author;
import com.Elastickserch_code_crud.elastick.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<String> addBook(@RequestBody Book book) throws IOException {
        bookService.addBook(book);
        return ResponseEntity.ok("Book added with ID: " + book.getId());
    }
    @GetMapping("/search") // Fixed the endpoint to "/search"
    public List<Book> searchBooks(@RequestParam String title) { // Use @RequestParam to extract the query parameter
        try {
            return bookService.fuzzySearchByTitle(title);
        } catch (IOException e) {
            // Log the exception (consider using a logger instead of e.printStackTrace())
            e.printStackTrace();
            return Collections.emptyList(); // Return an empty list if an error occurs
        }
    }

    /**
     * When your application starts or when you need a specific index, you should call the API endpoint that triggers the createIndex method.
     * This is important to ensure that the index exists before attempting any document operations
     * @param indexName
     * @param shards
     * @param replicas
     * @return
     */
    @PostMapping("/{indexName}")
    public ResponseEntity<String> createIndex(@PathVariable String indexName,
                                              @RequestParam(defaultValue = "3") int shards,
                                              @RequestParam(defaultValue = "2") int replicas) {
        // Validate input parameters (optional)
        if (shards < 1 || replicas < 0) {
            return ResponseEntity.badRequest().body("Invalid parameters: shards must be >= 1 and replicas must be >= 0.");
        }

        try {
            boolean indexCreated = bookService.createIndex(indexName, shards, replicas);
            if (indexCreated) {
                return ResponseEntity.ok("Index created: " + indexName);
            } else {
                return ResponseEntity.status(400).body("Index already exists: " + indexName);
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error creating index: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable String id) {
        return bookService.getBook(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable String id) throws IOException {
        bookService.deleteBook(id);
        return ResponseEntity.ok("Book deleted with ID: " + id);
    }
    @PostMapping
    public ResponseEntity<String> addAuthor(@RequestBody Author author) throws IOException {
        bookService.addAuthor(author);
        return ResponseEntity.ok("Author added with ID: " + author.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthor(@PathVariable String id) {
        return bookService.getAuthor(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteAuthor(@PathVariable String id) throws IOException {
//        bookService.deleteAuthor(id);
//        return ResponseEntity.ok("Author deleted with ID: " + id);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAuthor(@PathVariable String id, @RequestBody Author updatedAuthor) throws IOException {
        // Check if the author exists
        if (bookService.getAuthor(id).isPresent()) {
            updatedAuthor.setId(id); // Ensure the ID remains the same
            bookService.addAuthor(updatedAuthor); // Re-indexing the author
            return ResponseEntity.ok("Author updated with ID: " + id);
        } else {
            return ResponseEntity.notFound().build(); // Author not found
        }
    }
}
