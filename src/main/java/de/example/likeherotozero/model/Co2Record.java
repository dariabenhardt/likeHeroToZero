package de.example.likeherotozero.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Table(name = "co2_record")
public class Co2Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;
    private int year;
    private double emission;

    public Co2Record() {}

    public Co2Record(String country, int year, double emission) {
        this.country = country;
        this.year = year;
        this.emission = emission;
    }

    // Getter und Setter
    public Long getId() { return id; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public double getEmission() { return emission; }
    public void setEmission(double emission) { this.emission = emission; }
}
