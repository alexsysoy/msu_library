package alex.msu.gradwork.repositories;

import alex.msu.gradwork.domain.Note;
import org.springframework.data.repository.CrudRepository;

public interface NoteRepository extends CrudRepository<Note, Long> {
}
