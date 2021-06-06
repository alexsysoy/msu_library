package alex.msu.library.tools.exelFileUploadTool.service;

import alex.msu.library.domain.Note;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;


public interface ExelService {

    void saveXLSFile(Long registerId, MultipartFile file);

    void saveXLSFileHSSF(Long registerId, MultipartFile file);

    //Сохраняем файл полученный из выборки
    void saveSelectionFile(Set<Note> notes);
}
