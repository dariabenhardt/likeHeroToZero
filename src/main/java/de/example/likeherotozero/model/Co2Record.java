package de.example.likeherotozero.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "co2_data")
public class Co2Record  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;
    private int year;

    @Column(name = "co2")
    private double co2;

    @Enumerated(EnumType.STRING)
    private DataStatus status;

    public Co2Record() {}

    public Co2Record(String country, int year, double co2, DataStatus status) {
        this.country = country;
        this.year = year;
        this.co2 = co2;
        this.status = status;
    }

    // Getter und Setter
    public Long getId() { return id; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public double getCo2() { return co2; }
    public void setCo2(double co2) { this.co2 = co2; }
    public DataStatus getStatus() { return status; }
    public void setStatus(DataStatus status) { this.status = status; }
}
