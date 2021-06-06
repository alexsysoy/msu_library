package alex.msu.library.repositories;

import alex.msu.library.domain.Register;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RegisterRepository extends PagingAndSortingRepository<Register, Long> {
}
