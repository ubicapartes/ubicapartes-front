package com.okiimport.app.mvvm.carga_masiva;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.okiimport.app.mvvm.model.WBookFactory;
import com.okiimport.app.mvvm.model.WBookFactory.TypeDocument;

public abstract class ProcesarDatosEstrategy<T extends Cloneable> {
	
	//Atributos
	private Class<T> claseInstance;
	
	private List<T> list;
	
	private int maxColumns;
	
	protected boolean failure = false;

	/**CONSTRUCTOR DE LA CLASE*/
	public ProcesarDatosEstrategy(Class<T> claseInstance, int maxColumns) {
		this.claseInstance = claseInstance;
		this.maxColumns = maxColumns;
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	/**Metodos Privados*/
	/**
	 * Descripcion: Obtendra las filas que dependeran del tipo de documento y la pagina en excel respectiva
	 * Parametros: @param type: tipo de documento
	 * @param strFileIn: representa los byte entrantes del archivo
	 * @param page: pagina de donde se extraeran los datos
	 * Retorno: @return rows: iterador de las filas del archivo
	 * Nota: se usa la factoria WBookFactory para la instanciacion del boox dependiendo del tipo de documento
	 * */
	private Iterator<Row> getRows(TypeDocument type, InputStream strFileIn, int page) {
		Iterator<Row> rows = null;

		Workbook boox = WBookFactory.create(type, strFileIn);
		
		if(boox != null){
			Sheet sheet = boox.getSheetAt(page);
			rows = sheet.iterator(); 
		}
		
		return rows;
	}
	
	/**
	 * Descripcion: Metodo que permitira obtener el valor de un atributo del objeto pasado como parametro
	 * Parametros: @param params: map que contiene el campo field que contendra el nombre del atributo de la clase
	 * @param object: objeto al que se le extraera el valor obtenido
	 * Retorno: @return object: valor del atributo del objeto
	 * Nota: Ninguna
	 * */
	private Object getField(Map<String, String> params, T object){
		try {
			String field = params.get("field");
			Method get = claseInstance.getMethod("get"+field.substring(0, 1).toUpperCase()+field.substring(1, field.length()));
			return get.invoke(object);
		} 
		catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Descripcion: funcion que permitira verficar si una columna es obligatoria dependiendo del rango pivote 
	 * entre la columna actual y la columna anterior leida
	 * Parametros: @param column: columna actual leida
	 * @param columnAnt: columna anterior leida
	 * @param object: objeto que contiene los valores leidos
	 * Retorno: Nombre de la columna que es obligatoria o null si la columna no es obligatoria
	 * Nota: Ninguna
	 * */
	private String checkRequiredColumn(int column , int columnAnt, T object){
		if(column==1 && columnAnt==0){ //Verifica si la columna base es obligatoria
			Map<String, String> parametros = defineRequiredColumn(0);
			if(parametros!=null)
				if(getField(parametros, object)!=null)
					return null;
				else
					return parametros.get("column");
			
		}
		if(column > 1 && columnAnt==0){ //Ocurre cuando la columna leida es mayor que la columna 1 y la columna anterior leida es la base
			String resp = null;
			for(int i=columnAnt; i<=column; i++){ //Se recorre desde la columna base hasta la columna leida
				Map<String, String> parametros = defineRequiredColumn((int) (i+columnAnt)/2); //el incremento entre las columnas se basan en la columnAnt como base
				if(parametros!=null)
					if(getField(parametros, object)==null)
						return parametros.get("column");
			}
			return resp;
		}
		if((column-columnAnt)==2){ //Ocurre cuando la diferencia entre las columnas es es mayor que 2 y no cumple con las condiciones anteriores
			Map<String, String> parametros = defineRequiredColumn ((int)(column+columnAnt)/2);
			if(parametros!=null)
				if(getField(parametros, object)!=null)
					return null;
				else
					return parametros.get("column");
		}
		else if((column-columnAnt)>2){ //Ocurre cuando la diferencia entre las columnas es mayor a 2
			String resp = null;
			int columnPivot = columnAnt; //Se toma como pivote la columnAnt y el incremento porcentual para la columna siguiente es 2,
										//No se verifica la obligatoriedad de la columna leida originalmente
			while(((resp=checkRequiredColumn(columnPivot+2, columnPivot, object))==null) && columnPivot<column){
				columnPivot+=1;
			}
			return resp;
		}
		
		return null;
	}
	
	/**Metodos Protected*/
	protected void mostrarMensaje(String mensaje, Cell cell){
		StringBuilder builder = new StringBuilder(mensaje);
		builder.append(" Fila Nro. ");
		builder.append(cell.getRowIndex()+1);
	}
	
	/**Metodos Publicos*/
	/**
	 * Descripcion: permitira cargar los datos del archivo y convertirlos a un arreglo de datos genericos T
	 * Parametros: @param type: tipo de archivo
	 * @param strArchivoIn: representa los byte entrantes del archivo
	 * @param page: pagina a leer
	 * Retorno: @return failure: retornara true si falla la carga del archivo y false en caso contrario
	 * Nota: Ninguna
	 * */
	public boolean cargarDatos(TypeDocument type, InputStream strArchivoIn, int page) 
			throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException {

		list = new ArrayList<T>();
		
		Iterator<Row> rows = getRows(type, strArchivoIn, page);
		rows.next(); //No se toma en cuenta el encabezado
		
		Row row;  //fila
		Iterator<Cell> columns; //columnas
		Cell cell = null; //celda
		
		T dato;
		
		String nameColumn;
		int columnAnt, column;

		while (rows.hasNext()) {

			//Instancia por fila
			row = rows.next();
			columns = row.cellIterator(); 								
			dato = claseInstance.newInstance(); //instancia del nuevo dato
			columnAnt = column = 0;

			System.out.println("FILA: "+row.getRowNum());

			while (columns.hasNext() && !failure){
				
				//Instancia por columna
				cell = columns.next();
				column = cell.getColumnIndex();
				
				System.out.println("COLUMNA: "+column+" Anterior: "+columnAnt);

				if((nameColumn=checkRequiredColumn(column , columnAnt, dato))==null){
					cargarDato(dato, cell.getColumnIndex(), cell);
					columnAnt = cell.getColumnIndex();
				}
				else {
					failure = true;
					//						Messagebox.show(
					//								"¡El campo "+nameColumn.toLowerCase()+" es obligatorio!"+ " " + "Fila nro" + (cell.getRowIndex()+1),
					//										"Información", Messagebox.OK, Messagebox.ERROR);
				}
			}

			if(!failure)
				list.add(dato);
			else {
				list.clear();
				list = null;
			}
						
			if(!failure && column<this.maxColumns-1 && cell!=null){
				nameColumn=checkRequiredColumn(maxColumns, column, dato);
				if(nameColumn!=null){
					//						Messagebox.show(
					//								"¡El campo "+nameColumn.toLowerCase()+" es obligatorio!"+ " " + "Fila nro" + (cell.getRowIndex()+1),
					//										"Información", Messagebox.OK, Messagebox.ERROR);
					failure = true;
					break;
				}
			}
			if(failure)
				break;
		}
		return failure;
	}
	
	/**Metodos Abstractos*/
	/**
	 * Descripcion: Permitira definir cualquier columna del archivo que se obligatoria
	 * Parametros: Ninguno
	 * Retorno: @return map: map que contendra los parametros 
	 * 							-> column: nombre de la columna y -> field: nombre del atributo de la clase
	 * Nota: Si el retorno es null, entonces la columna no es obligatoria
	 * */
	protected abstract Map<String, String> defineRequiredColumn(int column);
	
	/**
	 * Descripcion: Permitira cargar el dato de la columna pasada como parametro al objeto pasado como parametro
	 * Parametros: @param model: objeto del modelo que contiene la informacion leida
	 * @param column: entero que representa el numero de la columna
	 * @param cell: celda que se esta leyendo
	 * Retorno: @return model: objeto del modelo que contiene la informacion leida
	 * Nota: El objeto model es un parametro de salida
	 * */
	protected abstract T cargarDato(T modelo, int column, Cell cell);

	/**GETTERS Y SETTERS*/
	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
}
