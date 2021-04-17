package alex.msu.gradwork.repositories;

import alex.msu.gradwork.domain.Register;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RegisterRepository extends PagingAndSortingRepository<Register, Long> {
}
