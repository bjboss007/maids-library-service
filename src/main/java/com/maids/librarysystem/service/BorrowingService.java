package com.maids.librarysystem.service;

import com.maids.librarysystem.exception.LibraryApplicationException;
import com.maids.librarysystem.model.Book;
import com.maids.librarysystem.model.BorrowingRecord;
import com.maids.librarysystem.model.Patron;
import com.maids.librarysystem.repository.BorrowingRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowingService {
    
    private final BookService bookService;
    private final PatronService patronService;
    private final BorrowingRecordRepository borrowingRecordRepository;

    public BorrowingRecord borrowBook(Long bookId, Long patronId) {
        Book book = bookService.getBookById(bookId);
        Patron patron = patronService.getPatronById(patronId);

        if (isBookAvailableForBorrowing(bookId)) {
            throw new LibraryApplicationException("Book with id: " + bookId + " is not available for borrowing");
        }

        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowingDate(LocalDate.now());
        return borrowingRecordRepository.save(borrowingRecord);
    }

    private boolean isBookAvailableForBorrowing(Long bookId) {
        return  borrowingRecordRepository.existsByBookIdAndReturnDateIsNull(bookId);
    }

    public BorrowingRecord returnBook(Long bookId, Long patronId) {
        List<BorrowingRecord> borrowingRecords = borrowingRecordRepository
                .findByBookIdAndPatronIdAndReturnDateIsNull(bookId, patronId);
        if (borrowingRecords.isEmpty()) {
            throw new LibraryApplicationException(HttpStatus.NOT_FOUND, "No borrowing record found for book id: " + bookId + " and patron id: " + patronId);
        }
        BorrowingRecord borrowingRecord = borrowingRecords.get(0);
        borrowingRecord.setReturnDate(LocalDate.now());
        return borrowingRecordRepository.save(borrowingRecord);
    }
}
