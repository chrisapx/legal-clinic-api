package org.lc.kwengineadapter.identity_service.service;

import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.exception.BadRequestException;
import org.lc.kwengineadapter.common.exception.ResourceNotFoundException;
import org.lc.kwengineadapter.identity_service.dto.LawyerRequest;
import org.lc.kwengineadapter.identity_service.dto.LawyerResponse;
import org.lc.kwengineadapter.identity_service.entity.Lawyer;
import org.lc.kwengineadapter.identity_service.entity.LegalFirm;
import org.lc.kwengineadapter.identity_service.entity.User;
import org.lc.kwengineadapter.identity_service.repository.LawyerRepository;
import org.lc.kwengineadapter.identity_service.repository.LegalFirmRepository;
import org.lc.kwengineadapter.identity_service.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LawyerService {

    private final LawyerRepository lawyerRepository;
    private final UserRepository userRepository;
    private final LegalFirmRepository legalFirmRepository;

    @Transactional
    public LawyerResponse createLawyer(LawyerRequest request) {
        if (lawyerRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new BadRequestException("License number already exists");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));

        Lawyer lawyer = new Lawyer();
        lawyer.setUser(user);
        lawyer.setLicenseNumber(request.getLicenseNumber());
        lawyer.setSpecialization(request.getSpecialization());
        lawyer.setYearsOfExperience(request.getYearsOfExperience());
        lawyer.setBio(request.getBio());
        lawyer.setVerified(false);

        if (request.getLegalFirmId() != null) {
            LegalFirm firm = legalFirmRepository.findById(request.getLegalFirmId())
                    .orElseThrow(() -> new ResourceNotFoundException("LegalFirm", "id", request.getLegalFirmId()));
            lawyer.setLegalFirm(firm);
        }

        Lawyer savedLawyer = lawyerRepository.save(lawyer);
        return LawyerResponse.fromEntity(savedLawyer);
    }

    public LawyerResponse getLawyerById(Long id) {
        Lawyer lawyer = lawyerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lawyer", "id", id));
        return LawyerResponse.fromEntity(lawyer);
    }

    public List<LawyerResponse> getAllLawyers() {
        return lawyerRepository.findAll().stream()
                .map(LawyerResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<LawyerResponse> getLawyersByFirm(Long firmId) {
        return lawyerRepository.findByLegalFirmId(firmId).stream()
                .map(LawyerResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<LawyerResponse> getVerifiedLawyers() {
        return lawyerRepository.findByVerified(true).stream()
                .map(LawyerResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public LawyerResponse updateLawyer(Long id, LawyerRequest request) {
        Lawyer lawyer = lawyerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lawyer", "id", id));

        if (!lawyer.getLicenseNumber().equals(request.getLicenseNumber())
                && lawyerRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new BadRequestException("License number already exists");
        }

        lawyer.setLicenseNumber(request.getLicenseNumber());
        lawyer.setSpecialization(request.getSpecialization());
        lawyer.setYearsOfExperience(request.getYearsOfExperience());
        lawyer.setBio(request.getBio());

        if (request.getLegalFirmId() != null) {
            LegalFirm firm = legalFirmRepository.findById(request.getLegalFirmId())
                    .orElseThrow(() -> new ResourceNotFoundException("LegalFirm", "id", request.getLegalFirmId()));
            lawyer.setLegalFirm(firm);
        } else {
            lawyer.setLegalFirm(null);
        }

        Lawyer updatedLawyer = lawyerRepository.save(lawyer);
        return LawyerResponse.fromEntity(updatedLawyer);
    }

    @Transactional
    public void deleteLawyer(Long id) {
        if (!lawyerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Lawyer", "id", id);
        }
        lawyerRepository.deleteById(id);
    }

    @Transactional
    public LawyerResponse verifyLawyer(Long id) {
        Lawyer lawyer = lawyerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lawyer", "id", id));
        lawyer.setVerified(true);
        Lawyer verifiedLawyer = lawyerRepository.save(lawyer);
        return LawyerResponse.fromEntity(verifiedLawyer);
    }
}
