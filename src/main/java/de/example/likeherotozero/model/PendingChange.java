package de.example.likeherotozero.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pending_changes")
public class PendingChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ChangeType changeType; // NEW oder UPDATE

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status")
    private ApprovalStatus approvalStatus; // PENDING, APPROVED, REJECTED

    // Referenz zum Original-Datensatz (null bei NEW)
    private Long originalRecordId;

    // Die vorgeschlagenen Daten
    private String country;
    private Integer year;
    private Double co2;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_status")
    private DataStatus dataStatus; // VERIFIED, ESTIMATED, etc. - für die CO2-Daten

    // Wer hat die Änderung vorgeschlagen?
    private String submittedBy;
    private LocalDateTime submittedAt;

    // Wer hat genehmigt/abgelehnt?
    private String reviewedBy;
    private LocalDateTime reviewedAt;
    private String rejectionReason;

    // Begründung für die Änderung
    @Column(length = 1000)
    private String changeReason;

    // Konstruktoren
    public PendingChange() {
        this.submittedAt = LocalDateTime.now();
        this.approvalStatus = ApprovalStatus.PENDING;
    }

    // Getter und Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ChangeType getChangeType() { return changeType; }
    public void setChangeType(ChangeType changeType) { this.changeType = changeType; }

    public ApprovalStatus getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(ApprovalStatus approvalStatus) { this.approvalStatus = approvalStatus; }

    public Long getOriginalRecordId() { return originalRecordId; }
    public void setOriginalRecordId(Long originalRecordId) { this.originalRecordId = originalRecordId; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public Double getCo2() { return co2; }
    public void setCo2(Double co2) { this.co2 = co2; }

    // WICHTIG: Umbenennung von getStatus/setStatus zu getDataStatus/setDataStatus
    public DataStatus getDataStatus() { return dataStatus; }
    public void setDataStatus(DataStatus dataStatus) { this.dataStatus = dataStatus; }

    public String getSubmittedBy() { return submittedBy; }
    public void setSubmittedBy(String submittedBy) { this.submittedBy = submittedBy; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public String getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(String reviewedBy) { this.reviewedBy = reviewedBy; }

    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public String getChangeReason() { return changeReason; }
    public void setChangeReason(String changeReason) { this.changeReason = changeReason; }
}