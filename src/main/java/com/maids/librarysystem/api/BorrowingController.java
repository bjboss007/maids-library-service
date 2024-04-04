package com.maids.librarysystem.api;


import com.maids.librarysystem.config.AppResponse;
import com.maids.librarysystem.model.BorrowingRecord;
import com.maids.librarysystem.service.BorrowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/borrow")
@RequiredArgsConstructor
public class BorrowingController {
    private final BorrowingService borrowingService;

    @PostMapping("/{bookId}/patron/{patronId}")
    public ResponseEntity<AppResponse<BorrowingRecord>> borrowBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        BorrowingRecord borrowingRecord = borrowingService.borrowBook(bookId, patronId);
        return AppResponse.build(HttpStatus.CREATED, borrowingRecord);
    }

    @PutMapping("/{bookId}/patron/{patronId}")
    public ResponseEntity<AppResponse<BorrowingRecord>> returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        BorrowingRecord borrowingRecord = borrowingService.returnBook(bookId, patronId);
        return AppResponse.build(borrowingRecord);
    }
}
