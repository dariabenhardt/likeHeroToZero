package de.example.likeherotozero.model;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private boolean enabled;

    // NEU: Rolle des Users
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public Users() {}

    // Getter und Setter
    public Long getId() { return id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean getEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    // NEU: Role Getter/Setter
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    // Hilfsmethoden für einfachere Prüfungen
    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    public boolean isScientist() {
        return role == UserRole.SCIENTIST;
    }
}