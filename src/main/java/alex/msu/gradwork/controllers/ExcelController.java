package alex.msu.gradwork.controllers;


import alex.msu.gradwork.services.RegisterService;
import alex.msu.gradwork.tools.exelFileUploadTool.service.ExelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@Slf4j
public class ExcelController {

    private final ExelService fileService;
    private final RegisterService registerService;
    private final ExelService exelService;

    public ExcelController(ExelService fileService, RegisterService registerService, ExelService exelService) {
        this.fileService = fileService;
        this.registerService = registerService;
        this.exelService = exelService;
    }

    // Открываем форму для добавления Файл
    @GetMapping("/files/{registerId}/load")
    public String showUploadForm(@PathVariable String registerId, Model model){

        // Передаём Опись
        model.addAttribute("register", registerService.findById(Long.valueOf(registerId)));

        return "files/fileLoader";
    }


    // Отправляем файл на распознание
    @PostMapping("/files/{registerId}/load")
    public String mapReapExcelDataToDB(@RequestParam("file") MultipartFile file,
                                       @PathVariable String registerId) throws IOException {

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        switch (extension) {
            case "xls":
                exelService.saveXLSFileHSSF(Long.valueOf(registerId), file);
                log.debug("Файл с расширением {} добавлен", extension);
                return "redirect:/page/registers/" + registerId + "/notes";

            case "xlsx":
                exelService.saveXLSFile(Long.valueOf(registerId), file);
                log.debug("Файл с расширением {} добавлен", extension);
                return "redirect:/page/registers/" + registerId + "/notes";
            default:
                log.debug("Данный файл имеет неверное расширение");
                return "error";
        }

    }


}


