package alex.msu.library.repositories;

import alex.msu.library.domain.DupSubject;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DupSubjectRepository extends PagingAndSortingRepository<DupSubject,Long> {
}
