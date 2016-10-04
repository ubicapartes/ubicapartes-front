package com.okiimport.app.mvvm.model;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public final class WBookFactory {
	
	public enum TypeDocument {
		XLS("xls"),
		XLSX("xlsx");
		
		String extension;
		TypeDocument(String extension){
			this.extension = extension;
		}
		
		public static TypeDocument switchName(String name){
			switch(name){
			case "xls": return TypeDocument.XLS;
			case "xlsx": return TypeDocument.XLSX;
			default: return null;
			}
		}
		
	}
	
	public static Workbook create(TypeDocument type, InputStream strRutaArchivoIn){
		try {
			if(type == TypeDocument.XLS)
				return new HSSFWorkbook(new POIFSFileSystem(strRutaArchivoIn));
			else if(type == TypeDocument.XLSX)
				return (XSSFWorkbook) WorkbookFactory.create(strRutaArchivoIn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}