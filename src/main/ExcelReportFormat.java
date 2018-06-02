package main;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReportFormat{
	
	private XSSFWorkbook wb;
	private XSSFSheet sheet;
	
	private String username, message, date, answer;
	
	public ExcelReportFormat(String username, String date, String message, String answer){
		this.username = username;
		this.date = date;
		this.message = message;
		this.answer = answer;
		
	}
	
	
	public void createExcel(List<String[]> links) throws Exception{
		wb = new XSSFWorkbook();
	    sheet = (XSSFSheet) wb.createSheet("Mensajes");
	    
	    Row row = sheet.createRow(0);
	    row.createCell(0).setCellValue("COUNT");
	    row.createCell(1).setCellValue("REMITENTE");
	    row.createCell(2).setCellValue("DESTINATARIO");
	    row.createCell(3).setCellValue("LINK");
	    row.createCell(4).setCellValue("FECHA");
	    row.createCell(5).setCellValue("MENSAJE");
	    row.createCell(6).setCellValue("RESPUESTA");
	    
	    
	    Iterator<String[]> ite = links.iterator();
	    int row_count = 0; 
	    
	    row = sheet.createRow(1);
	    row.createCell(0).setCellValue(row_count);	// Count
	    row.createCell(5).setCellValue(message);	// Mensaje
	    
	    
	    while(ite.hasNext()){
	    	String[] data = ite.next();
	    	
	    	row = sheet.createRow(++row_count);
			
			row.createCell(0).setCellValue(row_count);	// Count
		    row.createCell(1).setCellValue(username);	// Remitente
	    	row.createCell(2).setCellValue(data[0]);	// Destinatario
	    	row.createCell(3).setCellValue(data[1]);	// Link
	    	row.createCell(4).setCellValue(date);		// Fecha
	 	    
	 	    row.createCell(6).setCellValue(answer);		// Respuesta
	    	
	    }
	    
	}
	
	public void writeExcel(String exlpath) throws Exception{
		
		exlpath = exlpath.replace("\\", "/");
		
		exlpath += "/Reporte " + date + " (0).xlsx";
		
		File file = new File(exlpath);
		
		int counter = 1;
		
		while(file.exists() && !file.isDirectory()){
			
			
			exlpath = exlpath.substring(0, exlpath.indexOf("(")) + "(" + counter++ + ").xlsx";
			
			file = new File(exlpath);
		}
		
		
		FileOutputStream fileOut = new FileOutputStream(file);
	    wb.write(fileOut);
	    fileOut.close();
	    
	}

	
	
}