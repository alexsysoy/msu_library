package alex.msu.gradwork.repositories;

import alex.msu.gradwork.domain.DupActor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DupActorRepository extends PagingAndSortingRepository<DupActor, Long> {
}
