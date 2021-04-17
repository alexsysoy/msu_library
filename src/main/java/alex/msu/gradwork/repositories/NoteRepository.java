package alex.msu.gradwork.repositories;

import alex.msu.gradwork.domain.Note;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NoteRepository extends PagingAndSortingRepository<Note, Long> {

}
