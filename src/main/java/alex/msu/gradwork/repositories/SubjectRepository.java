package alex.msu.gradwork.repositories;

import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.domain.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface SubjectRepository extends PagingAndSortingRepository<Subject,Long> {

    Optional<Subject> findByName(String name);

    Page<Subject> findSubjectsByRegisterId(Long registerId, Pageable pageable);
}
