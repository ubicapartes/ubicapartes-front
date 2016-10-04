package com.okiimport.app.mvvm.controladores;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;

import com.okiimport.app.model.Requerimiento;

public class MisRequerimientosEmitidosViewModel extends AbstractMisRequerimientosViewModel {

	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: listaMisRequerimientosEmitidos.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
		listaEstatus = llenarListaEstatusEmitidos();
		listaEstatus.add(estatusFiltro);
	}
	
	/**COMMAND*/
	/**
	 * Descripcion: permitira crear el emergente (modal) necesario para editar el requerimiento seleccionado
	 * Parametros: requerimiento @param view: listaMisRequerimientosEmitidos.zul  
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void editarReguerimiento(@BindingParam("requerimiento") final Requerimiento requerimiento){
		if(validaAcciones(requerimiento, "Editar")){
			requerimiento.setDetalleRequerimientos(consultarDetallesRequerimiento(requerimiento));
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("requerimiento", requerimiento);
			parametros.put("editar", true);
			crearModal(BasePackageSistemaFunc+"emitidos/editarRequerimiento.zul", parametros);
		}else
			mostrarMensaje("Error", "¡Ha expirado el tiempo para la edicion del requerimiento!", null, null, null, null);
	}
	
	/**
	 * Descripcion: permitira crear el emergente (modal) necesario para enviar las solicitudes de cotizacion
	 * a los proveedores del requerimiento seleccionado
	 * Parametros: Requerimiento: requerimiento seleccionado @param view: listaMisRequerimientosEmitidos.zul    
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void enviarProveedores(@BindingParam("requerimiento") final Requerimiento requerimiento){
		if(validaAcciones(requerimiento, "Enviar a proveedores")){
			if(!requerimiento.isCerrarSolicitud()){
				requerimiento.setDetalleRequerimientos(consultarDetallesRequerimiento(requerimiento));
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("requerimiento", requerimiento);
				crearModal(BasePackageSistemaFunc+"emitidos/enviarRequerimientoProv.zul", parametros);
			}
			else
				mostrarMensaje("Informaci\u00F3n", "Ha expirado el tiempo para Enviar a Proveedores", null, null, null, null);
		}else
			mostrarMensaje("Error", "¡Ha expirado el tiempo para Enviar a Proveedores!", null, null, null, null);
	}
	
	/**VALIDACION DE ACCIONES**/
	public boolean validaAcciones(Requerimiento requerimiento, String bandera){
		if(requerimiento.getFechaUltimaModificacion() != null){
			Date hoy = new Date();
			int transcurridos = obtener_dias_entre_2_fechas(requerimiento.getFechaUltimaModificacion(), hoy);
			if(bandera.equals("Editar")){
				if(transcurridos > 1)
					return false;
			}
			else if(bandera.equals("Enviar a proveedores")){
				if(transcurridos > 3)
					return false;
			}
		}else if(requerimiento.getFechaCreacion() != null){
			Date hoy = new Date();
			int transcurridos = obtener_dias_entre_2_fechas( requerimiento.getFechaCreacion(), hoy);
			if(bandera.equals("Editar")){
				if(transcurridos > 1)
					return false;
			}
			else if(bandera.equals("Enviar a proveedores")){
				if(transcurridos > 3)
					return false;
			}
		}
		return true;
	}
	
	/**RETORNA LA DIFERENCIA EN DIAS ENTRE DOS FECHAS*/
	public static int obtener_dias_entre_2_fechas(Date fechainicial, Date fechafinal) {

		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
		String fechainiciostring = df.format(fechainicial);
		try {
			fechainicial = df.parse(fechainiciostring);
		}
		catch (ParseException ex) {
		}

		String fechafinalstring = df.format(fechafinal);
		try {
			fechafinal = df.parse(fechafinalstring);
		}
		catch (ParseException ex) {
		}

		long fechainicialms = fechainicial.getTime();
		long fechafinalms = fechafinal.getTime();
		long diferencia = fechafinalms - fechainicialms;
		double dias = Math.floor(diferencia / 86400000L);// 3600*24*1000 
		return ( (int) dias);
	}


	/**METODOS OVERRIDE*/
	@Override
	protected Map<String, Object> consultarMisRequerimientos(String fieldSort, Boolean sortDirection, int page) {
		return sTransaccion.consultarMisRequerimientosEmitidos(requerimientoFiltro, 
				fieldSort, sortDirection,usuario.getPersona().getId(), page, pageSize);
	}

}
