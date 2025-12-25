package org.lc.kwengineadapter.identity_service.repository;

import org.lc.kwengineadapter.identity_service.entity.LegalFirm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LegalFirmRepository extends JpaRepository<LegalFirm, Long> {
    Optional<LegalFirm> findByRegistrationNumber(String registrationNumber);
    List<LegalFirm> findByVerified(Boolean verified);
    boolean existsByRegistrationNumber(String registrationNumber);
}
