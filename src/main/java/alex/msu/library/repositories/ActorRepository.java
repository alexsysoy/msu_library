package alex.msu.library.repositories;

import alex.msu.library.domain.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ActorRepository extends PagingAndSortingRepository<Actor, Long> {

    Page<Actor> findActorsByRegisterId(Long registerId, Pageable pageable);
}
