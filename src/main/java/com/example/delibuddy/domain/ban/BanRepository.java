package com.example.delibuddy.domain.ban;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BanRepository extends JpaRepository<Ban, Long> {

    Optional<Ban> findByUserAndParty();
}
