package com.maids.librarysystem.service;


import com.maids.librarysystem.dto.BookDTO;
import com.maids.librarysystem.dto.BookUpdateDTO;
import com.maids.librarysystem.exception.LibraryApplicationException;
import com.maids.librarysystem.model.Book;
import com.maids.librarysystem.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    @Transactional
    @CacheEvict(value = "books", allEntries = true)
    public Book addBook(BookDTO bookDTO){
        if (bookRepository.existsByIsbn(bookDTO.getIsbn())) {
            throw new LibraryApplicationException(HttpStatus.CONFLICT, "Book already exists with the isbn!");
        }
        Book book = modelMapper.map(bookDTO, Book.class);
        return bookRepository.save(book);
    }

    @Cacheable(value = "books", key = "#id")
    public Book getBookById(Long id){
       return bookRepository.findById(id)
                .orElseThrow(() -> new LibraryApplicationException(HttpStatus.NOT_FOUND, "Book with id "+ id + " not found!"));
    }


    @CacheEvict(key = "#id", value = "books")
    public void deleteBook(Long id){
        Book book = getBookById(id);
        book.deleteEntity();
        bookRepository.save(book);
    }

    @Cacheable("books")
    public List<Book> getBooks() {
        return bookRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @CacheEvict(value = "books")
    public Book updateBook(Long id, BookUpdateDTO bookUpdateDTO){
        Book book = getBookById(id);
        modelMapper.map(bookUpdateDTO, book);
        return bookRepository.save(book);
    }
}
