package com.secretkeeper.controller;

import com.secretkeeper.model.EmailRequest;
import com.secretkeeper.service.EmailDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/email")
public class EmailController {
    @Autowired
    private EmailDetailService service;
    @GetMapping("/{userId}")
    public ResponseEntity<List<EmailRequest>> getAllEmailDetail(@PathVariable("userId") Integer userId) {

        return ResponseEntity.status(HttpStatus.OK).body(service.getAllEmailDetail(userId));
    }

    @GetMapping("/{userId}/{emailId}")
    public String getUniqueEmailDetail(@PathVariable("userId") Integer userId, @PathVariable("emailId") Integer emailId) {
        return "heheh";
    }

    @PostMapping("/{customerId}")
    public ResponseEntity<String> saveEmailDetail(@RequestBody EmailRequest request, @PathVariable("customerId") Integer id) {
        service.saveEmailDetail(request, id)
        return ResponseEntity.status(HttpStatus.CREATED).body("You Detail is saved Successfully!! , Thanks for you Trust.");
    }

    @PutMapping
    public String updateEmailDetail() {
        return "update";
    }

    @DeleteMapping("/{emailId}")
    public ResponseEntity<String> deleteEmailDetail(@PathVariable("emailId") Integer id) {
        String response = service.deleteEmailData(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

    }


}
