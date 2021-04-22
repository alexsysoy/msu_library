package alex.msu.gradwork.repositories;

import alex.msu.gradwork.domain.Image;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Image, Long> {
}
