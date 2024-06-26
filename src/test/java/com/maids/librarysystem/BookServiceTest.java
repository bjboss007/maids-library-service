package com.maids.librarysystem;

import com.maids.librarysystem.dto.BookDTO;
import com.maids.librarysystem.dto.BookUpdateDTO;
import com.maids.librarysystem.exception.LibraryApplicationException;
import com.maids.librarysystem.model.Book;
import com.maids.librarysystem.repository.BookRepository;
import com.maids.librarysystem.service.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookService bookService;

    @Spy
    private ModelMapper modelMapper;

    @Test
    public void testGetAllBooks() {
        // Mocking data
        List<Book> books = Arrays.asList(new Book(1L, "Mastery", "Robert Green", 2022, "ISBN1"),
                new Book(2L, "Rich Dad Poor Dad", "Robert T. Kiyosaki", 2023, "ISBN2"));
        Mockito.when(bookRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))).thenReturn(books);

        List<Book> result = bookService.getBooks();

        assertEquals(2, result.size());
    }

    @Test
    public void testGetBookById() {

        Book book = new Book(1L, "Seduction Volume 1", "Robert Green", 2022, "ISBN");
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Book result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals("Seduction Volume 1", result.getTitle());
    }

    @Test
    public void testSaveBook() {
        BookDTO book = new BookDTO("Seduction Volume 1", "Robert Green", 2022, "ISBN");
        Book bookc = new Book(1L, "Seduction Volume 1", "Robert Green", 2022, "ISBN");
        Mockito.when(bookRepository.existsByIsbn("ISBN")).thenReturn(false);
        Mockito.when(bookRepository.save(Mockito.any())).thenReturn(bookc);

        Book result = bookService.addBook(book);
        assertNotNull(result);
        assertEquals("Seduction Volume 1", result.getTitle());
    }

    @Test
    public void testSaveBookExisting() {
        Book existingBook = new Book(1L, "Seduction Volume 1", "Robert Green", 2022, "ISBN");
        Mockito.when(bookRepository.existsByIsbn("ISBN")).thenReturn(true);
        assertThrows(LibraryApplicationException.class, () -> bookService.addBook(new BookDTO("Seduction Volume 1", "Robert Green", 2022, "ISBN")));
    }

    @Test
    public void testUpdateBook() {
        Book existingBook = new Book(1L, "Seduction Volume 1", "Robert Green", 2022, "ISBN");
        BookUpdateDTO updatedBook = new BookUpdateDTO(1L, "Seduction Volume 2", "Robert Green", 2023, "New ISBN");

        Book book = modelMapper.map(updatedBook, Book.class);

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        Mockito.when(bookRepository.save(Mockito.any())).thenReturn(book);

        Book result = bookService.updateBook(1L, updatedBook);

        assertNotNull(result);
        assertEquals("Seduction Volume 2", result.getTitle());
    }

    @Test
    public void testDeleteBook() {
        Book book = new Book(1L, "Seduction Volume 1", "Robert Green", 2022, "ISBN");
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        bookService.deleteBook(1L);

        Mockito.verify(bookRepository).save(Mockito.any());
    }

    @Test
    public void testDeleteBookNonExisting() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(LibraryApplicationException.class, () -> bookService.deleteBook(1L));
        Mockito.verify(bookRepository, Mockito.never()).save(Mockito.any());
    }
}

