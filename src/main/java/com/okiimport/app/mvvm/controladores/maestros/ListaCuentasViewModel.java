package com.okiimport.app.mvvm.controladores.maestros;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Default;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SortEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;

import com.okiimport.app.model.Banco;
import com.okiimport.app.model.Cuenta;
import com.okiimport.app.model.Proveedor;
import com.okiimport.app.model.Usuario;
import com.okiimport.app.model.enumerados.EEstatusGeneral;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.maestros.SMaestros;
import com.okiimport.app.service.transaccion.STransaccion;

public class ListaCuentasViewModel extends AbstractRequerimientoViewModel implements
		EventListener<SortEvent> {

	// Servicios
	@BeanInjector("sMaestros")
	private SMaestros sMaestros;
	
	@BeanInjector("sTransaccion") 
	  private STransaccion sTransaccion;

	// GUI
	@Wire("#gridCuentas")
	private Listbox gridCuentas;
	
	@Wire("#winListaMisCuentas")
	private Component winListaMisCuentas;


	@Wire("#pagCuentas")
	private Paging pagCuentas;
	
	// Atributos
	// Modelos
	private List<Cuenta> cuentas;
	private Cuenta cuentaFiltro;
	private static final int PAGE_SIZE = 10;
	private Usuario usuario;
	
	private String fromList = "Proveedores";


	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: listaCuentas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view) {
		super.doAfterCompose(view);
		cuentaFiltro = new Cuenta();
		cuentaFiltro.setBanco(new Banco());
		cuentaFiltro.setProveedor(new Proveedor());
		pagCuentas.setPageSize(PAGE_SIZE);
		agregarGridSort(gridCuentas);
		cambiarCuentas(0, null, null);
	}

	@Override
	public void onEvent(SortEvent event) throws Exception {
		// TODO Auto-generated method stub
		if(event.getTarget() instanceof Listheader){
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("fieldSort", ((Listheader) event.getTarget()).getValue().toString());
			parametros.put("sortDirection", event.isAscending());
			ejecutarGlobalCommand("cambiarCuentas", parametros );
		}
	}

	/** GLOBAL COMMAND */
	/**
	 * Descripcion: Llama a consultar la cuenta
	 * Parametros: @param view: listaCuentas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@SuppressWarnings("unchecked")
	@GlobalCommand
	@NotifyChange("cuentas")
	public void cambiarCuentas(@Default("0") @BindingParam("page") int page, @BindingParam("fieldSort") String fieldSort, @BindingParam("sortDirection") Boolean sortDirection) {
		usuario = super.getUsuario();
		
		//el usuario es proveedor
		if(usuario.getPersona().getTipoMenu()!=null && !usuario.getPersona().getTipoMenu().toString().isEmpty() && !isAdminOrAnalista()){
			cuentaFiltro.getProveedor().setId(usuario.getPersona().getId());
		} else {
			cuentaFiltro.getProveedor().setId(-1);
		}
		
		Map<String, Object> parametros = sMaestros.consultarCuentasF(cuentaFiltro,  fieldSort, sortDirection, page, pageSize);
		Integer total = (Integer) parametros.get("total");
		cuentas = (List<Cuenta>) parametros.get("cuentas");
		pagCuentas.setActivePage(page);
		pagCuentas.setTotalSize(total);
	}
	

	/** COMMAND */
	/**
	 * Descripcion: Permitira cambiar la paginacion de acuerdo a la pagina activa del Paging 
	 * Parametros: @param view: listaCuentas.zul  
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange("*")
	public void paginarLista() {
		int page = pagCuentas.getActivePage();
		cambiarCuentas(page, null, null);
	}


	/**
	 * Descripcion: Llama a un modal para crear o registrar una Cuenta
	 * Parametros: @param view: listaCuentas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void nuevaCuenta() {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("fromList", fromList);
		llamarFormulario("pago/formularioCuenta.zul", parametros);
	}

	/**
	 * Descripcion: Llama a un modal para ver los datos de la cuenta
	 * Parametros: Cuenta @param view: listaCuentas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void verCuenta(@BindingParam("cuenta") Cuenta cuenta){
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("cuenta", cuenta);
		parametros.put("recordMode", "READ");
		parametros.put("fromList", fromList);
		llamarFormulario("pago/formularioCuenta.zul", parametros);
	}
	
	/**
	 * Descripcion: Llama a un modal para editar los datos de la cuenta
	 * Parametros: Cuenta @param view: listaCuentas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void editarCuenta(@BindingParam("cuenta") Cuenta cuenta){
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("cuenta", cuenta);
			parametros.put("valor", "editar");
			parametros.put("fromList", fromList);
			llamarFormulario("pago/formularioCuenta.zul", parametros);
	}
	
	/**
	 * Descripcion: Metodo de la clase que permite llamar formularios 
	 * Parametros: @param view: listaCuentas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	private void llamarFormulario(String ruta, Map<String, Object> parametros) {
		crearModal(BasePackageSistemaFunc+ruta, parametros);
	}
	
	/**
	 * Descripcion: Llama a un modal para eliminar la cuenta
	 * Parametros: Proveedor @param view: listaCuentas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void eliminarCuenta(@BindingParam("cuenta") final Cuenta cuenta){
		super.mostrarMensaje("Confirmacion", "�Desea Eliminar Cuenta?", Messagebox.EXCLAMATION, new Messagebox.Button[]{Messagebox.Button.YES,Messagebox.Button.NO}, 
				new EventListener(){

					@Override
					public void onEvent(Event event) throws Exception {
						// TODO Auto-generated method stub
						Messagebox.Button button = (Messagebox.Button) event.getData();
						if (button == Messagebox.Button.YES) {
							cuenta.setEstatus(EEstatusGeneral.INACTIVO);
							sMaestros.registrarCuenta(cuenta);
							cambiarCuentas(0, null, null);
							notifyChange("cuentas");							          
						}
						
					}
					
		}, null);
	}
	
	
	/**
	 * Descripcion: Permitira filtrar por los campos del analista
	 * Parametros: @param view: listaCuenta.zul    
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange("*")
	public void aplicarFiltro(){
		cambiarCuentas(0, null, null);
	}
	/** METODOS PROPIOS DE LA CLASE */
	/** SETTERS Y GETTERS */

	public SMaestros getsMaestros() {
		return sMaestros;
	}

	public void setsMaestros(SMaestros sMaestros) {
		this.sMaestros = sMaestros;
	}

	public List<Cuenta> getCuentas() {
		return cuentas;
	}

	public void setCuentas(List<Cuenta> cuentas) {
		this.cuentas = cuentas;
	}

	public Cuenta getCuentaFiltro() {
		return cuentaFiltro;
	}

	public void setCuentaFiltro(Cuenta cuentaFiltro) {
		this.cuentaFiltro = cuentaFiltro;
	}
	
	 public STransaccion getsTransaccion() { 
		 return sTransaccion; 
	 } 
		 
	 public void setsTransaccion(STransaccion sTransaccion) { 
		 this.sTransaccion = sTransaccion; 
	 } 
	 
	 public Component getWinListaMisCuentas() {
		return winListaMisCuentas;
	}

	public void setWinListaMisCuentas(Component winListaMisCuentas) {
		this.winListaMisCuentas = winListaMisCuentas;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Boolean isVisible(Cuenta cuenta){
		Integer idProveedor = 0;
		Integer idProveedorCuenta = -1;
		usuario = super.getUsuario();
		if(usuario.getPersona().getTipoMenu()!=null){
			idProveedor = usuario.getPersona().getId();
		}
		Boolean respuesta = false;
		if(cuenta.getProveedor()==null && usuario.getPersona().getTipoMenu()!=null && (isAdminOrAnalista()))
			respuesta = true;
		else if(cuenta.getProveedor()==null && usuario.getPersona().getTipoMenu()!=null && !(isAdminOrAnalista()))
			respuesta = false;
		else if(cuenta.getProveedor()!=null && usuario.getPersona().getTipoMenu()!=null && (isAdminOrAnalista()))
			respuesta = false;
		else if(cuenta.getProveedor()!=null && usuario.getPersona().getTipoMenu()!=null && usuario.getPersona().getTipoMenu()==3 && idProveedor==cuenta.getProveedor().getId())
			respuesta = true;
		
		return respuesta;
	}
	
	public Boolean isAdminOrAnalista(){
		usuario = super.getUsuario();
		return (usuario.getPersona().getTipoMenu()==1 || usuario.getPersona().getTipoMenu()==2);
	}
	
	public String getFromList() {
		return fromList;
	}

	public void setFromList(String fromList) {
		this.fromList = fromList;
	}

}
