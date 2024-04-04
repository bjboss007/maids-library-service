package com.maids.librarysystem.model;


import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;



@Entity
@Getter
@Setter
public class AppUser extends BaseEntity{
    private String username;
    private String password;
}
