package org.lc.kwengineadapter.identity_service.service;

import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.exception.BadRequestException;
import org.lc.kwengineadapter.common.exception.ResourceNotFoundException;
import org.lc.kwengineadapter.identity_service.dto.LegalFirmRequest;
import org.lc.kwengineadapter.identity_service.dto.LegalFirmResponse;
import org.lc.kwengineadapter.identity_service.entity.LegalFirm;
import org.lc.kwengineadapter.identity_service.repository.LegalFirmRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LegalFirmService {

    private final LegalFirmRepository legalFirmRepository;

    @Transactional
    public LegalFirmResponse createLegalFirm(LegalFirmRequest request) {
        if (legalFirmRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new BadRequestException("Registration number already exists");
        }

        LegalFirm firm = new LegalFirm();
        firm.setName(request.getName());
        firm.setRegistrationNumber(request.getRegistrationNumber());
        firm.setAddress(request.getAddress());
        firm.setCity(request.getCity());
        firm.setCountry(request.getCountry());
        firm.setPhoneNumber(request.getPhoneNumber());
        firm.setEmail(request.getEmail());
        firm.setDescription(request.getDescription());
        firm.setVerified(false);

        LegalFirm savedFirm = legalFirmRepository.save(firm);
        return LegalFirmResponse.fromEntity(savedFirm);
    }

    public LegalFirmResponse getLegalFirmById(Long id) {
        LegalFirm firm = legalFirmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LegalFirm", "id", id));
        return LegalFirmResponse.fromEntity(firm);
    }

    public List<LegalFirmResponse> getAllLegalFirms() {
        return legalFirmRepository.findAll().stream()
                .map(LegalFirmResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<LegalFirmResponse> getVerifiedLegalFirms() {
        return legalFirmRepository.findByVerified(true).stream()
                .map(LegalFirmResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public LegalFirmResponse updateLegalFirm(Long id, LegalFirmRequest request) {
        LegalFirm firm = legalFirmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LegalFirm", "id", id));

        if (!firm.getRegistrationNumber().equals(request.getRegistrationNumber())
                && legalFirmRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new BadRequestException("Registration number already exists");
        }

        firm.setName(request.getName());
        firm.setRegistrationNumber(request.getRegistrationNumber());
        firm.setAddress(request.getAddress());
        firm.setCity(request.getCity());
        firm.setCountry(request.getCountry());
        firm.setPhoneNumber(request.getPhoneNumber());
        firm.setEmail(request.getEmail());
        firm.setDescription(request.getDescription());

        LegalFirm updatedFirm = legalFirmRepository.save(firm);
        return LegalFirmResponse.fromEntity(updatedFirm);
    }

    @Transactional
    public void deleteLegalFirm(Long id) {
        if (!legalFirmRepository.existsById(id)) {
            throw new ResourceNotFoundException("LegalFirm", "id", id);
        }
        legalFirmRepository.deleteById(id);
    }

    @Transactional
    public LegalFirmResponse verifyLegalFirm(Long id) {
        LegalFirm firm = legalFirmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LegalFirm", "id", id));
        firm.setVerified(true);
        LegalFirm verifiedFirm = legalFirmRepository.save(firm);
        return LegalFirmResponse.fromEntity(verifiedFirm);
    }
}
