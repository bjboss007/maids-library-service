package com.maids.librarysystem.service;


import com.maids.librarysystem.dto.PatronDTO;
import com.maids.librarysystem.dto.PatronUpdateDTO;
import com.maids.librarysystem.exception.LibraryApplicationException;
import com.maids.librarysystem.model.Patron;
import com.maids.librarysystem.repository.PatronRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatronService {

    private final PatronRepository patronRepository;
    private final ModelMapper modelMapper;

    @CacheEvict(value = "patrons", allEntries = true)
    public Patron addPatron(PatronDTO patronDTO){
        Patron patron = modelMapper.map(patronDTO, Patron.class);
        boolean exists = patronRepository.existsByContactInformation(patronDTO.getContactInformation());
        if (exists) throw new LibraryApplicationException(HttpStatus.CONFLICT, "Patron with the contact info exists");
        return patronRepository.save(patron);
    }

    @Cacheable(value = "patrons", key = "#id")
    public Patron getPatronById(Long id){
        return patronRepository.findById(id)
                .orElseThrow(() -> new LibraryApplicationException(HttpStatus.NOT_FOUND, "Patron not found!"));

    }

    @CacheEvict(value = "patrons", key = "#id")
    public void deletePatron(Long id){
        Patron patron = getPatronById(id);
        patron.deleteEntity();
        patronRepository.save(patron);
    }


    @Cacheable(value = "patrons")
    public List<Patron> getAllPatrons() {
        return patronRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }


    @CacheEvict(value = "patrons")
    public Patron updatePatron(Long id, PatronUpdateDTO patronUpdateDTO){
        Patron patron = getPatronById(id);
        boolean exists = patronRepository.existsByContactInformationAndIdNot(patronUpdateDTO.getContactInformation(), patron.getId());
        if (exists) throw new LibraryApplicationException(HttpStatus.CONFLICT, "Patron with the contact info exists");
        modelMapper.map(patronUpdateDTO, patron);
        return patronRepository.save(patron);
    }
}
