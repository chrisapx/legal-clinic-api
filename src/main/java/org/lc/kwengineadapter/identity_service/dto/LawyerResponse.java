package org.lc.kwengineadapter.identity_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lc.kwengineadapter.identity_service.entity.Lawyer;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LawyerResponse {
    private Long id;
    private UserResponse user;
    private String licenseNumber;
    private String specialization;
    private Integer yearsOfExperience;
    private String bio;
    private Long legalFirmId;
    private String legalFirmName;
    private Boolean verified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static LawyerResponse fromEntity(Lawyer lawyer) {
        return new LawyerResponse(
                lawyer.getId(),
                UserResponse.fromEntity(lawyer.getUser()),
                lawyer.getLicenseNumber(),
                lawyer.getSpecialization(),
                lawyer.getYearsOfExperience(),
                lawyer.getBio(),
                lawyer.getLegalFirm() != null ? lawyer.getLegalFirm().getId() : null,
                lawyer.getLegalFirm() != null ? lawyer.getLegalFirm().getName() : null,
                lawyer.getVerified(),
                lawyer.getCreatedAt(),
                lawyer.getUpdatedAt()
        );
    }
}
