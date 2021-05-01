package alex.msu.gradwork.controllers;

import alex.msu.gradwork.domain.Image;
import alex.msu.gradwork.services.ImageService;
import alex.msu.gradwork.services.RegisterService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class ImageController {

    private final ImageService imageService;
    private final RegisterService registerService;

    public ImageController(ImageService imageService, RegisterService registerService) {
        this.imageService = imageService;
        this.registerService = registerService;
    }


    // Открываем форму для добавления Изображения
    @GetMapping("/images/{registerId}/load")
    public String showUpLoadForm(@PathVariable String registerId, Model model){

        // Передаём Опись
        model.addAttribute("register", registerService.findById(Long.valueOf(registerId)));

        return "images/imageLoader";
    }


    // Сохраняем изображение. Название задает диапазон.
    // Например 103_118 данная фотография актуальна для Дел от 103 по 118 включая
    @PostMapping("/images/{registerId}/load")
    public String handleImagePost(@PathVariable String registerId,
                                  @RequestParam("imageFile") MultipartFile file,
                                  @RequestParam("fileName") String fileName) {

        imageService.saveImageFile(Long.valueOf(registerId), file, fileName);
        return "redirect:/page/registers/" + registerId + "/notes";
    }

    // Показываем Изображение
    @GetMapping("/images/{ImageId}/imageShow/display")
    public void renderImageFromDB(@PathVariable String ImageId,
                                  HttpServletResponse response) throws IOException{
        Image image = imageService.findById(Long.valueOf(ImageId));

        if (image != null) {
            byte[] byteArray = new byte[image.getBytes().length];
            int i = 0;

            for (Byte wrappedByte : image.getBytes()) {
                byteArray[i++] = wrappedByte;
            }

            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(byteArray);
            IOUtils.copy(is, response.getOutputStream());
        }
    }

    // Направляем на просмотр изображения
    @GetMapping("/images/{ImageId}/imageShow")
    public String imageNoteShow(@PathVariable String ImageId,
                                Model model) {

        model.addAttribute("image", imageService.findById(Long.valueOf(ImageId)));

        return "images/imageShow";
    }

}
