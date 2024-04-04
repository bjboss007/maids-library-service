package com.maids.librarysystem;

import com.maids.librarysystem.dto.PatronDTO;
import com.maids.librarysystem.dto.PatronUpdateDTO;
import com.maids.librarysystem.exception.LibraryApplicationException;
import com.maids.librarysystem.model.Patron;
import com.maids.librarysystem.repository.PatronRepository;
import com.maids.librarysystem.service.PatronService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PatronServiceTest {

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private PatronService patronService;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @Test
    public void testGetAllPatrons() {
        List<Patron> patrons = Arrays.asList(new Patron(1L, "Patron 1", "Contact 1"),
                new Patron(2L, "Patron 2", "Contact 2"));
        Mockito.when(patronRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))).thenReturn(patrons);

        List<Patron> result = patronService.getAllPatrons();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetPatronById() {
        Patron patron = new Patron(1L, "Patron", "Contact");
        Mockito.when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));

        Patron result = patronService.getPatronById(1L);

        assertNotNull(result);
        assertEquals("Patron", result.getName());
    }

    @Test
    public void testSavePatron() {

        PatronDTO patron = new PatronDTO("Patron", "Contact");
        Patron patron1 = new Patron(1L, "Patron", "Contact");
        Mockito.when(patronRepository.save(Mockito.any())).thenReturn(patron1);

        Patron result = patronService.addPatron(patron);

        assertNotNull(result);
        assertEquals("Patron", result.getName());
    }

    @Test
    public void testUpdatePatron() {

        Patron existingPatron = new Patron(1L, "Patron", "Contact");
        PatronUpdateDTO updatedPatron = new PatronUpdateDTO(1L, "New Patron", "New Contact");
        Patron patron = modelMapper.map(updatedPatron, Patron.class);
        Mockito.when(patronRepository.findById(1L)).thenReturn(Optional.of(existingPatron));
        Mockito.when(patronRepository.save(existingPatron)).thenReturn(patron);

        Patron result = patronService.updatePatron(1L, updatedPatron);

        assertNotNull(result);
        assertEquals("New Patron", result.getName());
    }

    @Test
    public void testDeletePatron() {

        Patron existingPatron = new Patron(1L, "Patron", "Contact");
        Mockito.when(patronRepository.findById(1L)).thenReturn(Optional.of(existingPatron));
        patronService.deletePatron(1L);
        verify(patronRepository, times(1)).save(Mockito.any());
    }

    @Test()
    public void testGetPatronByIdNotFound() {
        Mockito.when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        LibraryApplicationException exception = assertThrows(LibraryApplicationException.class, () -> {
            patronService.getPatronById(1L);
        });

        assertEquals(exception.getHttpStatus(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testUpdatePatronNotFound() {

        PatronUpdateDTO patronDTO = new PatronUpdateDTO(111L, "Patron", "Contact");
        LibraryApplicationException exception = assertThrows(LibraryApplicationException.class, () -> {
            patronService.updatePatron(111L, patronDTO);
        });

        assertEquals(exception.getHttpStatus(), HttpStatus.NOT_FOUND);
    }



}
