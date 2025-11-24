package de.example.likeherotozero.controller;

import de.example.likeherotozero.model.Co2Record;
import de.example.likeherotozero.repository.Co2RecordRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import de.example.likeherotozero.model.DataStatus;


import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/co2")
public class Co2Controller {

    private final Co2RecordRepository repository;

    public Co2Controller(Co2RecordRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/country")
    public String getLatestByCountry(@RequestParam("country") String country, Model model) {
        // Hole alle Länder für das Dropdown
        List<String> countries = repository.findAllCountries();
        model.addAttribute("countries", countries);
        model.addAttribute("selectedCountry", country);

        // Hole den aktuellsten Datensatz für das ausgewählte Land
        Co2Record latestRecord = repository.findFirstByCountryOrderByYearDesc(country);

        if (latestRecord != null) {
            model.addAttribute("record", latestRecord);
            model.addAttribute("message", "Aktuellster CO2-Datensatz für " + country + ":");
        } else {
            model.addAttribute("message", "Keine Daten für " + country + " gefunden.");
        }

        return "countryData";
    }

    @GetMapping("/result")
    public String showResultPage(Model model) {
        List<String> countries = repository.findAllCountries();
        model.addAttribute("countries", countries);
        return "countryData";
    }
}
