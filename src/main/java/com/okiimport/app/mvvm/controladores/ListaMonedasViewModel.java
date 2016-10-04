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


import com.okiimport.app.model.Moneda;

import com.okiimport.app.model.enumerados.EEstatusGeneral;

import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.configuracion.SControlConfiguracion;
import com.okiimport.app.service.maestros.SMaestros;
import com.okiimport.app.service.transaccion.STransaccion;

public class ListaMonedasViewModel  extends AbstractRequerimientoViewModel implements EventListener<SortEvent> {

	
	//Servicios
		@BeanInjector("sMaestros")
		protected SMaestros sMaestros;
		@BeanInjector("sTransaccion")
		private STransaccion sTransaccion;
		@BeanInjector("sControlConfiguracion")
		private SControlConfiguracion sControlConfiguracion;
		
		//GUI
		@Wire("#gridMonedas")
		private Listbox gridMonedas;
		
		@Wire("#pagMonedas")
		protected Paging pagMonedas;
		
		//Atributos
		
		Window windowMonedas = null;
		int idcount = 0;
		private boolean makeAsReadOnly;
		protected List<Moneda> monedas;
		protected Moneda monedaFiltro;
		
		
		
		/**
		 * Descripcion: Llama a inicializar la clase 
		 * Parametros: @param view: listaMonedas.zul 
		 * Retorno: Ninguno 
		 * Nota: Ninguna
		 * */
		@AfterCompose
		public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
			try {super.doAfterCompose(view);
			monedaFiltro = new Moneda();
			pagMonedas.setPageSize(pageSize);
			agregarGridSort(gridMonedas);
			consultarMonedas(0, null, null);
			} catch (Exception e) {
			    System.out.println("do after    "+e.getMessage());
			}
		}
		
	
	@Override
	@NotifyChange("monedas")
	public void onEvent(SortEvent event) throws Exception {
		try {// TODO Auto-generated method stub
		if(event.getTarget() instanceof Listheader){
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("fieldSort",  event.getTarget().getId().toString());
			parametros.put("sortDirection", event.isAscending());
			ejecutarGlobalCommand("cambiarMonedas", parametros );
		}} catch (Exception e) {
		    System.out.println("on event "+e.getMessage());
		}
		
		
	}
	
	/**GLOBAL COMMAND*/
	/**
	 * Descripcion: Llama a consultar monedas  
	 * Parametros: @param view: listaMonedas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@GlobalCommand
	@NotifyChange("monedas")
	public void consultarMonedas(@Default("0") @BindingParam("page") int page, 
			@BindingParam("fieldSort") String fieldSort, 
			@BindingParam("sortDirection") Boolean sortDirection){
		try{
		Map<String, Object> parametros = sControlConfiguracion.consultarMonedasConHistorico(page, pageSize);
		Integer total = (Integer) parametros.get("total");
		monedas = (List<Moneda>) parametros.get("monedas");
		System.out.println("monedas"+monedas.get(2).getNombre());
		
		pagMonedas.setActivePage(page);
		pagMonedas.setTotalSize(total);
		System.out.println("moneda tamano  "+total);
		} catch (Exception e) {
		    System.out.println("consultar moneda  "+e.getMessage());
		}
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
		int page=pagMonedas.getActivePage();
		consultarMonedas(page, null, null);
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
		consultarMonedas(0, null, null);
	}
	
	/**
	 * Descripcion: Llama a un modal para crear o registrar un proveedor
	 * Parametros: @param view: listaProveedores.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void nuevaMoneda(){
		windowMonedas = crearModal(BasePackageSistemaMaest+"formularioMoneda.zul", null);
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
	 * Descripcion: Llama a un modal para editar los datos del proveedor
	 * Parametros: Proveedor @param view: listaProveedores.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void editarMoneda(@BindingParam("moneda") Moneda moneda){
		//cargarModelosLazy(moneda);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("moneda", moneda);
		map.put("recordMode", "EDIT");
		map.put("cerrar", false);
		Sessions.getCurrent().setAttribute("allmyvalues", map);
		if (windowMonedas != null) {
			windowMonedas.detach();
			windowMonedas.setId(null);
		}
		windowMonedas = crearModal(BasePackageSistemaMaest+"formularioMoneda.zul", map);
		windowMonedas.setMaximizable(true);
		windowMonedas.doModal();
		windowMonedas.setId("doModal" + "" + idcount + "");
		
		
	}
	
	/**
	 * Descripcion: Llama a un modal para ver los datos del proveedor
	 * Parametros: Proveedor @param view: listaProveedores.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void verMoneda(
			@BindingParam("moneda") Moneda moneda) {
		//cargarModelosLazy(proveedor);
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("moneda", moneda);
		map.put("recordMode", "READ");
		map.put("cerrar", false);
		Sessions.getCurrent().setAttribute("allmyvalues", map);
		if (windowMonedas != null) {
			windowMonedas.detach();
			windowMonedas.setId(null);
		}
		windowMonedas = this.crearModal(BasePackageSistemaMaest+"formularioMoneda.zul", map);
		windowMonedas.setMaximizable(true);
		windowMonedas.doModal();
		windowMonedas.setId("doModal" + "" + idcount + "");
	}
	
	/**
	 * Descripcion: Llama a un modal para eliminar los datos del proveedor
	 * Parametros: Proveedor @param view: listaProveedores.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	/*@Command
	public void eliminarMoneda(@BindingParam("moneda") final Moneda moneda){
		super.mostrarMensaje("Confirmacion", "¿Desea Eliminar la Moneda?", Messagebox.EXCLAMATION, new Messagebox.Button[]{Messagebox.Button.YES,Messagebox.Button.NO}, 
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
	*/
	
	/*@SuppressWarnings({ "unchecked", "rawtypes" })
	private void cargarModelosLazy(final Moneda moneda){
		List<MarcaVehiculo> marcasVehiculo = new ArrayList((List<MarcaVehiculo>) sMaestros.consultarMarcasVehiculoProveedor(proveedor.getId(), 0, -1).get("marcas"));
		List<ClasificacionRepuesto> clasifRepuesto = new ArrayList((List<ClasificacionRepuesto>) sMaestros.consultarClasificacionRepuestoProveedor(proveedor.getId(), 0, -1).get("clasificacionRepuesto"));
		proveedor.setMarcaVehiculos(marcasVehiculo);
		proveedor.setClasificacionRepuestos(clasifRepuesto);
	}*/
	
	
	/**
	 * Descripcion: Llama a un modal para eliminar los datos del proveedor
	 * Parametros: Proveedor @param view: listaProveedores.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void eliminarMoneda(@BindingParam("moneda") final Moneda moneda){
		super.mostrarMensaje("Confirmacion", "¿Desea Eliminar la moneda?", Messagebox.EXCLAMATION, new Messagebox.Button[]{Messagebox.Button.YES,Messagebox.Button.NO}, 
				new EventListener(){

					@Override
					public void onEvent(Event event) throws Exception {
						// TODO Auto-generated method stub
						Messagebox.Button button = (Messagebox.Button) event.getData();
						if (button == Messagebox.Button.YES) {
							
							System.out.println("PASE1");
								moneda.setEstatus(EEstatusGeneral.INACTIVO);
								sMaestros.registrarMoneda(moneda);
								System.out.println("PASE");
								consultarMonedas(0, null, null);
								notifyChange("monedas");
							}
							
						}
						
					
					
					
			
		}, null);
	}
	
	public SMaestros getsMaestros() {
		return sMaestros;
	}


	public void setsMaestros(SMaestros sMaestros) {
		this.sMaestros = sMaestros;
	}


	public STransaccion getsTransaccion() {
		return sTransaccion;
	}


	public void setsTransaccion(STransaccion sTransaccion) {
		this.sTransaccion = sTransaccion;
	}


	public SControlConfiguracion getsControlConfiguracion() {
		return sControlConfiguracion;
	}


	public void setsControlConfiguracion(SControlConfiguracion sControlConfiguracion) {
		this.sControlConfiguracion = sControlConfiguracion;
	}


	public Paging getPagMonedas() {
		return pagMonedas;
	}


	public void setPagMonedas(Paging pagMonedas) {
		this.pagMonedas = pagMonedas;
	}


	public List<Moneda> getMonedas() {
		return monedas;
	}


	public void setMonedas(List<Moneda> monedas) {
		this.monedas = monedas;
	}


	public Moneda getMonedaFiltro() {
		return monedaFiltro;
	}


	public void setMonedaFiltro(Moneda monedaFiltro) {
		this.monedaFiltro = monedaFiltro;
	}


	public Listbox getGridMonedas() {
		return gridMonedas;
	}


	public void setGridMonedas(Listbox gridMonedas) {
		this.gridMonedas = gridMonedas;
	}


	public boolean isMakeAsReadOnly() {
		return makeAsReadOnly;
	}


	public void setMakeAsReadOnly(boolean makeAsReadOnly) {
		this.makeAsReadOnly = makeAsReadOnly;
	}
	
	
	
}
