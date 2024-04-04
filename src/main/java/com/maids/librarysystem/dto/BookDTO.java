package com.maids.librarysystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    @NotBlank
    private String title;

    @NotBlank
    private String author;

    private int publicationYear;

    @NotBlank
    private String isbn;
}
