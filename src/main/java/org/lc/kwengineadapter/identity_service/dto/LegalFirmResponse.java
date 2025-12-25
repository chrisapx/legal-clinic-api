package org.lc.kwengineadapter.identity_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lc.kwengineadapter.identity_service.entity.LegalFirm;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LegalFirmResponse {
    private Long id;
    private String name;
    private String registrationNumber;
    private String address;
    private String city;
    private String country;
    private String phoneNumber;
    private String email;
    private String description;
    private Integer lawyerCount;
    private Boolean verified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static LegalFirmResponse fromEntity(LegalFirm firm) {
        return new LegalFirmResponse(
                firm.getId(),
                firm.getName(),
                firm.getRegistrationNumber(),
                firm.getAddress(),
                firm.getCity(),
                firm.getCountry(),
                firm.getPhoneNumber(),
                firm.getEmail(),
                firm.getDescription(),
                firm.getLawyers() != null ? firm.getLawyers().size() : 0,
                firm.getVerified(),
                firm.getCreatedAt(),
                firm.getUpdatedAt()
        );
    }
}
