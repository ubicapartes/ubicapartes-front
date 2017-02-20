package com.okiimport.app.mvvm.controladores.maestros;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.zkoss.zk.ui.event.SortEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Messagebox.ClickEvent;

import com.okiimport.app.model.Banco;
import com.okiimport.app.model.Cuenta;
import com.okiimport.app.model.FormaPago;
import com.okiimport.app.model.Proveedor;
import com.okiimport.app.model.Usuario;
import com.okiimport.app.model.enumerados.EEstatusGeneral;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.transaccion.STransaccion;

public class RegistrarCuentasViewModel extends AbstractRequerimientoViewModel {
	
	//Servicios
	@BeanInjector("sTransaccion")
	private STransaccion sTransaccion;
	
	
	//GUI
	@Wire("#winFormularioCuenta")
	private Window winFormularioCuenta;

	
	//Atributos
	private Cuenta cuenta;
	private String recordMode;
	private Boolean cerrar;
	private boolean makeAsReadOnly;
	private String valor=null;
	private String fromList=null;
	private String tipoCuenta=null;
	private Integer page_size = 10;
	private List<Banco> listaBancos;
	private List<String> listaTiposCuenta;
	private Usuario usuario;


	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: formularioCuentas.zul 
	 * Retorno: Ninguno 
	 * Nota: Ninguna
	 * */
	@SuppressWarnings("unchecked")
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("cuenta") Cuenta cuenta,
			@ExecutionArgParam("recordMode") String recordMode,
			@ExecutionArgParam("valor") String valor,
			@ExecutionArgParam("cerrar") Boolean cerrar, @ExecutionArgParam("fromList") String fromList) {
		try{
			super.doAfterCompose(view);
			this.setRecordMode((recordMode == null) ? "EDIT" : recordMode);
			llenarCombos();
			this.fromList = fromList;
			this.cuenta = (cuenta==null) ? new Cuenta() :  cuenta;
			this.cerrar = (cerrar==null) ? true : cerrar;
			tipoCuenta= (cuenta==null? "CORRIENTE": cuenta.getTipo());
			this.valor=valor;
			makeAsReadOnly = (recordMode != null && recordMode.equalsIgnoreCase("READ"))? true : false;
			cargarDatosCuenta(cuenta);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Descripcion: Permite registrar una cuenta en el sistema
	 * Parametros: @param view: formularioCuentas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange({ "cuenta" })
	public void registrar(@BindingParam("btnEnviar") Button btnEnviar,
			@BindingParam("btnLimpiar") Button btnLimpiar,
			@BindingParam("edicion") String valor) {
		
		try{
			
				if (checkIsFormValid()) {
				
				btnEnviar.setDisabled(true);
				btnLimpiar.setDisabled(true);
				usuario = super.getUsuario();
				if(usuario.getPersona().getTipoMenu()!=null && !usuario.getPersona().getTipoMenu().toString().isEmpty() && usuario.getPersona().getTipoMenu()==3){
					cuenta.setProveedor(new Proveedor());
					cuenta.getProveedor().setId(usuario.getPersona().getId());
				}
				cuenta.setEstatus(EEstatusGeneral.ACTIVO);
				cuenta.setTipo(tipoCuenta);
				cuenta = sMaestros.registrarCuenta(cuenta);
				recargar();
				this.valor=valor;
				if(this.valor!=null)
					mostrarMensaje("Informaci\u00F3n", "Cuenta Actualizada con Exito", null, null, null, null);
				else mostrarMensaje("Informaci\u00F3n", "Cuenta Registrada con Exito", null, null, null, null);
				this.setValor(null);
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Descripcion: Permite limpiar los campos del formulario Cuenta
	 * Parametros: @param view: formularioCuentas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange({ "cuenta" })
	public void limpiar() {
		recargar();
	}
	
	@SuppressWarnings("unchecked")
	public void llenarCombos(){
		this.listaTiposCuenta = new ArrayList<String>();
		this.listaTiposCuenta.add("CORRIENTE");
		this.listaTiposCuenta.add("AHORRO");
		listaBancos = (List<Banco>) sMaestros.consultarBancos(0, -1).get("bancos");
	}
	
	/**
	 * Descripcion: Permite recargar la pantalla al cerrar
	 * Parametros: @param view: formularioCuentas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	public void recargar() {
		winFormularioCuenta.onClose();
		
		if(fromList.equalsIgnoreCase("Ubicapartes"))
			ejecutarGlobalCommand("cambiarCuentasUbicapartes", null);
		else
			ejecutarGlobalCommand("cambiarCuentas", null);
			
	}
	
	public void cargarDatosCuenta(Cuenta cuenta){
		if(cuenta!=null && cuenta.getBanco()!=null){
			this.cuenta.setBanco(cuenta.getBanco());
		} else {
			this.cuenta.setBanco(listaBancos.get(0));
		}
		
		if(cuenta!=null && cuenta.getTipo()!=null && !cuenta.getTipo().isEmpty()){
			this.tipoCuenta = cuenta.getTipo();
		}
	}

	/**METODOS PROPIOS DE LA CLASE*/
	
	/**METODOS SETTERS AND GETTERS */
	public STransaccion getsTransaccion() {
		return sTransaccion;
	}

	public void setsTransaccion(STransaccion sTransaccion) {
		this.sTransaccion = sTransaccion;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
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
	
	public List<Banco> getListaBancos() {
		return listaBancos;
	}

	public void setBancos(List<Banco> listaBancos) {
		this.listaBancos = listaBancos;
	}
	
	public List<String> getListaTiposCuenta() {
		return listaTiposCuenta;
	}

	public void setListaTiposCuenta(List<String> listaTiposCuenta) {
		this.listaTiposCuenta = listaTiposCuenta;
	}
	
	public String getTipoCuenta() {
		return tipoCuenta;
	}

	public void setTipoCuenta(String tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public String getFromList() {
		return fromList;
	}

	public void setFromList(String fromList) {
		this.fromList = fromList;
	}

}
