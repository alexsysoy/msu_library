package alex.msu.gradwork.repositories;

import alex.msu.gradwork.domain.Note;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface NoteRepository extends PagingAndSortingRepository<Note, Long> {

    Optional<Note> findNoteByNumber(Long l);

}
