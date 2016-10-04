package com.okiimport.app.mvvm.carga_masiva;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;

import com.okiimport.app.model.DetalleRequerimiento;

public class PDDetalleRequerimientoEstrategy extends ProcesarDatosEstrategy<DetalleRequerimiento> {

	public PDDetalleRequerimientoEstrategy(){
		this(3);
	}
	
	public PDDetalleRequerimientoEstrategy(int maxColumns) {
		super(DetalleRequerimiento.class, (maxColumns <= 3) ? maxColumns : 3);
	}

	@Override
	protected Map<String, String> defineRequiredColumn(int column) {
		Map<String, String> parametros = null;
		switch (column) {
		case 1: { 
			parametros = new HashMap<String, String>();
			parametros.put("column" , "Descripcion");
			parametros.put("field", "descripcion");
			break;
		}
		case 2: {
			parametros = new HashMap<String, String>();
			parametros.put("columna" , "Cantidad");
			parametros.put("field", "cantidad");
			break;
		}
		default: break;
		}
		return parametros;
	}

	@Override
	protected DetalleRequerimiento cargarDato(DetalleRequerimiento detalle, int column, Cell cell) {
		String value = null;
		switch(column){
		case 0: {
			if(cell.getCellType() ==  Cell.CELL_TYPE_STRING){
				value = cell.getStringCellValue().trim();
				if(!value.equals("")){
					detalle.setCodigoOem(value);
				}
			}
		}; break;
		case 1: {
			if(cell.getCellType() ==  Cell.CELL_TYPE_STRING){
				value = cell.getStringCellValue();
				if(!value.trim().equals("")){
					detalle.setDescripcion(value);
				}
				else {
					failure = false;
					mostrarMensaje("¡Falta la descripcion del repuesto en el archivo!", cell);
				}
			}
		}; break;
		case 2: {
			if(cell.getCellType() ==  Cell.CELL_TYPE_NUMERIC){
				Double cantidad = cell.getNumericCellValue();
				if(cantidad > 0){
					detalle.setCantidad(cantidad.longValue());
				}
				else {
					failure = false;
					mostrarMensaje("¡La cantidad del repuesto debe ser mayor a 0!", cell);
				}
			}
			else {
				failure = false;
				mostrarMensaje("¡Falta la cantidad del repuesto en el archivo!", cell);
			}
		}; break;
		default: break;
		}
		return detalle;
	}

}
