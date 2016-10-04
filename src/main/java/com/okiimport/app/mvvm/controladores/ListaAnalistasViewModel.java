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

import com.okiimport.app.model.Analista;
import com.okiimport.app.model.factory.persona.EstatusPersonaFactory;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.maestros.SMaestros;
import com.okiimport.app.service.transaccion.STransaccion;

public class ListaAnalistasViewModel extends AbstractRequerimientoViewModel implements EventListener<SortEvent>{
	
	//Servicios
	@BeanInjector("sMaestros")
	private SMaestros sMaestros;
	@BeanInjector("sTransaccion")
	private STransaccion sTransaccion;
	
	//GUI
	@Wire("#gridAnalistas")
	private Listbox gridAnalistas;
	
	@Wire("#pagAnalistas")
	private Paging pagAnalistas;
	
	//Modelos
	private List<Analista> analistas;
	private Analista analistaFiltro;
	
	
	Window window = null;
	int idcount = 0;

	
	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: listaAnalistas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
		analistaFiltro = new Analista();
		pagAnalistas.setPageSize(pageSize);
		agregarGridSort(gridAnalistas);
		cambiarAnalistas(0, null, null);
	}
	
	
	/**Interface: EventListener<SortEvent>*/
	@Override
	@NotifyChange("analistas")
	public void onEvent(SortEvent event) throws Exception {
		// TODO Auto-generated method stub		
		if(event.getTarget() instanceof Listheader){
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("fieldSort",  event.getTarget().getId().toString());
			parametros.put("sortDirection", event.isAscending());
			ejecutarGlobalCommand("cambiarAnalistas", parametros );
		}
		
	}
	
	
	/**GLOBAL COMMAND*/
	/**
	 * Descripcion: Llama a consultar analistas  
	 * Parametros: @param view: listaAnalistas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@SuppressWarnings("unchecked")
	@GlobalCommand
	@NotifyChange("analistas")
	public void cambiarAnalistas(@Default("0") @BindingParam("page") int page, 
			@BindingParam("fieldSort") String fieldSort, 
			@BindingParam("sortDirection") Boolean sortDirection){
		Map<String, Object> parametros = sMaestros.consultarAnalistasF(analistaFiltro, fieldSort, sortDirection, 
				 page, pageSize);
		Integer total = (Integer) parametros.get("total");
		analistas = (List<Analista>) parametros.get("analistas");
		pagAnalistas.setActivePage(page);
		pagAnalistas.setTotalSize(total);
	}     
	

	/**
	 * Descripcion: Llama a cerrar a vista
	 * Parametros: @param view: listaAnalistas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange("*")
	public void cerrarvista(){
		
		cambiarAnalistas(0, null, null);
	}
	
	/**COMMAND*/
	/**
	 * Descripcion: Permitira cambiar la paginacion de acuerdo a la pagina activa del Paging 
	 * Parametros: @param view: listaAnalistas.zul  
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange("*")
	public void paginarLista(){
		int page=pagAnalistas.getActivePage();
		cambiarAnalistas(page, null, null);
	}

	/*@Command
	public void verAnalista(@BindingParam("analista") Analista analista){
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("analista", analista);
		llamarFormulario("verAnalistas.zul", parametros);
	}*/
	
	
	/**
	 * Descripcion: Llama a un modal para ver los datos del analista
	 * Parametros: Analista @param view: listaAnalistas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void verAnalista(@BindingParam("analista") Analista analista){
		
		if(analista!=null){
			System.out.println("nombre del analista: "+analista.getNombre()+" "+analista.getApellido());
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("analista", analista);
			parametros.put("recordMode", "READ");
			parametros.put("cerrar", false);
			Sessions.getCurrent().setAttribute("allmyvalues", parametros);
			if (window != null) {
				window.detach();
				window.setId(null);
			}
			window = this.crearModal(BasePackageSistemaMaest+"formularioAnalistas.zul", parametros);
			window.setMaximizable(true);
			window.doModal();
			window.setId("doModal" + "" + idcount + "");
		}else{
			System.out.println("prueba:------ analista es nulo");
		}
		
	}
	
	/*@Command
	public void editarAnalista(@BindingParam("analista") Analista analista){
		
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("analista", analista);
			llamarFormulario("editarAnalistas.zul", parametros);
	}*/
	
	/**
	 * Descripcion: Llama a un modal para editar los datos del analista
	 * Parametros: Analista @param view: listaAnalistas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void editarAnalista(@BindingParam("analista") Analista analista){
		
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("analista", analista);
			parametros.put("recordMode", "EDIT");
			parametros.put("valor", "editar");
			//parametros.put("editar", true);
			llamarFormulario("formularioAnalistas.zul", parametros);
	}
	
	
	
	/**
	 * Descripcion: Llama a un modal para crear o registrar un analista
	 * Parametros: @param view: listaAnalistas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void nuevoAnalista(){
		Map<String, Object> parametros = new HashMap<String, Object>();

		//parametros.put("recordMode", "NEW");
		llamarFormulario("formularioAnalistas.zul", null);
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
	 * Descripcion: Llama a un modal para eliminar los datos del analista
	 * Parametros: Analista @param view: listaAnalistas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void eliminarAnalista(@BindingParam("analista") final Analista analista){
		super.mostrarMensaje("Confirmacion", "¿Desea Eliminar el Analista?", Messagebox.EXCLAMATION, new Messagebox.Button[]{Messagebox.Button.YES,Messagebox.Button.NO}, 
				new EventListener(){

					@Override
					public void onEvent(Event event) throws Exception {
						// TODO Auto-generated method stub
						Messagebox.Button button = (Messagebox.Button) event.getData();
						if (button == Messagebox.Button.YES) {
							
							analista.setiEstatus(EstatusPersonaFactory.getEstatusInactivo());
		//				EL METODO DICE ACTUTALIZARPERSONA
						sMaestros.acutalizarPersona(analista);
						cambiarAnalistas(0, null, null);
						notifyChange("analistas");
						
//										if (sTransaccion.validarAnalistaEnRequerimientos(analista)){
//											analista.setiEstatus(EstatusPersonaFactory.getEstatusInactivo());
//											//EL METODO DICE ACTUTALIZARPERSONA
//											sMaestros.acutalizarPersona(analista);
//											cambiarAnalistas(0, null, null);
//											notifyChange("analistas");
//										}
//										else
//											mostrarMensaje("Informacion", "No se Puede eliminar el analista, esta asignado a un requerimiento que esta activo", Messagebox.EXCLAMATION, null, null, null);
//										}
								
								
							}
					}
							
				
					
			
		}, null);
	}
	
	
	/**
	 * Descripcion: Llama a un modal para cambiar el estatus del analista a ACTIVO
	 * Parametros: Analista @param view: listaAnalistas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void actualizarEstatus(@BindingParam("analista") final Analista analista){
		super.mostrarMensaje("Confirmacion", "¿Está seguro que desea cambiar el estatus del analista?", Messagebox.EXCLAMATION, new Messagebox.Button[]{Messagebox.Button.YES,Messagebox.Button.NO}, 
				new EventListener(){

					@Override
					public void onEvent(Event event) throws Exception {
						// TODO Auto-generated method stub
						Messagebox.Button button = (Messagebox.Button) event.getData();
						if (button == Messagebox.Button.YES) {
							
								analista.setiEstatus(EstatusPersonaFactory.getEstatusActivo());
								//EL METODO DICE ACTUTALIZARPERSONA
								sMaestros.acutalizarPersona(analista);
								cambiarAnalistas(0, null, null);
								notifyChange("analistas");
							}
							
				}
					
			
		}, null);
	}
	
	
	
	/**METODOS PROPIOS DE LA CLASE*/
	
	/**
	 * Descripcion: Permitira filtrar por los campos del analista
	 * Parametros: @param view: listaAnalista.zul    
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange("*")
	public void aplicarFiltro(){
		cambiarAnalistas(0, null, null);
	}
	/**SETTERS Y GETTERS*/

	public List<Analista> getAnalistas() {
		return analistas;
	}
	
	public void setAnalistas(List<Analista> analistas) {
		this.analistas = analistas;
	}
	
	public Analista getAnalistaFiltro() {
		return analistaFiltro;
	}

	public SMaestros getsMaestros() {
		return sMaestros;
	}

	public void setsMaestros(SMaestros sMaestros) {
		this.sMaestros = sMaestros;
	}
	
	
}
