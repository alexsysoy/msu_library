package alex.msu.library.services;

import alex.msu.library.domain.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    void saveImageFile(Long registerId, MultipartFile file, String fileName);

    Image findById(Long l);
}
