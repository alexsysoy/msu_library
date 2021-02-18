package alex.msu.gradwork.repositories;

import alex.msu.gradwork.domain.Actor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ActorRepository extends CrudRepository<Actor, Long> {

    Optional<Actor> findByName(String name);
}
