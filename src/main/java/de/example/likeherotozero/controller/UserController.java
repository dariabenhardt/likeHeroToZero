package de.example.likeherotozero.controller;

import de.example.likeherotozero.model.Users;
import de.example.likeherotozero.model.UserRole;
import de.example.likeherotozero.repository.UserRepository;
import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
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

                // Je nach Rolle zum passenden Dashboard weiterleiten
                if (user.isAdmin()) {
                    return "redirect:/admin/dashboard";
                } else if (user.isScientist()) {
                    return "redirect:/scientist/dashboard";
                } else {
                    return "redirect:/";
                }
            }
        }
        model.addAttribute("error", "Ungültiger Benutzername oder Passwort");
        return "redirect:/?error=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}