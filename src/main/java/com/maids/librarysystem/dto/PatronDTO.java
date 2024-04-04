package com.maids.librarysystem.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatronDTO {
    private String name;

    private String contactInformation;
}
