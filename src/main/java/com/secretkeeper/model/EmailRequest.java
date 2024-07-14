package com.secretkeeper.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmailRequest {
    private String emailId;
    private String pwd;
    private String userName;
}
