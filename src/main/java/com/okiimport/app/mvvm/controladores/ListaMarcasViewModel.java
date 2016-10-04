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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SortEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;

import com.okiimport.app.model.MarcaVehiculo;
import com.okiimport.app.model.enumerados.EEstatusGeneral;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.maestros.SMaestros;

public class ListaMarcasViewModel extends AbstractRequerimientoViewModel implements
		EventListener<SortEvent> {

	// Servicios
	@BeanInjector("sMaestros")
	private SMaestros sMaestros;

	// GUI
	@Wire("#gridMarcas")
	private Listbox gridMarcas;

	@Wire("#pagMarcas")
	private Paging pagMarcas;
	
	// Atributos
	// Modelos
	private List<MarcaVehiculo> marcas;
	private MarcaVehiculo marcaFiltro;
	private static final int PAGE_SIZE = 10;

	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: listaMarcas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view) {
		super.doAfterCompose(view);
		marcaFiltro = new MarcaVehiculo();
		pagMarcas.setPageSize(PAGE_SIZE);
		agregarGridSort(gridMarcas);
		cambiarMarcas(0, null, null);
	}

	/** Interface: EventListener<SortEvent> */
	@Override
	@NotifyChange("marcas")
	public void onEvent(SortEvent event) throws Exception {
		// TODO Auto-generated method stub
		if (event.getTarget() instanceof Listheader) {
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("fieldSort", event.getTarget().getId().toString());
			parametros.put("sortDirection", event.isAscending());
			ejecutarGlobalCommand("cambiarUsuarios", parametros);
		}

	}

	/** GLOBAL COMMAND */
	/**
	 * Descripcion: Llama a consultar la marca
	 * Parametros: @param view: listaMarcas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@SuppressWarnings("unchecked")
	@GlobalCommand
	@NotifyChange("marcas")
	public void cambiarMarcas(@Default("0") @BindingParam("page") int page,
			@BindingParam("fieldSort") String fieldSort,
			@BindingParam("sortDirection") Boolean sortDirection) {
		Map<String, Object> parametros = sMaestros.consultarMarcasF(marcaFiltro,  fieldSort, sortDirection, 
				 page, pageSize);//marcaFiltro,
		Integer total = (Integer) parametros.get("total");
		marcas = (List<MarcaVehiculo>) parametros.get("marcas");
		pagMarcas.setActivePage(page);
		pagMarcas.setTotalSize(total);
	}
	

	/** COMMAND */
	/**
	 * Descripcion: Permitira cambiar la paginacion de acuerdo a la pagina activa del Paging 
	 * Parametros: @param view: listaMarcas.zul  
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange("*")
	public void paginarLista() {
		int page = pagMarcas.getActivePage();
		cambiarMarcas(page, null, null);
	}


	/**
	 * Descripcion: Llama a un modal para crear o registrar una Marca
	 * Parametros: @param view: listaMarcas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void nuevaMarca() {
		llamarFormulario("formularioMarcas.zul", null);
	}

	/**
	 * Descripcion: Llama a un modal para ver los datos de la marca
	 * Parametros: Marca @param view: listaMarcas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void verMarca(@BindingParam("marca") MarcaVehiculo marca){
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("marca", marca);
		parametros.put("recordMode", "READ");
		//parametros.put("editar", false);
		llamarFormulario("formularioMarcas.zul", parametros);
	}
	
	/**
	 * Descripcion: Llama a un modal para editar los datos de la marca
	 * Parametros: Marca @param view: listaMarcas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void editarMarca(@BindingParam("marca") MarcaVehiculo marca){
		
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("marca", marca);
			parametros.put("valor", "editar");
			//parametros.put("editar", true);
			llamarFormulario("formularioMarcas.zul", parametros);
	}
	
	/**
	 * Descripcion: Metodo de la clase que permite llamar formularios 
	 * Parametros: @param view: listaMarcas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	private void llamarFormulario(String ruta, Map<String, Object> parametros) {
		crearModal(BasePackageSistemaMaest + ruta, parametros);
	}
	
	/**
	 * Descripcion: Llama a un modal para eliminar la marca
	 * Parametros: Proveedor @param view: listaMarcas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void eliminarMarca(@BindingParam("marca") final MarcaVehiculo marcaVehiculo){
		super.mostrarMensaje("Confirmacion", "¿Desea Eliminar Marca?", Messagebox.EXCLAMATION, new Messagebox.Button[]{Messagebox.Button.YES,Messagebox.Button.NO}, 
				new EventListener(){

					@Override
					public void onEvent(Event event) throws Exception {
						// TODO Auto-generated method stub
						Messagebox.Button button = (Messagebox.Button) event.getData();
						if (button == Messagebox.Button.YES) {
							
							//if (sTransaccion.validarProveedorEnCotizaciones(proveedor)){
								//proveedor.setiEstatus(EstatusProveedorFactory.getEstatusEliminado());
								//HAY QUE CREAR EN SMAESTROS EL SERVICIO PARA ACTUALIZAR LA MARCA
								marcaVehiculo.setEstatus(EEstatusGeneral.INACTIVO);
								sMaestros.registrarMarca(marcaVehiculo);
								cambiarMarcas(0, null, null);
								notifyChange("marcas");
							}
							else
								//SI ES QUE ES NECESARIO, ¿CUANDO NO PUEDO ELIMINAR UNA MARCA?
								mostrarMensaje("Informacion", "No se Puede eliminar la Marca", Messagebox.EXCLAMATION, null, null, null);
						}
						
					//}
					
		}, null);
	}
	
	/**
	 * Descripcion: Llama a un modal para cambiar el estatus de la marca a ACTIVO
	 * Parametros: Analista @param view: listaMarcas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void actualizarEstatus(@BindingParam("marca") final MarcaVehiculo marca){
		super.mostrarMensaje("Confirmacion", "¿Está seguro que desea cambiar el estatus de la marca?", Messagebox.EXCLAMATION, new Messagebox.Button[]{Messagebox.Button.YES,Messagebox.Button.NO}, 
				new EventListener(){

					@Override
					public void onEvent(Event event) throws Exception {
						// TODO Auto-generated method stub
						Messagebox.Button button = (Messagebox.Button) event.getData();
						if (button == Messagebox.Button.YES) {
							
								marca.setEstatus(EEstatusGeneral.ACTIVO);
								sMaestros.registrarMarca(marca);
								cambiarMarcas(0, null, null);
								notifyChange("marcas");
							}
							
				}
					
			
		}, null);
	}

	/**
	 * Descripcion: Permitira filtrar por los campos del analista
	 * Parametros: @param view: listaMarca.zul    
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange("*")
	public void aplicarFiltro(){
		cambiarMarcas(0, null, null);
	}
	/** METODOS PROPIOS DE LA CLASE */
	/** SETTERS Y GETTERS */

	public SMaestros getsMaestros() {
		return sMaestros;
	}

	public void setsMaestros(SMaestros sMaestros) {
		this.sMaestros = sMaestros;
	}

	public List<MarcaVehiculo> getMarcas() {
		return marcas;
	}

	public void setMarcas(List<MarcaVehiculo> marcas) {
		this.marcas = marcas;
	}

	public MarcaVehiculo getMarcaFiltro() {
		return marcaFiltro;
	}

	public void setMarcaFiltro(MarcaVehiculo marcaFiltro) {
		this.marcaFiltro = marcaFiltro;
	}

}
