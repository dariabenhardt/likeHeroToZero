package de.example.likeherotozero.controller;

import de.example.likeherotozero.model.Users;
import de.example.likeherotozero.repository.Co2RecordRepository;
import de.example.likeherotozero.repository.UserRepository;
import de.example.likeherotozero.model.Co2Record;
import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/login")
public class UserController {

    private final UserRepository userRepository;
    private final Co2RecordRepository Co2RecordRepository;

    public UserController(UserRepository userRepository, Co2RecordRepository Co2RecordRepository) {
        this.userRepository = userRepository;
        this.Co2RecordRepository = Co2RecordRepository;
    }


    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
        var userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            Users user = userOpt.get();
            if (user.getPassword().equals(password) && user.getEnabled()) {
                session.setAttribute("user", user);
                return "loggedin";
            }
        }
        model.addAttribute("error", "Ungültiger Benutzername oder Passwort");
        return "index";
    }

    @GetMapping("/loggedin")
    public String loggedInPage(Model model, HttpSession session) {
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/";
        }
        model.addAttribute("username", currentUser.getUsername());
        return "loggedin";
    }



    @PostMapping("/scientist/new")
    public String createNewData(@RequestParam String country, Model model) {
        // Weiterleitung zum Formular für neue Daten mit Vorauswahl Land
        model.addAttribute("country", country);
        return "scientist/newDataForm";
    }

    @GetMapping("/scientist/edit")
    public String editDataForm(@RequestParam String country, Model model) {
        // Lade bestehende Daten zum Land und zeige Formular mit Bearbeitungsdaten
        Co2Record record = Co2RecordRepository.findTopByCountryOrderByYearDesc(country);
        model.addAttribute("co2Record", record);
        return "scientist/editDataForm";
    }
}

