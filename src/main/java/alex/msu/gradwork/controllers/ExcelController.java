package alex.msu.gradwork.controllers;


import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.services.RegisterService;
import alex.msu.gradwork.tools.exelFileUploadTool.service.ExelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        return "/files/fileLoader";
    }


    // Отправляем файл на распознание
    @PostMapping("/files/{registerId}/load")
    public String mapReapExcelDataToDB(@RequestParam("file") MultipartFile file,
                                       @PathVariable String registerId) throws IOException {

        exelService.saveXLSFile(Long.valueOf(registerId), file);
        return "redirect:/page/registers/" + registerId + "/notes";

    }


}


