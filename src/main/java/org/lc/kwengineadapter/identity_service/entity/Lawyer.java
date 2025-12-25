package org.lc.kwengineadapter.identity_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.lc.kwengineadapter.common.entity.BaseEntity;

@Entity
@Table(name = "lawyers")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Lawyer extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, unique = true)
    private String licenseNumber;

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false)
    private Integer yearsOfExperience;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @ManyToOne
    @JoinColumn(name = "legal_firm_id")
    private LegalFirm legalFirm;

    @Column(nullable = false)
    private Boolean verified = false;
}
