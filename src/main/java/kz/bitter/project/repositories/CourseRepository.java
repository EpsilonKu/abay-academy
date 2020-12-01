package kz.bitter.project.repositories;

import kz.bitter.project.entities.Courses;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository <Courses, Long> {
    List<Courses> findAll ();
}
