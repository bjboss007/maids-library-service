package com.maids.librarysystem.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book extends BaseEntity {

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    private int publicationYear;

    @NotBlank
    @Column(unique = true)
    private String isbn;

    public Book(Long id, String title, String author, int publicationYear, String isbn) {
        this.setId(id);
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
    }
}
