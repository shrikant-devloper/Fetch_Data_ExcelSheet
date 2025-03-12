package com.digiquad.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.digiquad.model.RowData;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

@Service
public class FileService {
	public List<RowData> processFile(MultipartFile file, int startRow, int endRow) throws IOException {
		String fileName = file.getOriginalFilename();
		if (fileName != null && fileName.endsWith(".csv")) {
			return processCsv(file, startRow);
		} else if (fileName != null && (fileName.endsWith(".xls") || fileName.endsWith(".xlsx"))) {
			return processExcel(file, startRow, endRow);
		} else {
			throw new IOException("Unsupported file");
		}
	}

	private List<RowData> processExcel(MultipartFile file, int startRow, int endRow) throws IOException {
		List<RowData> rowDataList = new ArrayList<>();
		try (InputStream input = file.getInputStream(); Workbook book = new XSSFWorkbook(input)) {
			Sheet sheet = book.getSheetAt(0);
			int rowNum = 0;
			
			for (Row row : sheet) {
				if (rowNum++ < startRow)
					continue;
				List<String> columns = new ArrayList<>();
				if (rowNum <= endRow+1) {
					for (Cell cell : row) {
						columns.add(cell.toString());
					}
				}
				rowDataList.add(new RowData(columns));
			}
		}

		return rowDataList;
	}

	private List<RowData> processCsv(MultipartFile file, int startRow) throws IOException {
		List<RowData> rowDataList = new ArrayList<>();
		try (Reader reader = new InputStreamReader(file.getInputStream());
				CSVReader csvReader = new CSVReader(reader);) {
			String[] nextRecord;

			int rowNum = 0;

			try {
				while ((nextRecord = csvReader.readNext()) != null) {
					if (rowNum++ < startRow)
						continue;
					rowDataList.add(new RowData(Arrays.asList(nextRecord)));
				}
			} catch (CsvValidationException | IOException e) {

				e.printStackTrace();
			}
		}
		return rowDataList;
	}

	public RowData getOnlyValue(MultipartFile file1, int startRow1, int col) throws Exception {
		RowData rowData=new RowData();
		String str = null;
		try (InputStream inputStream = file1.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {

			Sheet sheet = workbook.getSheetAt(0); // Read the first sheet
			//str = sheet.getRow(startRow1).getCell(col).toString();
//	            for (int i = startRow1; i <= sheet.getLastRowNum(); i++) { // Start from the given row
//	                Row row = sheet.getRow(i);
//	                if (row != null) {
//	                    Cell cell = row.getCell(col);
//	                    if (cell != null) {
//	                        rowDataList.add(getCellValue(cell));
//	                    }
//	                }
//	            }
			int rowNum=0;
			int colNum=0;
			for(Row row1: sheet)
			{
				if(rowNum++ == startRow1)
				{
					for(Cell cell: row1)
					{
						if(colNum++ == col)
						{
							rowData.setValue(cell.toString());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rowData;

	}
}