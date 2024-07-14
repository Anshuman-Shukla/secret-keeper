package com.secretkeeper.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
@Entity
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmailDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id")
    private int id;

    private int customerId;
    private String emailId;
    private String userName;
    private String password;
    @JsonIgnore
    @OneToOne(mappedBy = "emailDetails",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @ToString.Exclude
    private EncryptionKey key;

}
