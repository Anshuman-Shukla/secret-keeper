package com.secretkeeper.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CardDetail {

    private Integer firstFourDigit;
    private Integer secondFourDigit;
    private Integer thirdFourDigit;
    private Integer fourthFourDigit;
    private String cvv;
    private String expDate;
    private String pwd;
    private String cardType;

}
