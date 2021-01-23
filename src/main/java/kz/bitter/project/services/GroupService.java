package kz.bitter.project.services;

import kz.bitter.project.entities.Courses;
import kz.bitter.project.entities.Groups;

import java.util.List;

public interface GroupService {
    List<Groups> getAllGroups ();
    Groups getGroupById (Long id);
    Groups saveGroups (Groups groups);

    Groups saveCourseToGroup (Groups group, Courses course);
    void kickCourseFromGroup (Groups group, Courses course);

    void removeGroups (Groups groups);
}
