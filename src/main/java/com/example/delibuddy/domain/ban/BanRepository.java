package com.example.delibuddy.domain.ban;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BanRepository extends JpaRepository<Ban, Long> {

    @Query
    Optional<Ban> findByPartyIdAndUserId(Long partyId, Long userId);
}
