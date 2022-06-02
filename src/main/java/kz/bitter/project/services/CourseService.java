package kz.bitter.project.services;

import kz.bitter.project.entities.Chapters;
import kz.bitter.project.entities.Courses;
import kz.bitter.project.entities.Lessons;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseService {
    List<Courses> getAllCourses ();
    void saveCourse (Courses course);
    void saveChapter (Chapters chapter);
    Lessons saveLesson (Lessons lesson);

    void removeCourse (Long id);
    void removeChapter (Long id);
    void removeLesson (Long id);

    Courses getCourseById (Long id);
    Chapters getChapterById (Long id);
    Lessons getLessonbyId (Long id);

    List<Chapters> getChapterByCourseId (Long id);
    List<Lessons> getLessonsByChapterId (Long id);

    Page<Courses> getCoursesByPage (int page);
}
