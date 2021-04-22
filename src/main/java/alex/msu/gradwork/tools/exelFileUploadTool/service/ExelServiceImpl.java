package alex.msu.gradwork.tools.exelFileUploadTool.service;

import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.domain.Register;
import alex.msu.gradwork.repositories.NoteRepository;
import alex.msu.gradwork.repositories.RegisterRepository;
import alex.msu.gradwork.tools.exelFileUploadTool.helper.ExelHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@Service
public class ExelServiceImpl implements ExelService {

    private final RegisterRepository registerRepository;
    private final NoteRepository noteRepository;

    public ExelServiceImpl(RegisterRepository registerRepository, NoteRepository noteRepository) {
        this.registerRepository = registerRepository;
        this.noteRepository = noteRepository;
    }

//    @Override
//    public void save(MultipartFile file) {
//        try {
//            List<Note> notes = ExelHelper.excelToNotes(file.getInputStream());
//            noteRepository.saveAll(notes);
//
//        } catch (IOException e) {
//            throw new RuntimeException("fail to store excel data: " + e.getMessage());
//        }
//    }

    @Override
    public void saveXLSFile(Long registerId, MultipartFile file) {

        Register register = registerRepository.findById(registerId).get();

        try {
            Set<Note> notes = ExelHelper.excelToNotes(file.getInputStream());
            for (Note note : notes) {
                register.addNote(note);
            }
            registerRepository.save(register);

        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }

    }
}
