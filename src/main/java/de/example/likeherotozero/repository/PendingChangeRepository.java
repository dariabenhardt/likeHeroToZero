package de.example.likeherotozero.repository;

import de.example.likeherotozero.model.ApprovalStatus;
import de.example.likeherotozero.model.PendingChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PendingChangeRepository extends JpaRepository<PendingChange, Long> {

    // Alle ausstehenden Änderungen
    List<PendingChange> findByApprovalStatus(ApprovalStatus approvalStatus);

    // Änderungen eines bestimmten Users
    List<PendingChange> findBySubmittedBy(String username);

    // Ausstehende Änderungen eines Users
    List<PendingChange> findBySubmittedByAndApprovalStatus(String username, ApprovalStatus approvalStatus);
}
