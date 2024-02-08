package com.event.booking.repository;

import com.event.booking.enums.Category;
import com.event.booking.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query(
            "SELECT e FROM Event e " +
                    "WHERE (:name IS NULL OR e.name = :name) " +
                    "AND (:startDate IS NULL OR CAST(e.date AS DATE) >= CAST(:startDate AS DATE) ) " +
                    "AND (:endDate IS NULL OR CAST(e.date AS DATE) <= CAST(:endDate AS DATE) ) " +
                    "AND (:category IS NULL OR e.category = :category)"
    )
    List<Event> filterEvents(
            @Param("name") String name,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("category") Category category);


}
