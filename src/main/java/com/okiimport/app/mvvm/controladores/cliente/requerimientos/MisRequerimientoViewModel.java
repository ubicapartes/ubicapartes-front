package com.okiimport.app.mvvm.controladores.cliente.requerimientos;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;

import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.mvvm.controladores.AbstractMisRequerimientosViewModel;

public class MisRequerimientoViewModel extends AbstractMisRequerimientosViewModel  {
	
	
	
	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: listaMisRequerimientosEmitidos.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
		listaEstatus = llenarListaEstatusGeneral();
		listaEstatus.add(estatusFiltro);
	}
	
	/**COMMAND*/
	@Command
	public void registarNuevoRequerimiento(){
		crearModal(BasePackageSistemaFunc+"usuario/registrarRequerimiento.zul", null);
	}
	
	@Command
	public void mostrarOfertas(@BindingParam("requerimiento") Requerimiento requerimiento){
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("requerimiento", requerimiento);
		crearModal(BasePackageSistemaFunc+"usuario/listaOfertasCliente.zul", parametros);
		
		
	}

	/**METODOS OVERRIDE*/
	@Override
	protected Map<String, Object> consultarMisRequerimientos(String fieldSort, Boolean sortDirection, int page) {
		return this.sTransaccion.consultarRequerimientosCliente(this.requerimientoFiltro, fieldSort, sortDirection, 
				usuario.getPersona().getCedula(), page, pageSize);
	}	
}
