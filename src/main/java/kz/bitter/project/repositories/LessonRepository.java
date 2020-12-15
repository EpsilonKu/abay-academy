package kz.bitter.project.repositories;

import kz.bitter.project.entities.Chapters;
import kz.bitter.project.entities.Lessons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface LessonRepository extends JpaRepository <Lessons,Long> {
    List<Lessons> getByChapter (Chapters chapters);
    void deleteAllByChapterCourseId (Long id);
    void deleteAllByChapterId (Long id);
}
