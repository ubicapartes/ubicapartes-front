package com.okiimport.app.mvvm.controladores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Default;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;

import com.okiimport.app.model.Proveedor;
import com.okiimport.app.model.Requerimiento;

public class ListaProveedoresCotizarViewModel extends ListaProveedoresViewModel {
	
	
	//Atributos
	private Requerimiento requerimiento;
	
	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: listaProveedoresCotizar.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@AfterCompose(superclass=true)
	public void doAfterCompose(@ExecutionArgParam("requerimiento") Requerimiento requerimiento){
		this.requerimiento = requerimiento;
		consultarProveedores(0, null, null);
	}
	
	/**GLOBAL COMMAND OVERRIDE*/
	@GlobalCommand
	@Override
	@NotifyChange("proveedores")
	public void consultarProveedores(@Default("0") @BindingParam("page") int page, 
			@BindingParam("fieldSort") String fieldSort, 
			@BindingParam("sortDirection") Boolean sortDirection){
		if(requerimiento!=null){
			Map<String, Object> parametros = sMaestros.consultarProveedoresConSolicitudCotizaciones(proveedorFiltro, 
					requerimiento.getIdRequerimiento(), fieldSort, sortDirection, page, pageSize);
			Integer total = (Integer) parametros.get("total");
			proveedores = (List<Proveedor>) parametros.get("proveedores");
			pagProveedores.setActivePage(page);
			pagProveedores.setTotalSize(total);
		}
	}
	
	/**COMMAND*/
	/**
	 * Descripcion: Permitira visualizar la lista de proveedores para poder cotizar un requerimiento
	 * Parametros: @param view: listaProveedoresCotizar.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void cotizar(@BindingParam("proveedor") Proveedor proveedor){
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("requerimiento", this.requerimiento);
		parametros.put("persona", proveedor);
		if(proveedor.getTipoProveedor())
			crearModal(BasePackageSistemaFunc+"en_proceso/listaCotizacionesProveedorNacional.zul", parametros);
		else
			crearModal(BasePackageSistemaFunc+"en_proceso/listaCotizacionesProveedorInternacional.zul", parametros);
	}
	
	/**METODOS OVERRIDE*/
	@Command
	@Override
	public void closeModal(){
		super.closeModal();
		this.ejecutarGlobalCommand("cambiarRequerimientos", null);
	}

	/**GETTERS Y SETTERS*/
}
