package kz.bitter.project.services.impl;

import kz.bitter.project.entities.Chapters;
import kz.bitter.project.entities.Courses;
import kz.bitter.project.entities.Lessons;
import kz.bitter.project.repositories.ChapterRepository;
import kz.bitter.project.repositories.CourseRepository;
import kz.bitter.project.repositories.LessonRepository;
import kz.bitter.project.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableWebSecurity
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ChapterRepository chapterRepository;

    @Autowired
    LessonRepository lessonRepository;

    @Override
    public void saveCourse(Courses courses) {
        courseRepository.save(courses);
    }
    @Override
    public void saveChapter(Chapters chapter) {
        chapterRepository.save(chapter);
    }
    @Override
    public Lessons saveLesson(Lessons lesson) {
        return lessonRepository.save(lesson);
    }

    @Override
    public void removeCourse(Long id) {
        lessonRepository.deleteAllByChapterCourseId(id);
        chapterRepository.deleteAllByCourseId(id);
        courseRepository.deleteById(id);
    }
    @Override
    public void removeChapter(Long id) {
        lessonRepository.deleteAllByChapterId(id);
        chapterRepository.deleteById(id);
    }
    @Override
    public void removeLesson(Long id) {
        lessonRepository.deleteById(id);
    }

    @Override
    public List<Courses> getAllCourses() {
        return courseRepository.findAll();
    }
    @Override
    public Courses getCourseById(Long id) {
        return courseRepository.getOne(id);
    }
    @Override
    public Chapters getChapterById(Long id) {
        return chapterRepository.getOne(id);
    }
    @Override
    public Lessons getLessonbyId(Long id) {
        return lessonRepository.getOne(id);
    }

    @Override
    public List<Chapters> getChapterByCourseId(Long id) {
        return chapterRepository.findByCourseId(id);
    }
    @Override
    public List<Lessons> getLessonsByChapterId(Long id) {
        return lessonRepository.findByChapterId(id);
    }

}
