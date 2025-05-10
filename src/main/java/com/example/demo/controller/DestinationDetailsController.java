package com.example.demo.controller;

import com.example.demo.entity.DestinationDetails;
import com.example.demo.service.DestinationDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/destinations")
public class DestinationDetailsController {

    @Autowired
    private DestinationDetailsService service;

    @PostMapping
    public DestinationDetails addDestination(@RequestBody DestinationDetails details) {
        return service.save(details);
    }

    @GetMapping
    public List<DestinationDetails> getAllDestinations() {
        System.out.println("HOooo");
        return service.getAll();
    }

    @GetMapping("/{id}")
    public DestinationDetails getDestinationById(@PathVariable Long id) {
        return service.getById(id);
    }
}
