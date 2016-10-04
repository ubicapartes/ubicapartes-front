package com.okiimport.app.mvvm.controladores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Default;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;

import com.okiimport.app.model.DetalleRequerimiento;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.transaccion.STransaccion;

public class EnviarRequerimientoProvViewModel extends AbstractRequerimientoViewModel {
	
	//Servicios
	@BeanInjector("sTransaccion")
	private STransaccion sTransaccion;
	
	//GUI
	
	//Atributos
	private List <DetalleRequerimiento> listaDetalleRequerimientoSeleccionados;
	private Requerimiento requerimiento;
	
	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: enviarRequerimientoProv.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view, 
			@ExecutionArgParam("requerimiento") Requerimiento requerimiento){
		super.doAfterCompose(view);
		this.requerimiento = requerimiento;
	}
	
	/**INTERFACE*/
	
	/**GLOBAL COMMAND*/
	/**
	 * Descripcion: Llama a remover los items seleccionados
	 * Parametros: @param view: enviarRequerimientoProv.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@GlobalCommand
	@NotifyChange("listaDetalleRequerimientoSeleccionados")
	public void removerSeleccionados(){
		if(listaDetalleRequerimientoSeleccionados!=null)
			listaDetalleRequerimientoSeleccionados.clear();
	}
	
	/**COMMAND*/
	/**
	 * Descripcion: Permitira enviar la solicitud de cotizacion al proveedor 
	 * Parametros: requerimiento @param view: enviarRequerimientoProv.zul
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void enviarSolicitudProv(@Default("false") @BindingParam("enviar") Boolean enviar){
		if(listaDetalleRequerimientoSeleccionados!= null && this.listaDetalleRequerimientoSeleccionados.size()>0){
			if (validarListaClasificacion()==true){
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("enviar", enviar);
				parametros.put("requerimiento", this.requerimiento);
				parametros.put("repuestosseleccionados", listaDetalleRequerimientoSeleccionados);
				crearModal(BasePackageSistemaFunc+"emitidos/seleccionarProveedores.zul", parametros);
			}
			else
				mostrarMensaje("Informaci\u00F3n", "Algunos repuestos seleccionados no tienen una clasificaci\u00F3n asignada", //cambiar mensaje
						null, null, null, null);
		} 
		else
			mostrarMensaje("Informaci\u00F3n", "Seleccione al menos un Repuesto", null, null, null, null);
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	/**
	 * Descripcion: Permite validar al proveedor segun la clasificacion del repuesto
	 * Parametros: Ninguno
	 * Retorno: @return valor boolean que permitira verificar si todos los repuestos seleccionados ya tiene
	 * una clasificaion de repuesto asignada
	 * Nota: Ninguna
	 * */
	private boolean validarListaClasificacion(){
		if (listaDetalleRequerimientoSeleccionados!= null){
			for( DetalleRequerimiento detalleReq: listaDetalleRequerimientoSeleccionados){
				if ( detalleReq.getClasificacionRepuesto()==null )
					return false;
			}
		}

		return true;
	}
	
	/**METODOS OVERRIDE*/
	/**
	 * Descripcion: Evento que se ejecuta al cerrar la ventana y que valida si el proceso actual de la compra se perdera o no
	 * Parametros: Ninguno
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@Override
	public void closeModal(){
		super.closeModal();
		ejecutarGlobalCommand("cambiarRequerimientos", null);
	}
		
	/**SETTERS Y GETTERS*/
	public STransaccion getsTransaccion() {
		return sTransaccion;
	}

	public void setsTransaccion(STransaccion sTransaccion) {
		this.sTransaccion = sTransaccion;
	}

	public List<DetalleRequerimiento> getListaDetalleRequerimientoSeleccionados() {
		return listaDetalleRequerimientoSeleccionados;
	}

	public void setListaDetalleRequerimientoSeleccionados(
			List<DetalleRequerimiento> listaDetalleRequerimientoSeleccionados) {
		this.listaDetalleRequerimientoSeleccionados = listaDetalleRequerimientoSeleccionados;
	}

	public Requerimiento getRequerimiento() {
		return requerimiento;
	}

	public void setRequerimiento(Requerimiento requerimiento) {
		this.requerimiento = requerimiento;
	}
}
