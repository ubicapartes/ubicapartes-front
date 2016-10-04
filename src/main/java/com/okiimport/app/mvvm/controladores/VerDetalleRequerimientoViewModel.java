package com.okiimport.app.mvvm.controladores;

import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;

import com.okiimport.app.model.DetalleRequerimiento;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.maestros.SMaestros;
import com.okiimport.app.service.transaccion.STransaccion;

public class VerDetalleRequerimientoViewModel extends AbstractRequerimientoViewModel  {

	//Servicios
	@BeanInjector("sMaestros")
	private SMaestros sMaestros;
	
	@BeanInjector("sTransaccion")
	private STransaccion sTransaccion;
	
	//Atributos
	private Requerimiento requerimiento;
	
	private String titulo;
	
	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view:  
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view, 
			@ExecutionArgParam("requerimiento") Requerimiento requerimiento)
	{
		super.doAfterCompose(view);
		System.out.println(view.getId());
		this.requerimiento = requerimiento;
		this.titulo = "Requerimiento Nro. "+this.requerimiento.getIdRequerimiento();
		List<DetalleRequerimiento> detallesRequerimiento 
			= (List<DetalleRequerimiento>) sTransaccion
				.consultarDetallesRequerimiento(requerimiento.getIdRequerimiento(), 0, -1).get("detallesRequerimiento");
		this.requerimiento.setDetalleRequerimientos(detallesRequerimiento);
	}
	
	
	/** METODOS PROPIOS DE LA CLASE */

	/** GETTERS Y SETTERS */
	public STransaccion getsTransaccion() {
		return sTransaccion;
	}

	public void setsTransaccion(STransaccion sTransaccion) {
		this.sTransaccion = sTransaccion;
	}
	
	public Requerimiento getRequerimiento() {
		return requerimiento;
	}

	public void setRequerimiento(Requerimiento requerimiento) {
		this.requerimiento = requerimiento;
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}	
}
