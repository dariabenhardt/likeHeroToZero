package de.example.likeherotozero.controller;

import de.example.likeherotozero.model.Co2Record;
import de.example.likeherotozero.repository.Co2RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/co2")
public class Co2Controller {

    @Autowired
    private final Co2RecordRepository repository;

    public Co2Controller(Co2RecordRepository repository) {
        this.repository = repository;
    }
// GET: Alle Datensätze
    @GetMapping
    public List<Co2Record> getAll () {
        return repository.findAll ();
    }
// GET: Der neueste Datensatz nach Land
    @GetMapping("/{country}")
    public Optional<Co2Record> getLatest(@PathVariable String country) {
        return repository.findTopByCountryOrderByYearDesc(country);
    }

// POST: Datensatz wird neu hinzugefügt
    @PostMapping
    public Co2Record create(@RequestBody Co2Record co2Record) {
        return repository.save(co2Record);
    }
// PUT: Datensatz wird aktualisiert
    @PutMapping ("/{id}")
    public ResponseEntity<Co2Record> updateRecord(@PathVariable int id, @RequestBody Co2Record updatedRecord) {
        return Co2RecordRepository.findById(id)
                .map(record -> {
                    record.setCountry(updatedRecord.getCountry());
                    record.setYear(updatedRecord.getYear());
                    record.setEmission(updatedRecord.getEmission());
                    Co2Record saved = Co2RecordRepository.save(record);
                    return ResponseEntity.ok().body(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE: Datensatz wird gelöscht
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable int id) {
        return Co2RecordRepository.findById(id)
                .map(record -> {
                    Co2RecordRepository.delete(record);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
