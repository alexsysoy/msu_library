package alex.msu.gradwork.repositories;

import alex.msu.gradwork.domain.Subject;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SubjectRepository  extends CrudRepository<Subject,Long> {

    Optional<Subject> findByName(String name);
}
