package com.blogapi.dto;


import com.blogapi.entities.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private int id;

    @NotEmpty(message = "Name must not be empty")
    @Size(min = 3, message = "Name must be at least 3 characters long")
    private String name;

    @Email
    private String email;

    @NotEmpty
    private String password;

    @NotNull(message = "Age must not be empty")
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 100, message = "Age must not be greater than 100")
    private Integer age;

    @NotEmpty
    private String gender;


}
