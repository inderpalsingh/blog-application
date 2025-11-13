package com.blogapi.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

//    UserDto user;
//    private String name;
    private String accessToken;
    private String refreshToken;

    private String email;
}
