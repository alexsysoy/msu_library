package alex.msu.gradwork.repositories;

import alex.msu.gradwork.domain.Subject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface SubjectRepository  extends PagingAndSortingRepository<Subject,Long> {

    Optional<Subject> findByName(String name);
}
