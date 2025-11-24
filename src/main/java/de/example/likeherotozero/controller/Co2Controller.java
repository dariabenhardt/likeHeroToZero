package de.example.likeherotozero.controller;

import de.example.likeherotozero.model.Co2Record;
import de.example.likeherotozero.repository.Co2RecordRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;
@Controller
@RequestMapping("/co2")
public class Co2Controller {

    private final Co2RecordRepository repository;

    @Autowired
    public Co2Controller(Co2RecordRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/country")
    public String showCountryForm(Model model) {
        List<String> countries = repository.findAllCountries(); // Alle Länder aus der DB ermitteln
        model.addAttribute("countries", countries);
        model.addAttribute("co2Record", new Co2Record()); // Leeres Objekt für Form Binding
        return "countryData";
    }

    @PostMapping("/country")
    public String showCountryData(@ModelAttribute Co2Record co2Record, Model model) {
        List<Co2Record> co2 = repository.findAllByCountry(co2Record.getCountry());
        model.addAttribute("co2", co2);
        model.addAttribute("countries", repository.findAllCountries());
        model.addAttribute("co2Record", co2Record);
        return "countryData";
    }
    }
