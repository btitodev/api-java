package com.eventostec.api.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventDetailsDTO;
import com.eventostec.api.domain.event.EventRequestDTO;
import com.eventostec.api.domain.event.EventResponseDTO;
import com.eventostec.api.service.EventService;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<Event> create(@ModelAttribute EventRequestDTO body) {
        Event event = eventService.create(body);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<EventResponseDTO>> getAll(@PathParam("page") Integer page, @PathParam("size") Integer size) {
        List<EventResponseDTO> events = eventService.getUpComing(page, size);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<EventResponseDTO>> filterEvents(@PathParam("title") String title,
                                                                @PathParam("city") String city,
                                                                @PathParam("uf") String uf,
                                                                @PathParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                                @PathParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
                                                                @PathParam("page") Integer page,
                                                                @PathParam("size") Integer size) {
        List<EventResponseDTO> events = eventService.filterEvents(title, city, uf, startDate, endDate, page, size);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable("id") String id) {
        EventResponseDTO event = eventService.getById(UUID.fromString(id));
        return ResponseEntity.ok(event);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<EventDetailsDTO> getEventDetailsById(@PathVariable("id") String id) {
        EventDetailsDTO eventDetails = eventService.getDetailsById(UUID.fromString(id));
        return ResponseEntity.ok(eventDetails);
    }
}