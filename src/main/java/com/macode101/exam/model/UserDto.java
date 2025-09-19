package com.macode101.exam.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    
    @NotBlank(message = "Name is required and cannot be blank")
    private String name;
    
    @NotBlank(message = "Username is required and cannot be blank")
    private String username;
    
    @NotBlank(message = "Email is required and cannot be blank")
    private String email;
    
    private String phone;
    
    private String website;
}
