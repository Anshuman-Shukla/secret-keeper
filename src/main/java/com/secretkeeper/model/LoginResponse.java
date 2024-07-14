package com.secretkeeper.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String user;
    private Integer userId;
    private Date tokenIssuedTime;
    private Date tokenExpirationTime;
    private String token;
}
