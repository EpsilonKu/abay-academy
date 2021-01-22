package kz.bitter.project.repositories;

import kz.bitter.project.entities.Groups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface GroupRepository extends JpaRepository<Groups, Long> {
    List<Groups> findAll ();
}
