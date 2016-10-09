package com.okiimport.app.mvvm.controladores;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.okiimport.app.model.Cliente;
import com.okiimport.app.model.Banco;
import com.okiimport.app.model.Compra;
import com.okiimport.app.model.FormaPago;
import com.okiimport.app.model.Pago;
import com.okiimport.app.model.PagoCliente;
import com.okiimport.app.model.Usuario;
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
	private Combobox cmbFormaDePago;
	@Wire
	private Textbox txtNroReferencia;
	@Wire
	private Combobox cmbBanco;
	@Wire
	private Combobox cmbEstatusPago;
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
	private List<Banco> listaBancos;
	private List<FormaPago> listaFormaPagos;
	private List<String> listaEstatusPago;
	private Banco bancoSeleccionado;
	private FormaPago formaPagoSeleccionado;
    private Cliente objCliente;
    private PagoCliente pagoCliente;
    private String estatusPago;



	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: verDetalleOferta.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@SuppressWarnings("unchecked")
	@NotifyChange({"totalFactura"})
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("pago")  Pago pago,
			@ExecutionArgParam("compra") Compra compra){
		super.doAfterCompose(view);
		this.listaEstatusPago = new ArrayList<String>();
		this.listaEstatusPago.add("EN ESPERA");
		this.listaEstatusPago.add("APROBADO");
		this.listaEstatusPago.add("RECHAZADO");
		estatusPago="EN ESPERA";
		Map<String, Object> parametros = sMaestros.consultarBancos(0, pageSize);
		this.listaBancos = (List<Banco>) parametros.get("bancos");
		parametros = sMaestros.consultarFormasPago(0, pageSize);
		this.listaFormaPagos = (List<FormaPago>) parametros.get("formasPago");
		setBancoSeleccionado(this.listaBancos.get(0));
		setFormaPagoSeleccionado(this.listaFormaPagos.get(0));
		this.pago = pago;
		this.compra = compra;
		setMontoPagar(this.compra.getPrecioVenta());
		UserDetails user = super.getUser();
		if(user!=null){
			Usuario usuario = sControlUsuario.consultarUsuario(user.getUsername(), user.getPassword(), null);
			objCliente = sMaestros.consultarClienteByPersonaId(usuario.getPersona().getId());
			if(objCliente!=null){
				habDesCampos(1);
			} else {
				habDesCampos(2);
			}
		}
		String cliente = this.compra.getRequerimiento().getCliente().getNombre().concat(" ".concat(this.compra.getRequerimiento().getCliente().getApellido()));
		txtTitular.setValue(cliente);
		txtTitular.setDisabled(true);
		dateFechaPago.setValue(tienePagoAsignado()?pagoCliente.getFechaPago():new Date());
	}
	
	

	/**COMMAND*/
	@Command
	public void registrarPago(@BindingParam("btnEnviar") Button button){
		if(checkIsFormValid()){
			if(validacionesParaGuardar() == true){
				Boolean exito = sPago.registrarPago(llenarPago());
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
		sTransaccion.registrarOActualizarCompra(this.compra);
		ejecutarGlobalCommand("refrescarListadoCompras", null);
		ejecutarGlobalCommand("cerrarModalFormasPago", null);

	}
	
	private PagoCliente llenarPago() {
		if(objCliente!=null && !tienePagoAsignado()){
			System.out.println("Cliente sin pago");
			pagoCliente = new PagoCliente();
			pagoCliente.setFechaPago(new Date());
			pagoCliente.setMonto(this.montoPagar);
			pagoCliente.setCompra(this.compra);
			pagoCliente.setTransactionId(txtNroReferencia.getValue());
			pagoCliente.setFormaPago(formaPagoSeleccionado);
			pagoCliente.setBanco(bancoSeleccionado);		
		} else if(objCliente!=null && tienePagoAsignado()){
			System.out.println("Cliente con pago");
			pagoCliente.setTransactionId(txtNroReferencia.getValue());
			pagoCliente.setFormaPago(formaPagoSeleccionado);
			pagoCliente.setBanco(bancoSeleccionado);
		} else{
			System.out.println("Analista");
			Map<String, Object> parametros = sPago.consultarPagoByCompra(this.compra);
			pagoCliente = (PagoCliente) parametros.get("pago");
			if(estatusPago=="APROBADO"){
				this.compra.setEstatus(EEstatusCompra.PAGADA);
			} else if(estatusPago=="RECHAZADO"){
				this.compra.setEstatus(EEstatusCompra.RECHAZADA);
			} 
		}
		
		pagoCliente.setEstatus(EEstatusGeneral.ACTIVO.name());
		pagoCliente.setDescripcion(txtObservaciones.getValue());
		return pagoCliente;
	}
	
	private boolean tienePagoAsignado(){
		Map<String, Object> parametros = sPago.consultarPagoByCompra(this.compra);
		pagoCliente = (PagoCliente) parametros.get("pago");
		if(pagoCliente!=null){
			bancoSeleccionado = pagoCliente.getBanco();
			formaPagoSeleccionado =  pagoCliente.getFormaPago();
			txtNroReferencia.setValue(pagoCliente.getTransactionId());
		}
		if(this.compra.getEstatus().equals(EEstatusCompra.RECHAZADA) || this.compra.getEstatus().equals(EEstatusCompra.PAGADA)){
			estatusPago = (this.compra.getEstatus().equals(EEstatusCompra.RECHAZADA)? "RECHAZADO":"APROBADO");
		}
		return (pagoCliente==null?false:true);
	}

	public boolean validacionesParaGuardar(){
		boolean guardar = false;
		boolean continuar = true;
		
		if(continuar && this.montoPagar>0){
			guardar = true;
			continuar = true;
		}else{
			mostrarMensaje("Error", "El monto total a pagar debe ser mayor a cero", null, null, null, null);
			guardar = false;
			continuar = false;
			return false;
		}
		
		if(continuar && (txtTitular.getValue() == ""|| txtTitular.getValue()==null)){
			mostrarMensaje("Error", "Debe ingresar todos los campos", null, null, null, null);
			guardar = false;
			continuar = false;
			return false;
		}
		
		if(continuar && (formaPagoSeleccionado.getNombre() != "Efectivo" && (txtNroReferencia.getValue()=="" || txtNroReferencia.getValue()==null || bancoSeleccionado.getNombre() == "" || bancoSeleccionado.getNombre() == null))){
			guardar = false;
			continuar = false;
			mostrarMensaje("Error", "Debe ingresar todos los campos", null, null, null, null);
			return false;
		}
		
		return guardar;
	}
	
	public void habDesCampos(int tipo){
		//tipo = 1 cliente, 2 admin, 3 analista, 4 proveedor
		if(tipo==1){
			txtTitular.setDisabled(true);
			cmbFormaDePago.setDisabled(false);
			txtNroReferencia.setDisabled(false);
			cmbBanco.setDisabled(false);
			dateFechaPago.setDisabled(true);
			txtObservaciones.setDisabled(false);
			cmbEstatusPago.setDisabled(true);
		} else {
			txtTitular.setDisabled(true);
			cmbFormaDePago.setDisabled(true);
			txtNroReferencia.setDisabled(true);
			cmbBanco.setDisabled(true);
			dateFechaPago.setDisabled(true);
			txtObservaciones.setDisabled(false);
			cmbEstatusPago.setDisabled(false);
		}
		
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
	
	public List<Banco> getListaBancos() {
		return listaBancos;
	}

	public void setListaBancos(List<Banco> listaBancos) {
		this.listaBancos = listaBancos;
	}
	
	public List<FormaPago> getListaFormaPagos() {
		return listaFormaPagos;
	}

	public void setListaFormaPagos(List<FormaPago> listaFormaPagos) {
		this.listaFormaPagos = listaFormaPagos;
	}
	
	public Banco getBancoSeleccionado() {
		return bancoSeleccionado;
	}


	public void setBancoSeleccionado(Banco bancoSeleccionado) {
		this.bancoSeleccionado = bancoSeleccionado;
	}


	public FormaPago getFormaPagoSeleccionado() {
		return formaPagoSeleccionado;
	}


	public void setFormaPagoSeleccionado(FormaPago formaPagoSeleccionado) {
		this.formaPagoSeleccionado = formaPagoSeleccionado;
	}
	
	public List<String> getListaEstatusPago() {
		return listaEstatusPago;
	}

	public void setListaEstatusPago(List<String> listaEstatusPago) {
		this.listaEstatusPago = listaEstatusPago;
	}
	
	public String getEstatusPago() {
		return estatusPago;
	}

	public void setEstatusPago(String estatusPago) {
		this.estatusPago = estatusPago;
	}
	
}
