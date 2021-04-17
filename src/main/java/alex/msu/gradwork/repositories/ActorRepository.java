package alex.msu.gradwork.repositories;

import alex.msu.gradwork.domain.Actor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ActorRepository extends PagingAndSortingRepository<Actor, Long> {

    Optional<Actor> findByName(String name);
}
