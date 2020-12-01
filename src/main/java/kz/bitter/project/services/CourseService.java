package kz.bitter.project.services;

import kz.bitter.project.entities.Courses;

import java.util.List;

public interface CourseService {
    List<Courses> getAllCourses ();
    void saveCourse (Courses courses);
}
