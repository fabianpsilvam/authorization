package com.zebrands.authorization.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name = "profile")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private String userId;

    @Column
    private String type;

    @Column
    private String email;

}
