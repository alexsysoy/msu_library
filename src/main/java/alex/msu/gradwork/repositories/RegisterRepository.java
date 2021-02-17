package alex.msu.gradwork.repositories;

import alex.msu.gradwork.domain.Register;
import org.springframework.data.repository.CrudRepository;

public interface RegisterRepository extends CrudRepository<Register, Long> {
}
