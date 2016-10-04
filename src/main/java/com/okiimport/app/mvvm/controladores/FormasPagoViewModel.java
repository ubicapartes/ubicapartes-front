package com.okiimport.app.mvvm.controladores;

import java.util.ArrayList;
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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.okiimport.app.model.Compra;
import com.okiimport.app.model.FormaPago;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.constraint.CustomConstraint;
import com.okiimport.app.mvvm.model.ModeloCombo;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.maestros.SMaestros;
import com.okiimport.app.service.transaccion.STransaccion;

public class FormasPagoViewModel extends AbstractRequerimientoViewModel{
	
	//Servicios
	@BeanInjector("sTransaccion")
	private STransaccion sTransaccion;
		
	@BeanInjector("sMaestros")
	private SMaestros sMaestros;
		
	//GUI
	@Wire
	private Combobox cmbFormaPago;
	
	@Wire("#winPagoFactura")
	private Window winPagoFactura;
	
	//Atributos
	private List<ModeloCombo<Boolean>> listaFormaPagoAux;
	private List<FormaPago> listaFormaPago;
	private ModeloCombo<Boolean> formaPago;
	private Compra compra;
	
	private CustomConstraint constraintCampoObligatorio;
	private CustomConstraint constraintMensaje;
	private boolean cerrar = false;
	
	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: verDetalleOferta.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@NotifyChange({"totalFactura"})
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("compra") Compra compra){
		super.doAfterCompose(view);
		this.compra = compra;
		llenarFormasNoParametrizadas();

	}
	
	@Listen("onSelect = #cmbFormaPago")
	public void actualizarView(){
		if(formaPago.getNombre().equals("Efectivo"))
			abrirInterfazPagoEfectivo();
		else if (formaPago.getNombre().equals("Debito") || formaPago.getNombre().equals("Credito"))
			abrirInterfazPagoTDC();
	}
	
	@Command
	public void seleccionarFormaPago(){	
		if(formaPago.getNombre().equals("Efectivo"))
			abrirInterfazPagoEfectivo();
		else if (formaPago.getNombre().equals("Debito") || formaPago.getNombre().equals("Credito"))
			abrirInterfazPagoTDC();
	}
	
	@Command
	public void abrirInterfazPagoEfectivo(){
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("compra", compra);
		crearModal(BasePackageSistemaFunc+"/pago/formularioPagoEfectivo.zul", parametros);	
	}
	
	//Para TDC o Debito
	@Command
	public void abrirInterfazPagoTDC(){
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("compra", compra);
		crearModal(BasePackageSistemaFunc+"/pago/formularioFormaPago2.zul", parametros);	
	}
	
	/**
	 * Descripcion: Evento que se ejecuta al cerrar la ventana 
	 * Parametros: Ninguno
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@Override
	public void closeModal(){
		if(!cerrar){
			super.mostrarMensaje("Informaci\u00F3n", "Si cierra la ventana el proceso realizado se perdera, ¿Desea continuar?", null, 
					new Messagebox.Button[]{Messagebox.Button.YES, Messagebox.Button.NO}, new EventListener<Event>(){
				@Override
				public void onEvent(Event event) throws Exception {
					Messagebox.Button button = (Messagebox.Button) event.getData();
					if (button == Messagebox.Button.YES) {
						cerrarVentana();
					}
				}
			}, null);
		}
		else {
			super.closeModal();
		}
	}
	
	/*@SuppressWarnings("unchecked")
	@NotifyChange({"listaFormaPago"})
	private void llenarFormaPago(){
		listaFormaPago = new ArrayList((List<FormaPago>) sMaestros.consultarFormasPago(0, -1).get("formasPago"));
	}*/
	
	private void llenarFormasNoParametrizadas(){
		listaFormaPagoAux = new ArrayList<ModeloCombo<Boolean>>();
		listaFormaPagoAux.add(new ModeloCombo<Boolean>("Efectivo", false));
		listaFormaPagoAux.add(new ModeloCombo<Boolean>("Credito", true));
		listaFormaPagoAux.add(new ModeloCombo<Boolean>("Debito", false));
	}
	
	@GlobalCommand
	public void cerrarModalFormasPago(){
		winPagoFactura.onClose();
	}
	
	/**
	 * Descripcion: metodo que actualiza la variable cerrar y llama al comman respectivo al cerrar la ventana.
	 * Parametros: Ninguno
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	private void cerrarVentana(){
		cerrar = true;
		closeModal();
	}

/**GETTERS Y SETTERS*/
	
	public STransaccion getsTransaccion() {
		return sTransaccion;
	}

	public SMaestros getsMaestros() {
		return sMaestros;
	}

	public void setsMaestros(SMaestros sMaestros) {
		this.sMaestros = sMaestros;
	}

	public void setsTransaccion(STransaccion sTransaccion) {
		this.sTransaccion = sTransaccion;
	}

	public CustomConstraint getConstraintMensaje() {
		return constraintMensaje;
	}

	public void setConstraintMensaje(CustomConstraint constraintMensaje) {
		this.constraintMensaje = constraintMensaje;
	}
	
	public CustomConstraint getConstraintCampoObligatorio() {
		return constraintCampoObligatorio;
	}

	public void setConstraintCampoObligatorio(CustomConstraint constraintCampoObligatorio) {
		this.constraintCampoObligatorio = constraintCampoObligatorio;
	}

	public List<ModeloCombo<Boolean>> getListaFormaPagoAux() {
		return listaFormaPagoAux;
	}

	public void setListaFormaPagoAux(List<ModeloCombo<Boolean>> listaFormaPagoAux) {
		this.listaFormaPagoAux = listaFormaPagoAux;
	}

	public List<FormaPago> getListaFormaPago() {
		return listaFormaPago;
	}

	public void setListaFormaPago(List<FormaPago> listaFormaPago) {
		this.listaFormaPago = listaFormaPago;
	}

	public ModeloCombo<Boolean> getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(ModeloCombo<Boolean> formaPago) {
		this.formaPago = formaPago;
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}
	
	
}
