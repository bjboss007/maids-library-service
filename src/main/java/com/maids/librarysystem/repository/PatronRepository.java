package com.maids.librarysystem.repository;

import com.maids.librarysystem.model.Patron;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatronRepository extends JpaRepository<Patron, Long> {
    boolean existsByContactInformation(String name);
    boolean existsByContactInformationAndIdNot(String name, Long id);
}
