package alex.msu.library.tools.exelFileUploadTool.helper;

import alex.msu.library.domain.Note;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class ExelHelper {

    public static Set<Note> excelToNotes (InputStream inputStream) throws IOException {

        Set<Note> notes = new HashSet<>();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet worksheet = workbook.getSheetAt(0);
        for(int i = 1; i<worksheet.getPhysicalNumberOfRows() ;i++) {
            Note note = new Note();

            XSSFRow row = worksheet.getRow(i);

            note.setNumber((long) row.getCell(0).getNumericCellValue());
            note.setAnnotation(row.getCell(1).getStringCellValue());

            notes.add(note);
        }

        return notes;
    }

    public static Set<Note> excelToNotesHSSF (InputStream inputStream) throws IOException {

        Set<Note> notes = new HashSet<>();
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet worksheet = workbook.getSheetAt(0);
        for(int i = 1; i<worksheet.getPhysicalNumberOfRows() ;i++) {
            Note note = new Note();

            HSSFRow row = worksheet.getRow(i);

            note.setNumber((long) row.getCell(0).getNumericCellValue());
            note.setAnnotation(row.getCell(1).getStringCellValue());

            notes.add(note);
        }

        return notes;
    }

}
