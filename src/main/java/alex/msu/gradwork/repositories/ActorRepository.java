package alex.msu.gradwork.repositories;

import alex.msu.gradwork.domain.Actor;
import alex.msu.gradwork.domain.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ActorRepository extends PagingAndSortingRepository<Actor, Long> {

    Page<Actor> findActorsByRegisterId(Long registerId, Pageable pageable);
}
