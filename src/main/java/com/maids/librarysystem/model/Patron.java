package com.maids.librarysystem.model;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLRestriction;

import javax.validation.constraints.NotBlank;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLRestriction("deleted <> 'Y'")
@ToString
public class Patron extends BaseEntity {
    @NotBlank
    private String name;

    private String contactInformation;

    public Patron(Long id, String name, String contactInformation) {
        this.setId(id);
        this.name = name;
        this.contactInformation = contactInformation;
    }
}
