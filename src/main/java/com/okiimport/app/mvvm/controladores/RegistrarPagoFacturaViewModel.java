package com.okiimport.app.mvvm.controladores;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.braintreegateway.BraintreeGateway;
import com.okiimport.app.model.FormaPago;
import com.okiimport.app.model.Pago;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.constraint.CustomConstraint;
import com.okiimport.app.mvvm.constraint.braintree.CVVConstraint;
import com.okiimport.app.mvvm.model.ModeloCombo;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.maestros.SMaestros;
import com.okiimport.app.service.transaccion.STransaccion;

public class RegistrarPagoFacturaViewModel extends AbstractRequerimientoViewModel{

	//Servicios
	@BeanInjector("sTransaccion")
	private STransaccion sTransaccion;
	
	@BeanInjector("sMaestros")
	private SMaestros sMaestros;

	@BeanInjector("gateway")
	private BraintreeGateway gateway;

	//GUI
	@Wire("#winPagoFactura")
	private Window winPagoFactura;	
	@Wire
	private Textbox txtTarjeta;
	@Wire
	private Textbox txtCodigo;
	@Wire
	private Textbox txtMesVence;
	@Wire
	private Textbox txtAnoVence;
	@Wire
	private Textbox txtTitular;
	@Wire
	private Combobox cmbTipoTarjeta;
	@Wire
	private Combobox cmbFormaPago;
	

	//Atributos
	private List<ModeloCombo<Boolean>> listaTipoDocumentos;
	private List<ModeloCombo<Boolean>> listaTipoTarjeta;
	private List<ModeloCombo<Boolean>> listaFormaPagoAux;
	private List<FormaPago> listaFormaPago;
	private ModeloCombo<Boolean> tipoDocumento;
	private ModeloCombo<Boolean> tipoTarjeta;
	private ModeloCombo<Boolean> formaPago;
	
	
	private CustomConstraint constraintCampoObligatorio;
	
	private Pago pago;
	
	private boolean cerrar = false;

	private CustomConstraint constraintMensaje;

	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: verDetalleOferta.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@NotifyChange({"totalFactura"})
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("pago")  Pago pago){
		super.doAfterCompose(view);
		this.pago = pago;
		llenarTiposTarjetas();
		llenarFormaPago();

		//Braintree
		String clientToken = gateway.clientToken().generate();		
		Clients.evalJavaScript("loadForm('"+clientToken+"');");

	}

	/**COMMAND*/
	@Command
	public void registrarPago(@BindingParam("btnEnviar") Button button){
		if(checkIsFormValid()){
			if(validacionesParaGuardar() == true){
				Clients.showBusy("Espere un momento...."); //Se puede cambiar el mensaje
				Clients.evalJavaScript("tokenizeCard("+buildCreditCardParameterJS()+");");
			}
		}
	}
	
	@Command
	public void seleccionarTipoTarjeta(){	
		txtCodigo.setPlaceholder("");
		if(!tipoTarjeta.getValor()){
			txtCodigo.setMaxlength(4);
			txtCodigo.setPlaceholder("1234");
		}else{
			txtCodigo.setMaxlength(3);
			txtCodigo.setPlaceholder("123");
		}			
	}
	
	public boolean validacionesParaGuardar(){
		boolean guardar = false;
		if(txtTarjeta.getValue() != "" && txtTitular.getValue() != "" && 
				txtMesVence.getValue() != "" && txtCodigo.getValue() != "" &&
				txtAnoVence.getValue() != ""){
			if(txtTarjeta.getValue().length() == 16){
				if(txtCodigo.getValue().length() == 3 || txtCodigo.getValue().length() == 4){
					if(txtAnoVence.getValue().length() == 4){
						if(txtMesVence.getValue().length() == 2){
				
							Calendar fecha = new GregorianCalendar();
							if(Integer.parseInt(txtAnoVence.getValue()) >= fecha.get(Calendar.YEAR))
								guardar = true;
							else
								mostrarMensaje("Error", "El año de vencimiento debe ser igual o mayor al año actual: "+"("+fecha.get(Calendar.YEAR)+")", null, null, null, null);
						}else
							mostrarMensaje("Error", "¡El mes de vencimiento debe contener 2 digitos!", null, null, null, null);   
					
					}else
						mostrarMensaje("Error", "¡El año de vencimiento debe contener 4 digitos!", null, null, null, null);   
						
				}else
					mostrarMensaje("Error", "¡Debe ingresar el CVV completo!", null, null, null, null);
			}else
				mostrarMensaje("Error", "¡Debe ingresar los 16 digitos de la Tarjeta!", null, null, null, null);
				
		}else{
			mostrarMensaje("Error", "¡Debe ingresar todos los campos!", null, null, null, null);
			
		}
		return guardar;
	}

	@Command
	@SuppressWarnings("unchecked")
	public void onPaymentSuccess(@ContextParam(ContextType.TRIGGER_EVENT) Event event){
		Clients.clearBusy();
		
		Map<String, Object> data = (Map<String, Object>) event.getData();
		String payment_method_nonce = (String) data.get("payment_method_nonce");
		this.pago.setPaymentMethodNonce(payment_method_nonce);
		System.out.println("En el Server payment_method_nonce: "+payment_method_nonce);
		
		winPagoFactura.detach();
		// ACA GUARDAR EL PAGO
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("pago", this.pago);
		parametros.put("gateway", gateway);
		this.ejecutarGlobalCommand("registrarPago", parametros);
		//sTransaccion.registrarPagoFactura(llenarPago());
		//sTransaccion.guardarOrdenCompra(compra, sControlConfiguracion);
	}

	@Command
	public void onPaymentError(@ContextParam(ContextType.TRIGGER_EVENT) Event event){
		Clients.clearBusy();
		mostrarMensaje("Error", "Error en Transaccion", null, null, null, null);
	}

	/**
	 * Descripcion: Permite limpiar los campos del formulario 
	 * Parametros: @param view: formularioSolicituddePedido.zul 
	 * Retorno: Ninguno 
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange({"tipoDocumento", "tipoTarjeta"})
	public void limpiar(){
		this.tipoDocumento = listaTipoDocumentos.get(0);
		this.tipoTarjeta = listaTipoTarjeta.get(0);
		this.txtAnoVence.setValue("");
		this.txtCodigo.setValue("");
		this.txtMesVence.setValue("");
		this.txtTarjeta.setValue("");
		this.txtTitular.setValue("");
		super.cleanConstraintForm();
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
			super.mostrarMensaje("Informaci\u00F3n", "Si cierra la ventana el proceso realizado se perdera, �Desea continuar?", null, 
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

	@SuppressWarnings("unchecked")
	@NotifyChange({"listaFormaPago"})
	private void llenarFormaPago(){
		listaFormaPago = new ArrayList((List<FormaPago>) sMaestros.consultarFormasPago(0, -1).get("formasPago"));
	}
	
	private void llenarTiposTarjetas(){
		listaTipoTarjeta = new ArrayList<ModeloCombo<Boolean>>();
		listaTipoTarjeta.add(new ModeloCombo<Boolean>("American Express", false));// cvv de 4 digitos
		listaTipoTarjeta.add(new ModeloCombo<Boolean>("MasterCard", true));
		listaTipoTarjeta.add(new ModeloCombo<Boolean>("Visa", true));
		listaTipoTarjeta.add(new ModeloCombo<Boolean>("Maestro", true));
		listaTipoTarjeta.add(new ModeloCombo<Boolean>("Otra", true));
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

	private String buildCreditCardParameterJS(){
		StringBuilder builder = new StringBuilder("");
		builder.append("'").append(txtTitular.getValue()).append("', ")
		.append("'").append(txtTarjeta.getValue()).append("', ")
		.append("'").append(txtMesVence.getValue()).append("', ")
		.append("'").append(txtAnoVence.getValue()).append("', ")
		.append("'").append(txtCodigo.getValue()).append("'");
		return builder.toString();
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

	public BraintreeGateway getGateway() {
		return gateway;
	}


	public void setGateway(BraintreeGateway gateway) {
		this.gateway = gateway;
	}


	public List<ModeloCombo<Boolean>> getListaTipoDocumentos() {
		return listaTipoDocumentos;
	}

	public void setListaTipoDocumentos(List<ModeloCombo<Boolean>> listaTipoDocumentos) {
		this.listaTipoDocumentos = listaTipoDocumentos;
	}

	public ModeloCombo<Boolean> getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(ModeloCombo<Boolean> tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
	public ModeloCombo<Boolean> getTipoTarjeta() {
		return tipoTarjeta;
	}

	public void setTipoTarjeta(ModeloCombo<Boolean> tipoTarjeta) {
		this.tipoTarjeta = tipoTarjeta;
	}
	
	public Pago getPago() {
		return pago;
	}
	
	public void setPago(Pago pago) {
		this.pago = pago;
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

	public List<ModeloCombo<Boolean>> getListaTipoTarjeta() {
		return listaTipoTarjeta;
	}

	public void setListaTipoTarjeta(List<ModeloCombo<Boolean>> listaTipoTarjeta) {
		this.listaTipoTarjeta = listaTipoTarjeta;
	}
	
	public CustomConstraint getCvvConstraint(){
		return new CVVConstraint();
	}
	
}
