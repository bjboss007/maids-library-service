package com.maids.librarysystem.repository;

import com.maids.librarysystem.model.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    Boolean existsByBookIdAndReturnDateIsNull(Long bookId);

    List<BorrowingRecord> findByBookIdAndPatronIdAndReturnDateIsNull(Long bookId, Long patronId);
}
