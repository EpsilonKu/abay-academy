package kz.bitter.project.services;

import kz.bitter.project.entities.Events;
import kz.bitter.project.entities.Groups;

import java.util.List;

public interface EventService{
    List<Events> getAllEvents ();
    Events saveEvent (Events event);

    void removeEvent (Events event);

    Events saveGroupToEvent (Groups group, Events event);
    Events kickGroupFromEvent (Groups group, Events event);

    Events getEventById (Long id);

}
