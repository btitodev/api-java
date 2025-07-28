package com.eventostec.api.repositories;

import com.eventostec.api.domain.event.Event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    @Query("SELECT e FROM Event e LEFT JOIN e.address a WHERE e.date >= CURRENT_DATE ORDER BY e.date ASC")
    Page<Event> findAllUpcomingEvents(Pageable pageable);

    @Query("SELECT e FROM Event e LEFT JOIN e.address a " +
            "WHERE e.date >= CURRENT_DATE AND " +
            "(:title IS NULL OR e.title LIKE %:title%) AND " +
            "(:city IS NULL OR a.city LIKE %:city%) AND " +
            "(:uf IS NULL OR a.uf LIKE %:uf%) AND " +
            "e.date BETWEEN COALESCE(:startDate, e.date) AND COALESCE(:endDate, e.date) " +
            "ORDER BY e.date ASC")
    Page<Event> findByFilters(String title, String city, String uf, Date startDate, Date endDate, Pageable pageable);

}
