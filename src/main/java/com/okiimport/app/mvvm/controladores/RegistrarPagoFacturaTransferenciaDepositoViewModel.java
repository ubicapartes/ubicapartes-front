package com.okiimport.app.mvvm.controladores;

import java.util.Date;

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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.okiimport.app.model.Compra;
import com.okiimport.app.model.Pago;
import com.okiimport.app.model.PagoCliente;
import com.okiimport.app.model.enumerados.EEstatusCompra;
import com.okiimport.app.model.enumerados.EEstatusGeneral;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.constraint.CustomConstraint;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.maestros.SMaestros;
import com.okiimport.app.service.transaccion.STransaccion;
import com.okiimport.app.service.web.SPago;

public class RegistrarPagoFacturaTransferenciaDepositoViewModel extends AbstractRequerimientoViewModel{
	
	//Servicios
	@BeanInjector("sPago")
	private SPago sPago;
		
	@BeanInjector("sMaestros")
	private SMaestros sMaestros;
	
	@BeanInjector("sTransaccion")
	private STransaccion sTransaccion;
		
	//GUI
	@Wire("#winPagoFacturaTD")
	private Window winPagoFactura;	
	@Wire
	private Textbox txtTitular;
	@Wire
	private Datebox dateFechaPago;
	@Wire
	private Textbox txtObservaciones;
		
	private CustomConstraint constraintCampoObligatorio;
	private Pago pago;
	private Compra compra;
	private Float montoPagar;
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
			@ExecutionArgParam("pago")  Pago pago,
			@ExecutionArgParam("compra") Compra compra){
		super.doAfterCompose(view);
		this.pago = pago;
		this.compra = compra;
		setMontoPagar(this.compra.getPrecioVenta());
		String cliente = this.compra.getRequerimiento().getCliente().getNombre().concat(" ".concat(this.compra.getRequerimiento().getCliente().getApellido()));
		txtTitular.setValue(cliente);
		txtTitular.setDisabled(true);
		dateFechaPago.setValue(new Date());
	}

	/**COMMAND*/
	@Command
	public void registrarPago(@BindingParam("btnEnviar") Button button){
		if(checkIsFormValid()){
			if(validacionesParaGuardar() == true){
				Boolean exito = sPago.registrarPagoEfectivo(llenarPago());
				if(exito){
					actualizarCompra();
					mostrarMensaje("Informaci\u00F3n", "Operacion registrada exitosamente", Messagebox.INFORMATION, null, null, null);
					this.winPagoFactura.onClose();
				}
				else 
					mostrarMensaje("Error", "El Pago no pudo realizarse, intente de nuevo", Messagebox.ERROR, null, null, null);
			}
		}
	}
	
	@NotifyChange("*")
	private void actualizarCompra(){
		this.compra.setEstatus(EEstatusCompra.PAGADA);
		sTransaccion.registrarOActualizarCompra(this.compra);
		ejecutarGlobalCommand("refrescarListadoCompras", null);
		ejecutarGlobalCommand("cerrarModalFormasPago", null);

	}
	
	private PagoCliente llenarPago() {
		PagoCliente pago = new PagoCliente();
		pago.setFechaPago(new Date());
		pago.setMonto(this.montoPagar);
		pago.setEstatus(EEstatusGeneral.ACTIVO.name());
		pago.setDescripcion(txtObservaciones.getValue());
		pago.setCompra(this.compra);
		//pago.setFormaPago(formaPago); CORREGIR
		return pago;
	}

	public boolean validacionesParaGuardar(){
		boolean guardar = false;
		if(this.montoPagar != 0 && txtTitular.getValue() != ""){
			if(this.montoPagar > 0)
				guardar = true;
			else
				mostrarMensaje("Error", "El monto total a pagar debe ser mayor a cero", null, null, null, null);
		}else{
			mostrarMensaje("Error", "Debe ingresar todos los campos", null, null, null, null);
		}
		return guardar;
	}
	
	@Command
	public void limpiar(){
		this.txtTitular.setValue("");
		this.txtObservaciones.setValue("");
		this.montoPagar=(float) 0;
		super.cleanConstraintForm();
	}
	
	/**GETTERS Y SETTERS*/

	public SMaestros getsMaestros() {
		return sMaestros;
	}

	public SPago getsPago() {
		return sPago;
	}

	public void setsPago(SPago sPago) {
		this.sPago = sPago;
	}

	public void setsMaestros(SMaestros sMaestros) {
		this.sMaestros = sMaestros;
	}

	public CustomConstraint getConstraintCampoObligatorio() {
		return constraintCampoObligatorio;
	}

	public void setConstraintCampoObligatorio(CustomConstraint constraintCampoObligatorio) {
		this.constraintCampoObligatorio = constraintCampoObligatorio;
	}

	public Pago getPago() {
		return pago;
	}

	public void setPago(Pago pago) {
		this.pago = pago;
	}

	public boolean isCerrar() {
		return cerrar;
	}

	public void setCerrar(boolean cerrar) {
		this.cerrar = cerrar;
	}

	public CustomConstraint getConstraintMensaje() {
		return constraintMensaje;
	}

	public void setConstraintMensaje(CustomConstraint constraintMensaje) {
		this.constraintMensaje = constraintMensaje;
	}

	public Float getMontoPagar() {
		return montoPagar;
	}

	public void setMontoPagar(Float montoPagar) {
		this.montoPagar = montoPagar;
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public STransaccion getsTransaccion() {
		return sTransaccion;
	}

	public void setsTransaccion(STransaccion sTransaccion) {
		this.sTransaccion = sTransaccion;
	}
	
}
