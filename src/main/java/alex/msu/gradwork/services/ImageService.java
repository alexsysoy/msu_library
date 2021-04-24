package alex.msu.gradwork.services;

import alex.msu.gradwork.domain.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    void saveImageFile(Long registerId, MultipartFile file, String fileName);

    Image findById(Long l);
}
