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
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SortEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;

import com.okiimport.app.model.Analista;
import com.okiimport.app.model.ClasificacionRepuesto;
import com.okiimport.app.model.MarcaVehiculo;
import com.okiimport.app.model.Proveedor;
import com.okiimport.app.model.factory.persona.EstatusPersonaFactory;
import com.okiimport.app.model.factory.persona.EstatusProveedorFactory;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.maestros.SMaestros;
import com.okiimport.app.service.transaccion.STransaccion;


public class ListaProveedoresViewModel extends AbstractRequerimientoViewModel implements EventListener<SortEvent>{
	
	//Servicios
	@BeanInjector("sMaestros")
	protected SMaestros sMaestros;
	@BeanInjector("sTransaccion")
	private STransaccion sTransaccion;
	
	
	//GUI
	@Wire("#gridProveedores")
	private Listbox gridProveedores;
	
	@Wire("#pagProveedores")
	protected Paging pagProveedores;
	
	//Atributos
	
	Window window = null;
	int idcount = 0;
	private boolean makeAsReadOnly;
	protected List<Proveedor> proveedores;
	protected Proveedor proveedorFiltro;

	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: listaProveedores.zul 
	 * Retorno: Ninguno 
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
		proveedorFiltro = new Proveedor();
		pagProveedores.setPageSize(pageSize);
		agregarGridSort(gridProveedores);
		System.out.println("antes de consultar");
		consultarProveedores(0, null, null);
	}
	
	/**Interface: EventListener<SortEvent>*/
	@Override
	@NotifyChange("proveedores")
	public void onEvent(SortEvent event) throws Exception {
		// TODO Auto-generated method stub		
		if(event.getTarget() instanceof Listheader){
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("fieldSort",  event.getTarget().getId().toString());
			parametros.put("sortDirection", event.isAscending());
			ejecutarGlobalCommand("cambiarProveedores", parametros );
		}
		
	}
	
	/**GLOBAL COMMAND*/
	/**
	 * Descripcion: Llama a consultar proveedores  
	 * Parametros: @param view: listaProveedores.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@SuppressWarnings("unchecked")
	@GlobalCommand
	@NotifyChange("proveedores")
	public void consultarProveedores(@Default("0") @BindingParam("page") int page, 
			@BindingParam("fieldSort") String fieldSort, 
			@BindingParam("sortDirection") Boolean sortDirection){
		Map<String, Object> parametros = sMaestros.consultarProveedores(proveedorFiltro, page, pageSize);
		Integer total = (Integer) parametros.get("total");
		proveedores = (List<Proveedor>) parametros.get("proveedores");
		pagProveedores.setActivePage(page);
		pagProveedores.setTotalSize(total);
	}
	
	/**COMMAND*/
	/**
	 * Descripcion: Permitira cambiar la paginacion de acuerdo a la pagina activa del Paging 
	 * Parametros: @param view: listaProveedores.zul   
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange("*")
	public void paginarLista(){
		int page=pagProveedores.getActivePage();
		consultarProveedores(page, null, null);
	}
	
	/**
	 * Descripcion: Permitira filtrar por los campos del proveedor
	 * Parametros: @param view: listaProveedores.zul   
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange("*")
	public void aplicarFiltro(){
		consultarProveedores(0, null, null);
	}
	
	/**
	 * Descripcion: Llama a un modal para crear o registrar un proveedor
	 * Parametros: @param view: listaProveedores.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void nuevoProveedor(){
		llamarFormulario(BasePackageSistemaMaest+"formularioProveedor.zul", null);
	}
	
	/**
	 * Descripcion: Llama a un modal para editar los datos del proveedor
	 * Parametros: Proveedor @param view: listaProveedores.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void editarProveedor(@BindingParam("proveedor") Proveedor proveedor){
		cargarModelosLazy(proveedor);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("proveedor", proveedor);
		map.put("recordMode", "EDIT");
		map.put("valor", "editar");
		map.put("cerrar", false);
		Sessions.getCurrent().setAttribute("allmyvalues", map);
		if (window != null) {
			window.detach();
			window.setId(null);
		}
		window = crearModal(BasePackageSistemaMaest+"formularioProveedor.zul", map);
		window.setMaximizable(true);
		window.doModal();
		window.setId("doModal" + "" + idcount + "");
		
		
	}
	
	
	/**
	 * Descripcion: Llama a un modal para ver los datos del proveedor
	 * Parametros: Proveedor @param view: listaProveedores.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void verProveedor(
			@BindingParam("proveedor") Proveedor proveedor) {
		cargarModelosLazy(proveedor);
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("proveedor", proveedor);
		map.put("recordMode", "READ");
		map.put("cerrar", false);
		Sessions.getCurrent().setAttribute("allmyvalues", map);
		if (window != null) {
			window.detach();
			window.setId(null);
		}
		window = this.crearModal(BasePackageSistemaMaest+"formularioProveedor.zul", map);
		window.setMaximizable(true);
		window.doModal();
		window.setId("doModal" + "" + idcount + "");
	}
	
	/**
	 * Descripcion: Llama a un modal para eliminar los datos del proveedor
	 * Parametros: Proveedor @param view: listaProveedores.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void eliminarProveedor(@BindingParam("proveedor") final Proveedor proveedor){
		super.mostrarMensaje("Confirmacion", "¿Desea Eliminar Proveedor?", Messagebox.EXCLAMATION, new Messagebox.Button[]{Messagebox.Button.YES,Messagebox.Button.NO}, 
				new EventListener(){

					@Override
					public void onEvent(Event event) throws Exception {
						// TODO Auto-generated method stub
						Messagebox.Button button = (Messagebox.Button) event.getData();
						if (button == Messagebox.Button.YES) {
							
							if (sTransaccion.validarProveedorEnCotizaciones(proveedor)){
								proveedor.setiEstatus(EstatusProveedorFactory.getEstatusEliminado());
								//EL METODO DICE ACTUTALIZARPERSONA
								sMaestros.acutalizarPersona(proveedor);
								consultarProveedores(0, null, null);
								notifyChange("proveedores");
							}
							else
								mostrarMensaje("Informacion", "No se Puede eliminar el proveedor", Messagebox.EXCLAMATION, null, null, null);
						}
						
					}
					
					
			
		}, null);
	}
	
	@Command
	public void registrarProveedor(){
		window = crearModal(BasePackageSistemaMaest+"formularioProveedor.zul", null);
		window.setMaximizable(true);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void cargarModelosLazy(final Proveedor proveedor){
		List<MarcaVehiculo> marcasVehiculo = new ArrayList((List<MarcaVehiculo>) sMaestros.consultarMarcasVehiculoProveedor(proveedor.getId(), 0, -1).get("marcas"));
		List<ClasificacionRepuesto> clasifRepuesto = new ArrayList((List<ClasificacionRepuesto>) sMaestros.consultarClasificacionRepuestoProveedor(proveedor.getId(), 0, -1).get("clasificacionRepuesto"));
		proveedor.setMarcaVehiculos(marcasVehiculo);
		proveedor.setClasificacionRepuestos(clasifRepuesto);
	}
	
	/**
	 * Descripcion: Metodo de la clase que permite llamar formularios 
	 * Parametros: @param view: listaProveedores.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	private void llamarFormulario(String ruta, Map<String, Object> parametros){
		crearModal(BasePackageSistemaMaest+ruta, parametros);
	}

	/**
	 * Descripcion: Llama a un modal para cambiar el estatus del proveedor a ACTIVO
	 * Parametros: Analista @param view: listaProveedores.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void actualizarEstatus(@BindingParam("proveedor") final Proveedor proveedor){
		super.mostrarMensaje("Confirmacion", "¿Está seguro que desea cambiar el estatus del proveedor?", Messagebox.EXCLAMATION, new Messagebox.Button[]{Messagebox.Button.YES,Messagebox.Button.NO}, 
				new EventListener(){

					@Override
					public void onEvent(Event event) throws Exception {
						// TODO Auto-generated method stub
						Messagebox.Button button = (Messagebox.Button) event.getData();
						if (button == Messagebox.Button.YES) {
							
								proveedor.setiEstatus(EstatusProveedorFactory.getEstatusActivo());
								sMaestros.acutalizarPersona(proveedor);
								consultarProveedores(0, null, null);
								notifyChange("proveedores");;
							}
							
				}
					
			
		}, null);
	}
	
	
	
	/**METODOS PROPIOS DE LA CLASE*/

	/**SETTERS Y GETTERS*/

	public SMaestros getsMaestros() {
		return sMaestros;
	}

	public List<Proveedor> getProveedores() {
		return proveedores;
	}

	public void setProveedores(List<Proveedor> proveedores) {
		this.proveedores = proveedores;
	}

	public Proveedor getProveedorFiltro() {
		return proveedorFiltro;
	}

	public void setProveedorFiltro(Proveedor proveedorFiltro) {
		this.proveedorFiltro = proveedorFiltro;
	}

	public void setsMaestros(SMaestros sMaestros) {
		this.sMaestros = sMaestros;
	}

	public boolean isMakeAsReadOnly() {
		return makeAsReadOnly;
	}

	public void setMakeAsReadOnly(boolean makeAsReadOnly) {
		this.makeAsReadOnly = makeAsReadOnly;
	}

	public STransaccion getsTransaccion() {
		return sTransaccion;
	}

	public void setsTransaccion(STransaccion sTransaccion) {
		this.sTransaccion = sTransaccion;
	}
	
	
}
