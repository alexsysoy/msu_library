package alex.msu.library.repositories;

import alex.msu.library.domain.DupActor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DupActorRepository extends PagingAndSortingRepository<DupActor, Long> {
}
