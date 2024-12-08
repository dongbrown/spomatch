package com.spomatch.repository;

import com.spomatch.entity.SportsFacilityProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProgramRepository extends JpaRepository<SportsFacilityProgram, Long> {
    List<SportsFacilityProgram> findByLatitudeIsNull();
    List<SportsFacilityProgram> findAll();

    @Query(value = """
        SELECT 
            p.id, p.fclty_nm, p.progrm_nm, p.latitude, p.longitude,
            p.ctprvn_nm, p.signgu_nm, p.fclty_addr, p.fclty_tel_no,
            p.progrm_begin_de, p.progrm_end_de, p.progrm_prc,
            p.progrm_rcrit_nmpr_co, p.hmpg_url, p.progrm_trget_nm,
            p.progrm_estbl_wkday_nm, p.progrm_estbl_tizn_value
        FROM facility_program p
        WHERE MBRContains(
            ST_LineFromText(
                CONCAT(
                    'LineString(',
                    :minLng, ' ', :minLat, ',',
                    :maxLng, ' ', :maxLat, ')'
                )
            ),
            POINT(p.longitude, p.latitude)
        )
        AND p.latitude BETWEEN :minLat AND :maxLat
        AND p.longitude BETWEEN :minLng AND :maxLng
        AND p.latitude IS NOT NULL
        AND p.longitude IS NOT NULL
        LIMIT :limit OFFSET :offset
        """, nativeQuery = true)
    List<Object[]> findFacilitiesInBoundingBox(
            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLng") double minLng,
            @Param("maxLng") double maxLng,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    @Query("SELECT p FROM SportsFacilityProgram p " +
            "WHERE p.latitude BETWEEN :minLat AND :maxLat " +
            "AND p.longitude BETWEEN :minLng AND :maxLng " +
            "AND p.latitude IS NOT NULL " +
            "AND p.longitude IS NOT NULL")
    List<SportsFacilityProgram> findByLatitudeBetweenAndLongitudeBetween(
            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLng") double minLng,
            @Param("maxLng") double maxLng
    );
}
