package kz.bitter.project.services;

import kz.bitter.project.entities.Chapters;
import kz.bitter.project.entities.Courses;
import kz.bitter.project.entities.Lessons;

import java.util.List;

public interface CourseService {
    List<Courses> getAllCourses ();
    void saveCourse (Courses course);
    void saveChapter (Chapters chapter);
    void saveLesson (Lessons lesson);

    void removeCourse (Long id);
    void removeChapter (Long id);
    void removeLesson (Long id);

    Courses getCourseById (Long id);
    Chapters getChapterById (Long id);
    Lessons getLessonbyId (Long id);

    List<Chapters> getChapterByCourseId (Courses course);
    List<Lessons> getLessonsByChapterId (Chapters chapter);
}
