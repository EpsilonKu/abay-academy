package kz.bitter.project.repositories;

import kz.bitter.project.entities.Chapters;
import kz.bitter.project.entities.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ChapterRepository extends JpaRepository <Chapters,Long> {
    List <Chapters> findByCourseId (Long id);
    void deleteAllByCourseId (Long id);

}
