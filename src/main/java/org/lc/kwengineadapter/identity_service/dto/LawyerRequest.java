package org.lc.kwengineadapter.identity_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LawyerRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @NotNull(message = "Years of experience is required")
    @Min(value = 0, message = "Years of experience must be non-negative")
    private Integer yearsOfExperience;

    private String bio;

    private Long legalFirmId;
}
