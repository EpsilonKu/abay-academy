package kz.bitter.project.services.impl;

import kz.bitter.project.entities.Courses;
import kz.bitter.project.repositories.CourseRepository;
import kz.bitter.project.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableWebSecurity
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourseRepository repository;

    @Override
    public List<Courses> getAllCourses() {
        return repository.findAll();
    }

    @Override
    public void saveCourse(Courses courses) {
        repository.save(courses);
    }
}
