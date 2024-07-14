package com.secretkeeper.entity;

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
public class EncryptionKey {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id")
    private int id;
    private String encryptionKey;
    @OneToOne
    @JoinColumn(name = "email_fk",referencedColumnName = "id")
    @ToString.Exclude
    private EmailDetails emailDetails;

}
