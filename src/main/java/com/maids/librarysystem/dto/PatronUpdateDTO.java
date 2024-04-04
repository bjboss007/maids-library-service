package com.maids.librarysystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatronUpdateDTO extends PatronDTO {
    @NotNull
    private Long id;

    public PatronUpdateDTO(Long id, String name, String contactInformation) {
        super(name, contactInformation);
        this.id = id;
    }
}
