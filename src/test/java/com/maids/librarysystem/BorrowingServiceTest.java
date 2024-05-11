package com.maids.librarysystem;

import com.maids.librarysystem.exception.LibraryApplicationException;
import com.maids.librarysystem.model.Book;
import com.maids.librarysystem.model.BorrowingRecord;
import com.maids.librarysystem.model.Patron;
import com.maids.librarysystem.repository.BorrowingRecordRepository;
import com.maids.librarysystem.service.BookService;
import com.maids.librarysystem.service.BorrowingService;
import com.maids.librarysystem.service.PatronService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class BorrowingServiceTest {
    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private BookService bookService;

    @Mock
    private PatronService patronService;

    @InjectMocks
    private BorrowingService borrowingService;

    @Spy
    private ModelMapper modelMapper;

    @Test
    public void testBorrowBook() {
        Book book = new Book("Art of War", "Robert Green", 2022, "ISBN1");
        book.setId(1L);
        Patron patron = new Patron("John Wich", "johnwich@gmail.com");
        patron.setId(1L);
        BorrowingRecord borrowingRecord = new BorrowingRecord(book, patron, LocalDate.now(), null);

        Mockito.when(bookService.getBookById(1L)).thenReturn(book);
        Mockito.when(patronService.getPatronById(1L)).thenReturn(patron);
        Mockito.when(borrowingRecordRepository.save(Mockito.any())).thenReturn(borrowingRecord);

        BorrowingRecord result = borrowingService.borrowBook(1L, 1L);

        assertNotNull(result);
        assertEquals(book, result.getBook());
        assertEquals(patron, result.getPatron());
        assertNotNull(result.getBorrowingDate());
        assertNull(result.getReturnDate());
    }

    @Test
    public void testReturnBook() {

        Book book = new Book("Art of War", "Robert Green", 2022, "ISBN1");
        book.setId(1L);
        Patron patron = new Patron("John Wich", "johnwich@gmail.com");
        patron.setId(1L);
        BorrowingRecord borrowingRecord = new BorrowingRecord(book, patron, LocalDate.now(), null);

        Mockito.when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(1L, 1L)).thenReturn(Collections.singletonList(borrowingRecord));
        borrowingRecord.setReturnDate(LocalDate.now());
        Mockito.when(borrowingRecordRepository.save(Mockito.any())).thenReturn(borrowingRecord);

        BorrowingRecord result = borrowingService.returnBook(1L, 1L);

        assertNotNull(result);
        assertNotNull(result.getReturnDate());
    }

    @Test
    public void testBorrowBookBookAlreadyBorrowed() {
        Book book = new Book(1L, "Increase Your Financial IQ", "Robert T. Kiyosaki", 2022, "ISBN");
        Patron patron = new Patron(1L, "Patron", "Contact");
        Mockito.when(bookService.getBookById(1L)).thenReturn(book);
        Mockito.when(patronService.getPatronById(1L)).thenReturn(patron);
        Mockito.when(borrowingRecordRepository.existsByBookIdAndReturnDateIsNull(1L)).thenReturn(true);

        LibraryApplicationException exception = assertThrows(LibraryApplicationException.class, () -> {
            borrowingService.borrowBook(1L, 1L);
        });

        assertEquals(exception.getHttpStatus(), HttpStatus.BAD_REQUEST);
    }
}
