package com.example.demo.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.util.List;

@Entity
@Table(name = "destination_details")
@Data
public class DestinationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;

    private String country;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private List<String> clues;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private List<String> funFacts;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private List<String> trivia;
}
