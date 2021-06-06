package alex.msu.library.tools.exelFileUploadTool.service;

import alex.msu.library.domain.Note;
import alex.msu.library.domain.Register;
import alex.msu.library.repositories.RegisterRepository;
import alex.msu.library.tools.exelFileUploadTool.helper.ExelHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

@Service
@Slf4j
public class ExelServiceImpl implements ExelService {

    private final RegisterRepository registerRepository;

    public ExelServiceImpl(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    //Сохраняем в Описи Дела, полученные из файла
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

    @Override
    public void saveXLSFileHSSF(Long registerId, MultipartFile file) {

        Register register = registerRepository.findById(registerId).get();

        try {
            Set<Note> notes = ExelHelper.excelToNotesHSSF(file.getInputStream());
            for (Note note : notes) {
                register.addNote(note);
            }
            registerRepository.save(register);

        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    //Сохраняем файл полученный из выборки
    @Override
    public void saveSelectionFile(Set<Note> notes) {


        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Выборка");

        // счетчик для строк
        int rowNum = 0;

        // создаем подписи к столбцам (это будет первая строчка в листе Excel файла)
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue("Номер");
        row.createCell(1).setCellValue("Заголовок дела");
        row.createCell(2).setCellValue("Примечание");
        row.createCell(3).setCellValue("Предметных указателей");
        row.createCell(4).setCellValue("Именных указателей");

        for (Note note : notes) {
            createSheetHeader(sheet, ++rowNum, note);
        }


        try {
//            FileOutputStream out = new FileOutputStream(new File("C:\\Users\\Alex\\Desktop\\Diplom\\Apache POI Excel File.xls"));
            FileOutputStream out = new FileOutputStream(new File(".\\Selection.xls"));
            workbook.write(out);
            out.close();

        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
        log.debug("Excel файл успешно создан");


    }

    private static void createSheetHeader(HSSFSheet sheet, int rowNum, Note note) {
        Row row = sheet.createRow(rowNum);

        row.createCell(0).setCellValue(note.getNumber());
        row.createCell(1).setCellValue(note.getAnnotation());
        row.createCell(2).setCellValue(note.getMemo());
        row.createCell(3).setCellValue(note.getSubjects().size());
        row.createCell(4).setCellValue(note.getActors().size());
    }
}
