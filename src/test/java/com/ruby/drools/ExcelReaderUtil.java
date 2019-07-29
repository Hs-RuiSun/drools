package com.ruby.drools;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReaderUtil {
    public static List<Row> read(String xlsPath) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(xlsPath));
        Sheet sheet = workbook.getSheetAt(0);
        List<Row> rows = new ArrayList<>();
        sheet.forEach(rows::add);
        return rows;
    }
}
