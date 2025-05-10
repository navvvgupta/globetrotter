package com.example.demo.repository;

import com.example.demo.entity.DestinationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DestinationDetailsRepository extends JpaRepository<DestinationDetails, Long> {
    @Query("SELECT MAX(d.id) FROM DestinationDetails d")
    Long getMaxId();

    List<DestinationDetails> findAll();

}

