package de.example.likeherotozero.controller;

import de.example.likeherotozero.model.Co2Record;
import de.example.likeherotozero.repository.Co2RecordRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/")
public class PublicController {

    private final Co2RecordRepository repository;

    public PublicController(Co2RecordRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String showHomepage(Model model) {
        List<String> countries = repository.findAllCountries();
        model.addAttribute("countries", countries);
        model.addAttribute("co2Record", new Co2Record());
        return "index";
    }

}
