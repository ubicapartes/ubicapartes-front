package com.okiimport.app.mvvm.controladores;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

import com.okiimport.app.model.Configuracion;
import com.okiimport.app.model.Oferta;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;

public class VerDetalleOfertaViewModel extends AbstractRequerimientoViewModel {

	//Servicios
	
	//GUI
    @Wire("#winOferta")
	private Window winOferta;
    
	//Atributos
	private Requerimiento requerimiento;
	private Oferta oferta;

	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: verDetalleOferta.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("requerimiento") Requerimiento requerimiento,
			@ExecutionArgParam("oferta") Oferta oferta)
	{
		super.doAfterCompose(view);
		this.requerimiento = requerimiento;
		this.oferta = oferta;
		prepararOferta();
	}

	/** METODOS PROPIOS DE LA CLASE */
	/**
	 * Descripcion: permitira agregar el porcenaje de iva y el porcentaje de ganancia a la oferta seleccionada
	 * @param Ninguno.
	 * Retorno: Ninguno
	 * Nota: Ninguna.
	 * */
	private void prepararOferta(){
		Configuracion configuracion = sControlConfiguracion.consultarConfiguracionActual();
		oferta.setPorctIva(configuracion.getPorctIva());
		oferta.setPorctGanancia(configuracion.getPorctGanancia());
	}

	/** GETTERS Y SETTERS */
	public Requerimiento getRequerimiento() {
		return requerimiento;
	}

	public void setRequerimiento(Requerimiento requerimiento) {
		this.requerimiento = requerimiento;
	}

	public Oferta getOferta() {
		return oferta;
	}

	public void setOferta(Oferta oferta) {
		this.oferta = oferta;
	}

}
