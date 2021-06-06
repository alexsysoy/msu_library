package alex.msu.library.services;

import alex.msu.library.domain.Image;
import alex.msu.library.domain.Register;
import alex.msu.library.repositories.ImageRepository;
import alex.msu.library.repositories.RegisterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final RegisterRepository registerRepository;

    public ImageServiceImpl(ImageRepository imageRepository, RegisterRepository registerRepository) {
        this.imageRepository = imageRepository;
        this.registerRepository = registerRepository;
    }

    // Сохраняем изображение:
    // Создаём новое изображение
    // С помощью имени изображения(например 105_110) привязываем Изображение к Делам от 105 до 110 включительно
    // Если некорректное имя выводим в лог сообщение
    @Override
    @Transactional
    public void saveImageFile(Long registerId, MultipartFile file, String fileName) {

        Optional<Register> registerOptional = registerRepository.findById(registerId);
        if (registerOptional.isEmpty()){
            log.error("Опись с id {} не найдена", registerId);
        }

        Register register = registerOptional.get();

        //Создаём новое Изображение
        Image image = new Image();
        // Вносим имя типа 45_123 (Диапазон)
        image.setImageName(fileName);
        String[] parts = fileName.split("_");


        // Ищем все Дела из диапазона
        register.getNotes().stream()
                .filter(x -> ((x.getNumber() >= Long.parseLong(parts[0])) && (x.getNumber() <= Long.parseLong(parts[1]))))
                .forEach(image::addNote);

        // Загружаем файл
        try{
            Byte[] byteObject = new Byte[file.getBytes().length];

            int i = 0;
            for (byte b : file.getBytes()){
                byteObject[i++] = b;
            }
            image.setBytes(byteObject);
            imageRepository.save(image);

        } catch (IOException e) {
            log.error("Error", e);
            e.printStackTrace();
        }

        log.debug("Сохраняем изображение");
    }

    @Override
    public Image findById(Long l) {

        Optional<Image> imageOptional = imageRepository.findById(l);

        if (imageOptional.isEmpty()){
            log.error("Нет изображения с id: " + l);
        }

        Image image = imageOptional.get();

        return image;
    }
}
