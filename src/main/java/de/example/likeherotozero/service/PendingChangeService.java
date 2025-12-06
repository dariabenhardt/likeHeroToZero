package de.example.likeherotozero.service;

import de.example.likeherotozero.model.Co2Record;
import de.example.likeherotozero.model.DataStatus;
import de.example.likeherotozero.model.PendingChange;
import de.example.likeherotozero.model.ChangeType;
import de.example.likeherotozero.model.ApprovalStatus;
import de.example.likeherotozero.repository.Co2RecordRepository;
import de.example.likeherotozero.repository.PendingChangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PendingChangeService {

    @Autowired
    private PendingChangeRepository pendingChangeRepository;

    @Autowired
    private Co2RecordRepository co2RecordRepository;

    /**
     * Wissenschaftler schlägt einen neuen Datensatz vor
     */
    public PendingChange proposeNewRecord(String country, int year, double co2,
                                          DataStatus status, String username, String reason) {
        PendingChange change = new PendingChange();
        change.setChangeType(ChangeType.NEW);
        change.setCountry(country);
        change.setYear(year);
        change.setCo2(co2);
        change.setDataStatus(status);  // GEÄNDERT: setDataStatus statt setStatus
        change.setSubmittedBy(username);
        change.setChangeReason(reason);

        return pendingChangeRepository.save(change);
    }

    /**
     * Wissenschaftler schlägt Änderung eines bestehenden Datensatzes vor
     */
    public PendingChange proposeUpdate(Long originalRecordId, String country, int year,
                                       double co2, DataStatus status, String username, String reason) {
        PendingChange change = new PendingChange();
        change.setChangeType(ChangeType.UPDATE);
        change.setOriginalRecordId(originalRecordId);
        change.setCountry(country);
        change.setYear(year);
        change.setCo2(co2);
        change.setDataStatus(status);  // GEÄNDERT: setDataStatus statt setStatus
        change.setSubmittedBy(username);
        change.setChangeReason(reason);

        return pendingChangeRepository.save(change);
    }

    /**
     * Admin genehmigt eine Änderung
     */
    @Transactional
    public void approveChange(Long changeId, String adminUsername) {
        Optional<PendingChange> optionalChange = pendingChangeRepository.findById(changeId);

        if (optionalChange.isEmpty()) {
            throw new RuntimeException("Änderung nicht gefunden");
        }

        PendingChange change = optionalChange.get();

        if (change.getApprovalStatus() != ApprovalStatus.PENDING) {
            throw new RuntimeException("Diese Änderung wurde bereits bearbeitet");
        }

        // Je nach Typ die Aktion ausführen
        if (change.getChangeType() == ChangeType.NEW) {
            // Neuen Datensatz estellen
            Co2Record newRecord = new Co2Record(
                    change.getCountry(),
                    change.getYear(),
                    change.getCo2(),
                    change.getDataStatus()
            );
            co2RecordRepository.save(newRecord);

        } else if (change.getChangeType() == ChangeType.UPDATE) {
            // Bestehenden Datensatz aktualisieren
            Optional<Co2Record> optionalRecord = co2RecordRepository.findById(change.getOriginalRecordId());

            if (optionalRecord.isPresent()) {
                Co2Record record = optionalRecord.get();
                record.setCountry(change.getCountry());
                record.setYear(change.getYear());
                record.setCo2(change.getCo2());
                record.setStatus(change.getDataStatus());
                co2RecordRepository.save(record);
            }
        }

        // Status der Änderung aktualisieren
        change.setApprovalStatus(ApprovalStatus.APPROVED);
        change.setReviewedBy(adminUsername);
        change.setReviewedAt(LocalDateTime.now());
        pendingChangeRepository.save(change);
    }

    /**
     * Admin lehnt eine Änderung ab
     */
    public void rejectChange(Long changeId, String adminUsername, String reason) {
        Optional<PendingChange> optionalChange = pendingChangeRepository.findById(changeId);

        if (optionalChange.isEmpty()) {
            throw new RuntimeException("Änderung nicht gefunden");
        }

        PendingChange change = optionalChange.get();

        if (change.getApprovalStatus() != ApprovalStatus.PENDING) {
            throw new RuntimeException("Diese Änderung wurde bereits bearbeitet");
        }

        change.setApprovalStatus(ApprovalStatus.REJECTED);
        change.setReviewedBy(adminUsername);
        change.setReviewedAt(LocalDateTime.now());
        change.setRejectionReason(reason);

        pendingChangeRepository.save(change);
    }

    /**
     * Alle ausstehenden Änderungen für Admin
     */
    public List<PendingChange> getPendingChanges() {
        return pendingChangeRepository.findByApprovalStatus(ApprovalStatus.PENDING);
    }

    /**
     * Alle Änderungen eines bestimmten Users
     */
    public List<PendingChange> getUserChanges(String username) {
        return pendingChangeRepository.findBySubmittedBy(username);
    }

    /**
     * Eine einzelne Änderung per ID laden
     */
    public Optional<PendingChange> getChangeById(Long id) {
        return pendingChangeRepository.findById(id);
    }
}