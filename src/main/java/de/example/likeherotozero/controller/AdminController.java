package de.example.likeherotozero.controller;

import de.example.likeherotozero.model.*;
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
@RequestMapping("/admin")
public class AdminController {

    private final PendingChangeService pendingChangeService;
    private final Co2RecordRepository co2RecordRepository;

    public AdminController(PendingChangeService pendingChangeService,
                           Co2RecordRepository co2RecordRepository) {
        this.pendingChangeService = pendingChangeService;
        this.co2RecordRepository = co2RecordRepository;
    }

    /**
     * Admin Dashboard - Zeigt alle ausstehenden Änderungen
     */
    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null || !currentUser.isAdmin()) {
            return "redirect:/";
        }

        // Alle ausstehenden Änderungen
        List<PendingChange> pendingChanges = pendingChangeService.getPendingChanges();
        model.addAttribute("pendingChanges", pendingChanges);
        model.addAttribute("username", currentUser.getUsername());

        return "admin/dashboard";
    }

    /**
     * Details einer Änderung anzeigen
     */
    @GetMapping("/review/{id}")
    public String showReviewPage(@PathVariable Long id,
                                 Model model,
                                 HttpSession session) {
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null || !currentUser.isAdmin()) {
            return "redirect:/";
        }

        Optional<PendingChange> changeOpt = pendingChangeService.getChangeById(id);

        if (changeOpt.isEmpty()) {
            return "redirect:/admin/dashboard?error=notfound";
        }

        PendingChange change = changeOpt.get();
        model.addAttribute("change", change);

        // Bei UPDATE: Original-Datensatz laden
        if (change.getChangeType() == ChangeType.UPDATE && change.getOriginalRecordId() != null) {
            Optional<Co2Record> originalOpt = co2RecordRepository.findById(change.getOriginalRecordId());
            originalOpt.ifPresent(original -> model.addAttribute("original", original));
        }

        return "admin/review-change";
    }

    /**
     * Änderung genehmigen
     */
    @PostMapping("/approve/{id}")
    public String approveChange(@PathVariable Long id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null || !currentUser.isAdmin()) {
            return "redirect:/";
        }

        try {
            pendingChangeService.approveChange(id, currentUser.getUsername());
            redirectAttributes.addFlashAttribute("successMessage",
                    "Änderung wurde erfolgreich genehmigt und übernommen.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Fehler beim Genehmigen: " + e.getMessage());
        }

        return "redirect:/admin/dashboard";
    }

    /**
     * Änderung ablehnen
     */
    @PostMapping("/reject/{id}")
    public String rejectChange(@PathVariable Long id,
                               @RequestParam String reason,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null || !currentUser.isAdmin()) {
            return "redirect:/";
        }

        try {
            pendingChangeService.rejectChange(id, currentUser.getUsername(), reason);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Änderung wurde abgelehnt.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Fehler beim Ablehnen: " + e.getMessage());
        }

        return "redirect:/admin/dashboard";
    }
}