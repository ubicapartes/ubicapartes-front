package com.okiimport.app.mvvm.controladores;

import java.text.DecimalFormat;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import com.okiimport.app.model.Configuracion;
import com.okiimport.app.model.Moneda;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;


public class ConfVariablesViewModel extends AbstractRequerimientoViewModel  {

	
	//Servicios
	    
	    @Wire("#winConfVariables")
		private Window winConfVariables;
	    
	   
	    @Wire("#txtValorLibra")
	    private Textbox txtValorLibra;
	    
	    @Wire("#txtPorcentajeGanancia")
	    private Textbox txtPorcentajeGanancia;
	    
	    @Wire("#txtPorcentajeIVA")
	    private Textbox txtPorcentajeIVA;
		
	//Atributos
		private Moneda monedaSeleccionada;
		
		private Configuracion configuracion;
		private Configuracion conf;
		private String pIva=null;
		private String pGanancia=null;
		private String vLibra=null;
	
	
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view/*,  @ExecutionArgParam("configuracion") Configuracion configuracion*/)
	{System.out.println("conf");
		try{
			
		
		super.doAfterCompose(view);
		limpiar();
		
		this.configuracion = sControlConfiguracion.consultarConfiguracionActual();
		this.conf = sControlConfiguracion.consultarConfiguracionActual();
		DecimalFormat formateador = new DecimalFormat("##.#"); 
		float aux1, aux2, aux3=0;
		aux1=configuracion.getPorctIva()*100;
		aux1=formateador.parse(formateador.format(aux1)).floatValue();
		System.out.println("aux1"+aux1);
		this.pIva=Float.toString(aux1);
		aux2=configuracion.getPorctGanancia()*100;
		
		aux2=formateador.parse(formateador.format(aux2)).floatValue();

		System.out.println("aux2"+aux2);
		this.pGanancia=Float.toString(aux2);
		aux3=configuracion.getValorLibra()*100;
		
		aux3=formateador.parse(formateador.format(aux3)).floatValue();

		System.out.println("aux"+aux3);
		this.vLibra=Float.toString(aux3);
		//this.configuracion = configuracion;
		
		
		System.out.println("conf"+this.configuracion.getPorctIva());
		
		
	}	catch (Exception e) {
	     System.out.println(e);
	}
	}
	
	
	@Command
	@NotifyChange("*")
	public void enviar(@BindingParam("btnEnviar") Button btnEnviar,
			@BindingParam("btnLimpiar") Button btnLimpiar){

		if(checkIsFormValid()){
			configuracion.setPorctIva(Float.parseFloat(pIva)/100);
			configuracion.setPorctGanancia(Float.parseFloat(pGanancia)/100);
			configuracion.setValorLibra(Float.parseFloat(vLibra)/100);
			if((configuracion.getPorctGanancia().equals(conf.getPorctGanancia()))&&(configuracion.getPorctIva().equals(conf.getPorctIva()))&&(configuracion.getValorLibra().equals(conf.getValorLibra())))
				mostrarMensaje("Informaci\u00F3n", "No existen cambios en la configuracion", null, null, null,null);
			else {

				btnEnviar.setDisabled(true);
				btnLimpiar.setDisabled(true);
				mostrarMensaje("Confirmacion", "¿Esta seguro que desea realizar el cambio de configuracion?", Messagebox.EXCLAMATION, new Messagebox.Button[]{Messagebox.Button.YES,Messagebox.Button.NO}, 
						new EventListener(){

					@Override
					public void onEvent(Event event) throws Exception {
						Messagebox.Button button = (Messagebox.Button) event.getData();
						if (button == Messagebox.Button.YES) {
							sControlConfiguracion.guardarConfiguracion(configuracion); 
							winConfVariables.onClose();
						}
					}
				}, null);
			}
		}
	}

	
	
	
	
	/**COMMAND*/
	/**
	 * Descripcion: Permite limpiar los campos de la pantalla ConfVariables
	 * Parametros: @param view: confVariables.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange( "*" )
	public void limpiar() {
		
		try{
		this.pIva=null;
		this.pGanancia=null;
		this.vLibra=null;
		super.cleanConstraintForm();
	}catch (Exception e) {
	     System.out.println(e);
	}}
		

	

	public Moneda getMonedaSeleccionada() {
		return monedaSeleccionada;
	}


	public void setMonedaSeleccionada(Moneda monedaSeleccionada) {
		this.monedaSeleccionada = monedaSeleccionada;
	}


	public Configuracion getConfiguracion() {
		return configuracion;
	}


	public void setConfiguracion(Configuracion configuracion) {
		this.configuracion = configuracion;
	}


	public String getpIva() {
		return pIva;
	}


	public void setpIva(String pIva) {
		this.pIva = pIva;
	}


	

	public String getpGanancia() {
		return pGanancia;
	}


	public void setpGanancia(String pGanancia) {
		this.pGanancia = pGanancia;
	}


	public String getvLibra() {
		return vLibra;
	}


	public void setvLibra(String vLibra) {
		this.vLibra = vLibra;
	}

	
	
}
