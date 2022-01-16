package com.cac.exchangerates.repository;

import com.cac.exchangerates.models.ConversionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ConversionRequestRepository extends JpaRepository<ConversionRequest, Long> {
    Page<ConversionRequest> findByIdOrDate(String id, LocalDate date, Pageable pageable);
}
