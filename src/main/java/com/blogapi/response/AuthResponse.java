package com.blogapi.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

//    UserDto user;
    private String accessToken;
    private String refreshToken;
    private int id;
    private String email;
    private String name;
    private int age;
    private String gender;
}
