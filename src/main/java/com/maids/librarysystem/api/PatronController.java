package com.maids.librarysystem.api;

import com.maids.librarysystem.config.AppResponse;
import com.maids.librarysystem.dto.PatronDTO;
import com.maids.librarysystem.dto.PatronUpdateDTO;
import com.maids.librarysystem.model.Patron;
import com.maids.librarysystem.service.PatronService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/patrons")
@RequiredArgsConstructor
public class PatronController {

    private final PatronService patronService;

    @GetMapping
    public ResponseEntity<List<Patron>> getAllPatrons() {
        List<Patron> patrons = patronService.getAllPatrons();
        return ResponseEntity.ok(patrons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppResponse<Patron>> getPatronById(@PathVariable Long id) {
        Patron patron = patronService.getPatronById(id);
        return AppResponse.build(patron);
    }

    @PostMapping
    public ResponseEntity<AppResponse<Patron>> addPatron(@RequestBody @Valid PatronDTO patron) {
        Patron savedPatron = patronService.addPatron(patron);
        return AppResponse.build(HttpStatus.CREATED, savedPatron);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppResponse<Patron>> updatePatron(@PathVariable Long id, @RequestBody @Valid PatronUpdateDTO patronDetails) {
        Patron updatedPatron = patronService.updatePatron(id, patronDetails);
        return AppResponse.build(updatedPatron);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppResponse<String>> deletePatron(@PathVariable Long id) {
        patronService.deletePatron(id);
        return AppResponse.build("Patron deleted successfully");
    }
}
