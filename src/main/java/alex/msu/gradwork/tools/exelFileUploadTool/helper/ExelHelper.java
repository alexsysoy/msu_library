package alex.msu.gradwork.tools.exelFileUploadTool.helper;

import alex.msu.gradwork.domain.Note;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
public class ExelHelper {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    //проверяем на exel-формат
    public static boolean hasExcelFormat(MultipartFile file) {

        return TYPE.equals(file.getContentType());
    }

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


//
//    public static List<Note> excelToNotes (InputStream inputStream) {
//        try {
//
//            log.debug("excelToNotes");
//            Workbook workbook = new XSSFWorkbook(inputStream);
//
//            Sheet sheet = workbook.getSheet(SHEET);
//            Iterator<Row> rows = sheet.iterator();
//
//            List<Note> notes = new ArrayList<>();
//
//            int rowNumber = 0;
//
//
//            while (rows.hasNext()) {
//                Row currentRow = rows.next();
//                System.out.println(currentRow.toString());
//
//                // Пропускаем заголовок таблицы
//                if (rowNumber == 0) {
//                    rowNumber++;
//                    continue;
//                }
//
//                Iterator<Cell> cellsInRow = currentRow.iterator();
//
//                Note note = new Note();
//
//                int cellIdx = 0;
//                while (cellsInRow.hasNext()) {
//                    Cell currentCell = cellsInRow.next();
//
//                    switch (cellIdx) {
//                        case 0:
//                            note.setNumber((long) currentCell.getNumericCellValue());
//                            break;
//                        case 1:
//                            note.setAnnotation(currentCell.getStringCellValue());
//                            break;
//                        default:
//                            break;
//                    }
//
//                    cellIdx++;
//
//                    }
//
//                notes.add(note);
//
//                }
//
//            workbook.close();
//
//            return notes;
//
//        } catch (IOException e) {
//            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
//        }
//    }
}
