package de.example.likeherotozero.repository;

import de.example.likeherotozero.model.Co2Record;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface Co2RecordRepository extends JpaRepository<Co2Record, Long>{
    Optional<Co2Record> findTopByCountryOrderByYearDesc(String country);
}
