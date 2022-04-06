package com.example.demo.alumni;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="alumni")
public class Alumni {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    String name;

    String addressMetadata;

    String educationMetadata;

}
