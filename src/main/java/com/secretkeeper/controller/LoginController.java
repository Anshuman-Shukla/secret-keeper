package com.secretkeeper.controller;

import com.secretkeeper.entity.CustomerDetail;
import com.secretkeeper.model.LoginDetail;
import com.secretkeeper.model.LoginResponse;
import com.secretkeeper.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class LoginController {
    @Autowired
    private LoginService service;

    @PostMapping("register")
    public CustomerDetail registerUser(@RequestBody CustomerDetail detail) {
        return service.saveCustomerDetail(detail);
    }

    @GetMapping("login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDetail detail) {
        LoginResponse token = service.getToken(detail);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(token);
    }
}
