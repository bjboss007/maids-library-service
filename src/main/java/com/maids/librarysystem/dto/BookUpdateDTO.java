package com.maids.librarysystem.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BookUpdateDTO extends BookDTO {
    @NotNull
    private Long id;

    public BookUpdateDTO(Long id, @NotBlank String title, @NotBlank String author, int publicationYear, @NotBlank String isbn) {
        super(title, author, publicationYear, isbn);
        this.id = id;
    }
}
