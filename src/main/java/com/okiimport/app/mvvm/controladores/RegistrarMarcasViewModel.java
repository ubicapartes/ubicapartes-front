package com.okiimport.app.mvvm.controladores;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;

import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;

import org.zkoss.zul.Window;

import com.okiimport.app.model.Analista;
import com.okiimport.app.model.MarcaVehiculo;
import com.okiimport.app.model.enumerados.EEstatusGeneral;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.transaccion.STransaccion;

public class RegistrarMarcasViewModel extends AbstractRequerimientoViewModel {
	
	//Servicios
	@BeanInjector("sTransaccion")
	private STransaccion sTransaccion;
	
	
	//GUI
	@Wire("#winFormularioMarca")
	private Window winFormularioMarca;
	
	//Atributos
	private MarcaVehiculo marca;
	private String recordMode;
	private Boolean cerrar;
	private boolean makeAsReadOnly;
	private String valor=null;
	private Integer page_size = 10;
	
	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: formularioMarcas.zul 
	 * Retorno: Ninguno 
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("marca") MarcaVehiculo marca,
			@ExecutionArgParam("recordMode") String recordMode,
			@ExecutionArgParam("valor") String valor,
			@ExecutionArgParam("cerrar") Boolean cerrar) {
		try{
			super.doAfterCompose(view);
			this.setRecordMode((recordMode == null) ? "EDIT" : recordMode);
			this.marca = (marca==null) ? new MarcaVehiculo() :  marca;
			this.cerrar = (cerrar==null) ? true : cerrar;
			this.valor=valor;
			makeAsReadOnly = (recordMode != null && recordMode.equalsIgnoreCase("READ"))? true : false; 
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//limpiar();
		
	}
	
	/**
	 * Descripcion: Permite registrar una marca en el sistema
	 * Parametros: @param view: formularioMarcas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange({ "marca" })
	public void registrar(@BindingParam("btnEnviar") Button btnEnviar,
			@BindingParam("btnLimpiar") Button btnLimpiar,
			@BindingParam("edicion") String valor) {
		
		try{
			
				if (checkIsFormValid()) {
				
				btnEnviar.setDisabled(true);
				btnLimpiar.setDisabled(true);
				
				marca.setEstatus(EEstatusGeneral.ACTIVO);
				marca = sMaestros.registrarMarca(marca);
				

				Map<String, Object> model = new HashMap<String, Object>();
				model.put("nombre", marca.getNombre());
				System.out.println("valor: "+valor);
				this.valor=valor;
				if(this.valor!=null)
					mostrarMensaje("Informaci\u00F3n", "Marca Actualizada con Exito", null, null, null, null);
				else mostrarMensaje("Informaci\u00F3n", "Marca Registrada con Exito", null, null, null, null);
				this.setValor(null);
				this.recargar();
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Descripcion: Permite limpiar los campos del formulario Marca
	 * Parametros: @param view: formularioMarcas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange({ "marca" })
	public void limpiar() {
		marca = new MarcaVehiculo();
		super.cleanConstraintForm();
	}
	
	/**
	 * Descripcion: Permite recargar la pantalla al cerrar
	 * Parametros: @param view: formularioMarcas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	public void recargar() {
		winFormularioMarca.onClose();
		ejecutarGlobalCommand("cambiarMarcas", null);
	}

	/**METODOS PROPIOS DE LA CLASE*/
	
	/**METODOS SETTERS AND GETTERS */
	public STransaccion getsTransaccion() {
		return sTransaccion;
	}

	public void setsTransaccion(STransaccion sTransaccion) {
		this.sTransaccion = sTransaccion;
	}

	public MarcaVehiculo getMarca() {
		return marca;
	}

	public void setMarca(MarcaVehiculo marca) {
		this.marca = marca;
	}

	public String getRecordMode() {
		return recordMode;
	}

	public void setRecordMode(String recordMode) {
		this.recordMode = recordMode;
	}

	public Boolean getCerrar() {
		return cerrar;
	}

	public void setCerrar(Boolean cerrar) {
		this.cerrar = cerrar;
	}

	public boolean isMakeAsReadOnly() {
		return makeAsReadOnly;
	}

	public void setMakeAsReadOnly(boolean makeAsReadOnly) {
		this.makeAsReadOnly = makeAsReadOnly;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	

}
