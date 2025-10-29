package de.example.likeherotozero.controller;

import de.example.likeherotozero.model.Co2Record;
import de.example.likeherotozero.repository.Co2RecordRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/co2")
public class Co2Controller {


    private final Co2RecordRepository repository;

    public Co2Controller(Co2RecordRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Co2Record> getAll () {
        return repository.findAll ();
    }

    @GetMapping("/{country}")
    public Optional<Co2Record> getLatest(@PathVariable String country) {
        return repository.findTopByCountryOrderByYearDesc(country);
    }
}
