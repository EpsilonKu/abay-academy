package kz.bitter.project.services;

import kz.bitter.project.entities.Groups;

import java.util.List;

public interface GroupService {
    List<Groups> getAllGroups ();
    Groups getGroupById (Long id);
    Groups saveGroups (Groups groups);

    void removeGroups (Groups groups);
}
