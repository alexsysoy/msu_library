package alex.msu.gradwork.repositories;

import alex.msu.gradwork.domain.DupSubject;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DupSubjectRepository extends PagingAndSortingRepository<DupSubject,Long> {
}
