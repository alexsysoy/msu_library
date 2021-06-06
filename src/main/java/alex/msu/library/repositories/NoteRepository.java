package alex.msu.library.repositories;

import alex.msu.library.domain.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.Set;

public interface NoteRepository extends PagingAndSortingRepository<Note, Long> {

    Optional<Note> findNoteByNumber(Long l);

    Page<Note> findNotesByRegisterId(Long registerId, Pageable pageable);

    Set<Note> findNotesByRegisterId(Long registerId);

}
