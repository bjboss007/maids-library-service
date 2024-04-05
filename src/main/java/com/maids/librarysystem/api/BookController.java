package com.maids.librarysystem.api;

import com.maids.librarysystem.config.AppResponse;
import com.maids.librarysystem.dto.BookDTO;
import com.maids.librarysystem.dto.BookUpdateDTO;
import com.maids.librarysystem.model.Book;
import com.maids.librarysystem.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<AppResponse<List<Book>>> getAllBooks() {
        List<Book> books = bookService.getBooks();
        return AppResponse.build(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppResponse<Book>> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return AppResponse.build(book);
    }

    @PostMapping
    public ResponseEntity<AppResponse<Book>> addBook(@RequestBody @Valid BookDTO book) {
        Book savedBook = bookService.addBook(book);
        return AppResponse.build(HttpStatus.CREATED, savedBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppResponse<Book>> updateBook(@PathVariable Long id, @RequestBody @Valid BookUpdateDTO bookDetails) {
        Book updatedBook = bookService.updateBook(id, bookDetails);
        return AppResponse.build(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppResponse<String>> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return AppResponse.build("Book deleted successfully");
    }
}
