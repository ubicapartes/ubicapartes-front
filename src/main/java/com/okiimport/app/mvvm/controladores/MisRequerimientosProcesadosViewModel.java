package com.okiimport.app.mvvm.controladores;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;

import com.okiimport.app.model.Requerimiento;

public class MisRequerimientosProcesadosViewModel extends AbstractMisRequerimientosViewModel {

	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: listaMisRequerimientosProcesados.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
		listaEstatus = llenarListaEstatusProcesados();
		listaEstatus.add(estatusFiltro);
	}
	
	/**COMMAND*/
	/**
	 * Descripcion: permitira crear el emergente (modal) necesario para aprobar las cotizaciones del requerimiento seleccionado
	 * @param requerimiento: requerimiento seleccionado
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void seleccionarCotizaciones(@BindingParam("requerimiento") final Requerimiento requerimiento){
		requerimiento.setDetalleRequerimientos(consultarDetallesRequerimiento(requerimiento));
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("requerimiento", requerimiento);
		crearModal(BasePackageSistemaFunc+"en_proceso/listaCreacionOfertas.zul", parametros);
	}
	
	/**COMMAND*/
	/**
	 * Descripcion: 
	 * @param requerimiento: requerimiento seleccionado
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void cotizacionesASeleccionar(@BindingParam("requerimiento") final Requerimiento requerimiento){
		requerimiento.setDetalleRequerimientos(consultarDetallesRequerimiento(requerimiento));
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("requerimiento", requerimiento);
		crearModal(BasePackageSistemaFunc+"en_proceso/listaCreacionOfertas.zul", parametros);
	}
	
	/**
	 * Descripcion: Permitira visualizar las Ofertas para luego enviar al cliente respectivo
	 * Parametros: Requerimiento: requerimiento seleccionado @param view: listaMisRequerimientosProcesados.zul    
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void enviarOferta(@BindingParam("requerimiento") final Requerimiento requerimiento){
		requerimiento.setDetalleRequerimientos(consultarDetallesRequerimiento(requerimiento));
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("requerimiento", requerimiento);
		parametros.put("size", "90%");
		crearModal(BasePackageSistemaFunc+"ofertados/listaOfertasCliente.zul", parametros );
	}
	
	/**
	 * Descripcion: Permitira visualizar la lista de cotizaciones a editar del requerimiento seleccionado
	 * Parametros: Requerimiento: requerimiento seleccionado @param view: listaMisRequerimientosProcesados.zul    
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void editarCotizaciones(@BindingParam("requerimiento") final Requerimiento requerimiento){//EC
		requerimiento.setDetalleRequerimientos(consultarDetallesRequerimiento(requerimiento));
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("requerimiento", requerimiento);
		crearModal(BasePackageSistemaFunc+"en_proceso/listaCotizacionesAnalista.zul", parametros);
	}
	

	/**METODOS OVERRIDE*/
	@Override
	protected Map<String, Object> consultarMisRequerimientos(String fieldSort, Boolean sortDirection, int page) {
		return sTransaccion.consultarMisRequerimientosProcesados(requerimientoFiltro, 
				fieldSort, sortDirection,usuario.getPersona().getId(), page, pageSize);
	}
	
}
