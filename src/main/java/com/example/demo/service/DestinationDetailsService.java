package com.example.demo.service;

import com.example.demo.dto.QuestionResponseDto;
import com.example.demo.entity.DestinationDetails;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.DestinationDetailsRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DestinationDetailsService {

    private final DestinationDetailsRepository destinationDetailsRepository;

    public DestinationDetails save(DestinationDetails details) {
        if (details == null || details.getCity() == null || details.getCountry() == null) {
            throw new IllegalArgumentException("City and country must not be null");
        }
        return destinationDetailsRepository.save(details);
    }

    public List<DestinationDetails> getAll() {
        List<DestinationDetails> destinations = destinationDetailsRepository.findAll();
        if (CollectionUtils.isEmpty(destinations)) {
            throw new ResourceNotFoundException("No destinations found");
        }
        return destinations;
    }

    public DestinationDetails getById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid destination ID");
        }
        return destinationDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destination with ID " + id + " not found"));
    }




}
