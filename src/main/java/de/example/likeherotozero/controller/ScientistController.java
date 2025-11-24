package de.example.likeherotozero.controller;

import de.example.likeherotozero.model.Users;
import de.example.likeherotozero.model.Co2Record;
import de.example.likeherotozero.model.PendingChange;
import de.example.likeherotozero.model.DataStatus;
import de.example.likeherotozero.repository.Co2RecordRepository;
import de.example.likeherotozero.service.PendingChangeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/scientist")
public class ScientistController {

    private final Co2RecordRepository co2RecordRepository;
    private final PendingChangeService pendingChangeService;

    public ScientistController(Co2RecordRepository co2RecordRepository,
                               PendingChangeService pendingChangeService) {
        this.co2RecordRepository = co2RecordRepository;
        this.pendingChangeService = pendingChangeService;
    }

    /**
     * Dashboard für Wissenschaftler
     */
    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null || !currentUser.isScientist()) {
            return "redirect:/";
        }

        // Alle Länder für Dropdown
        List<String> countries = co2RecordRepository.findAllCountries();
        model.addAttribute("countries", countries);

        // Eigene eingereichte Änderungen
        List<PendingChange> myChanges = pendingChangeService.getUserChanges(currentUser.getUsername());
        model.addAttribute("myChanges", myChanges);

        model.addAttribute("username", currentUser.getUsername());
        return "scientist/dashboard";
    }

    /**
     * Formular: Neuen Datensatz vorschlagen
     */
    @GetMapping("/new")
    public String showNewRecordForm(Model model, HttpSession session) {
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null || !currentUser.isScientist()) {
            return "redirect:/";
        }

        List<String> countries = co2RecordRepository.findAllCountries();
        model.addAttribute("countries", countries);
        model.addAttribute("dataStatuses", DataStatus.values());

        return "scientist/new-record-form";
    }

    /**
     * Neuen Datensatz vorschlagen (Submit)
     */
    @PostMapping("/new")
    public String submitNewRecord(@RequestParam String country,
                                  @RequestParam int year,
                                  @RequestParam double co2,
                                  @RequestParam DataStatus status,
                                  @RequestParam String reason,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null || !currentUser.isScientist()) {
            return "redirect:/";
        }

        try {
            pendingChangeService.proposeNewRecord(
                    country, year, co2, status,
                    currentUser.getUsername(), reason
            );

            redirectAttributes.addFlashAttribute("successMessage",
                    "Ihr Vorschlag wurde erfolgreich eingereicht und wartet auf Genehmigung.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Fehler beim Einreichen: " + e.getMessage());
        }

        return "redirect:/scientist/dashboard";
    }

    /**
     * Formular: Bestehenden Datensatz bearbeiten (Schritt 1: Land auswählen)
     */
    @GetMapping("/edit")
    public String showEditSelectCountry(Model model, HttpSession session) {
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null || !currentUser.isScientist()) {
            return "redirect:/";
        }

        List<String> countries = co2RecordRepository.findAllCountries();
        model.addAttribute("countries", countries);

        return "scientist/edit-select-country";
    }

    /**
     * Datensatz zum Bearbeiten anzeigen (Schritt 2: Aktuellen Wert laden)
     */
    @PostMapping("/edit/load")
    public String loadRecordForEdit(@RequestParam String country,
                                    Model model,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null || !currentUser.isScientist()) {
            return "redirect:/";
        }

        Co2Record record = co2RecordRepository.findFirstByCountryOrderByYearDesc(country);

        if (record == null) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Kein Datensatz für " + country + " gefunden.");
            return "redirect:/scientist/edit";
        }

        model.addAttribute("record", record);
        model.addAttribute("dataStatuses", DataStatus.values());

        return "scientist/edit-record-form";
    }

    /**
     * Änderung eines bestehenden Datensatzes vorschlagen (Submit)
     */
    @PostMapping("/edit/submit")
    public String submitEditRecord(@RequestParam Long recordId,
                                   @RequestParam String country,
                                   @RequestParam int year,
                                   @RequestParam double co2,
                                   @RequestParam DataStatus status,
                                   @RequestParam String reason,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null || !currentUser.isScientist()) {
            return "redirect:/";
        }

        try {
            pendingChangeService.proposeUpdate(
                    recordId, country, year, co2, status,
                    currentUser.getUsername(), reason
            );

            redirectAttributes.addFlashAttribute("successMessage",
                    "Ihre Änderung wurde erfolgreich eingereicht und wartet auf Genehmigung.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Fehler beim Einreichen: " + e.getMessage());
        }

        return "redirect:/scientist/dashboard";
    }
}