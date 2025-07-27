package com.eventostec.api.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventRequestDTO;
import com.eventostec.api.domain.event.EventResponseDTO;
import com.eventostec.api.repositories.EventRepository;

@Service
public class EventService {

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private EventRepository repository;

    @Value("${aws.bucket.name}")
    private String awsBucketName;

    public Event createEvent(EventRequestDTO eventRequest) {
        String imageUrl = null;

        if (eventRequest.image() != null) {
            imageUrl = this.uploadImage(eventRequest.image());
        }

        Event newEvent = new Event();
        newEvent.setTitle(eventRequest.title());
        newEvent.setDescription(eventRequest.description());
        newEvent.setDate(new Date(eventRequest.date()));
        newEvent.setRemote(eventRequest.remote());
        newEvent.setEventUrl(eventRequest.eventUrl());
        newEvent.setImageUrl(imageUrl);

        repository.save(newEvent);

        return newEvent;
    }

    private String uploadImage(MultipartFile multipartFile) {
        String imageName = UUID.randomUUID().toString() + "-" + multipartFile.getOriginalFilename();
        try {
            File file = this.convertMultipartToFile(multipartFile);
            s3Client.putObject(awsBucketName, imageName, file);
            file.delete();
            return s3Client.getUrl(awsBucketName, imageName).toString();
        } catch (Exception e) {
            System.out.println("Error uploading image: " + e.getMessage());
            return null;
        }
    }

    private File convertMultipartToFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot convert an empty file to a File object");
        }

        File convFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }

    public Event getById(UUID eventId) {
        return repository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found for ID: " + eventId));
    }

    public List<EventResponseDTO> getAllEvents(Integer page, Integer size) {
        if (page == null || page < 0) {
            page = 0;
        }
        if (size == null || size < 1) {
            size = 10;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventsPage = repository.findAll(pageable);
        return eventsPage
                .map(event -> new EventResponseDTO(event.getId(), event.getTitle(), event.getDescription(),
                        event.getDate(), "", "", event.getRemote(), event.getEventUrl(), event.getImageUrl()))
                .stream().toList();
    }

}
