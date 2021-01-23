package kz.bitter.project.services;

import kz.bitter.project.entities.Events;

import java.util.List;

public interface EventService{
    List<Events> getAllEvents ();
    Events saveEvent (Events event);

    void removeEvent (Events event);

    Events getEventById (Long id);

}
