package alex.msu.gradwork.tools.exelFileUploadTool.service;

import alex.msu.gradwork.domain.Note;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExelService {

    void saveXLSFile(Long registerId, MultipartFile file);

}
