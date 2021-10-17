package com.example.delibuddy.domain.party;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PartyUserRepository extends JpaRepository<PartyUser, Long> {

    @Query
    Long deleteByPartyIdAndUserId(Long partyId, Long userId);

    @Query
    Optional<PartyUser> findByPartyIdAndUserId(Long partyId, Long userId);
}
