package com.eventostec.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventRequestDTO;
import com.eventostec.api.service.EventService;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<Event> create(@ModelAttribute EventRequestDTO body) {
        Event event = eventService.createEvent(body);
        return ResponseEntity.ok(event);
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAll(@PathParam("page") Integer page, @PathParam("size") Integer size) {
        List<Event> events = eventService.getAllEvents(page, size);
        return ResponseEntity.ok(events);
    }
}
