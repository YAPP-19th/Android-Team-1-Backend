package com.example.delibuddy.domain.party;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PartyRepository extends JpaRepository<Party, Long> {

    @Query(value = "SELECT party.* FROM party WHERE ST_Distance_Sphere(coordinate, ST_GeomFromText(:wkt)) <= :meter ;", nativeQuery = true)
    List<Party> findPartiesNear(@Param("wkt") String wkt, @Param("meter") int meter);

    @Query(value = "SELECT party.* FROM party WHERE MBRContains(ST_GeomFromText(:wkt), coordinate)", nativeQuery = true)
    List<Party> findPartiesInGeom(@Param("wkt") String wkt);

}
