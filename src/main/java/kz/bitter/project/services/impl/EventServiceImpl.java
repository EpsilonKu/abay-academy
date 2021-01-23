package kz.bitter.project.services.impl;

import kz.bitter.project.entities.Events;
import kz.bitter.project.repositories.EventRepository;
import kz.bitter.project.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableWebSecurity
public class EventServiceImpl implements EventService {
    @Autowired
    EventRepository eventRepository;

    @Override
    public List<Events> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Events saveEvent(Events event) {
        return eventRepository.save(event);
    }

    @Override
    public void removeEvent(Events event) {
        eventRepository.delete(event);
    }

    @Override
    public Events getEventById(Long id) {
        return eventRepository.getOne(id);
    }
}
