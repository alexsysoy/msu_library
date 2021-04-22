package alex.msu.gradwork.tools.exelFileUploadTool.service;

import org.springframework.web.multipart.MultipartFile;


public interface ExelService {

    void saveXLSFile(Long registerId, MultipartFile file);

}
