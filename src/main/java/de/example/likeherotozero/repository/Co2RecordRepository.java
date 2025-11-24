package de.example.likeherotozero.repository;

import de.example.likeherotozero.model.Co2Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface Co2RecordRepository extends JpaRepository<Co2Record, Long>{
    List<Co2Record> findAllByCountry(String country);

    @Query("SELECT DISTINCT c.country FROM Co2Record c")
    List<String> findAllCountries();

    Co2Record findTopByCountryOrderByYearDesc(String country);
}
