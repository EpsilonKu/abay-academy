package kz.bitter.project.repositories;

import kz.bitter.project.entities.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface EventRepository extends JpaRepository<Events, Long> {
    List<Events> findAll ();
}
