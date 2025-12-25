package org.lc.kwengineadapter.identity_service.repository;

import org.lc.kwengineadapter.identity_service.entity.Lawyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LawyerRepository extends JpaRepository<Lawyer, Long> {
    Optional<Lawyer> findByLicenseNumber(String licenseNumber);
    List<Lawyer> findByLegalFirmId(Long legalFirmId);
    List<Lawyer> findByVerified(Boolean verified);
    boolean existsByLicenseNumber(String licenseNumber);
}
