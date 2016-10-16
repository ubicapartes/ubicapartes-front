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
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SortEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Paging;

import com.okiimport.app.model.Analista;
import com.okiimport.app.model.Cliente;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.model.enumerados.EEstatusRequerimiento;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.model.ModeloCombo;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.transaccion.STransaccion;

public class ListaRequerimientosGeneralViewModel extends AbstractRequerimientoViewModel implements EventListener<SortEvent>{
	
	//Servicios
	@BeanInjector("sTransaccion")
	private STransaccion sTransaccion;
	
	//GUI
	@Wire("#gridMisRequerimientos")
	private Listbox gridMisRequerimientos;
	
	@Wire("#pagMisRequerimientos")
	private Paging pagMisRequerimientos;
	
	//Atributos
	private List <Requerimiento> listaRequerimientos;
	private Requerimiento requerimientoFiltro;
	private List<ModeloCombo<String>> listaEstatus;
	private ModeloCombo<String> estatusFiltro;

	
	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: listaRequerimientosGeneral.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
		requerimientoFiltro = new Requerimiento(new Cliente(), new Analista());
		cambiarRequerimientos(0, null, null);
		agregarGridSort(gridMisRequerimientos);
		pagMisRequerimientos.setPageSize(pageSize);
		estatusFiltro=new ModeloCombo<String>("No Filtrar", "");
		listaEstatus = llenarListaEstatusGeneral();
		listaEstatus.add(estatusFiltro);
	}
	
	/**Interface: EventListener<SortEvent>*/
	@Override
	@NotifyChange("listaRequerimientos")
	public void onEvent(SortEvent event) throws Exception {
		// TODO Auto-generated method stub		
		if(event.getTarget() instanceof Listheader){
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("fieldSort", ((Listheader) event.getTarget()).getValue().toString());
			parametros.put("sortDirection", event.isAscending());
			ejecutarGlobalCommand("cambiarRequerimientos", parametros );
		}
		
	}
	
	/**GLOBAL COMMAND*/
	/**
	 * Descripcion: permitira cambiar los requerimientos de la grid de acuerdo a la pagina dada como parametro 
	 * Parametros: @param view: listaRequerimientosGeneral.zul 
	 * @param page: pagina a consultar, si no se indica sera 0 por defecto
	 * @param fieldSort: campo de ordenamiento, puede ser nulo
	 * @param sorDirection: valor boolean que indica el orden ascendente (true) o descendente (false) del ordenamiento
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@GlobalCommand
	@SuppressWarnings("unchecked")
	@NotifyChange("listaRequerimientos")
	public void cambiarRequerimientos(@Default("0") @BindingParam("page") int page, 
			@BindingParam("fieldSort") String fieldSort, 
			@BindingParam("sortDirection") Boolean sortDirection){
		Map<String, Object> parametros = sTransaccion.consultarRequerimientosGeneral(requerimientoFiltro, 
				fieldSort, sortDirection, page, pageSize);
		Integer total = (Integer) parametros.get("total");
		listaRequerimientos = (List<Requerimiento>) parametros.get("requerimientos");
		pagMisRequerimientos.setActivePage(page);
		pagMisRequerimientos.setTotalSize(total);
	}
	
	/**COMMAND*/
	/**
	 * Descripcion: Permitira cambiar la paginacion de acuerdo a la pagina activa del Paging 
	 * Parametros: @param view: listaRequerimientosGeneral.zul   
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange("*")
	public void paginarLista(){
		int page=pagMisRequerimientos.getActivePage();
		cambiarRequerimientos(page, null, null);
	}
	
	/**
	 * Descripcion: permitira filtrar los datos de la grid de acuerdo al campo establecido en el evento
	 * Parametros: @param view: listaRequerimientosGeneral.zul   
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange("listaRequerimientos")
	public void aplicarFiltro(){
		requerimientoFiltro.setEstatus(null);
		if(estatusFiltro!=null)
			if(!estatusFiltro.getNombre().equalsIgnoreCase("No Filtrar"))
				requerimientoFiltro.setEstatus(EEstatusRequerimiento.findEEstatusRequerimiento(estatusFiltro.getValor()));
		cambiarRequerimientos(0, null, null);
	}
	
	/**
	 * Descripcion: permitira crear el emergente (modal) necesario para editar el requerimiento seleccionado
	 * Parametros: requerimiento @param view: listaRequerimientosGeneral.zul   
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void editarReguerimiento(@BindingParam("requerimiento") Requerimiento requerimiento){
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("requerimiento", requerimiento);
		parametros.put("editar", true);
		crearModal(BasePackageSistemaFunc+"emitidos/editarRequerimiento.zul", parametros);
	}
	
	/**
	 * Descripcion: permitira crear el emergente (modal) necesario para enviar las solicitudes de cotizacion
	 * a los proveedores del requerimiento seleccionado
	 * Parametros: Requerimiento @param view: listaRequerimientosGeneral.zul   
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void enviarProveedores(@BindingParam("requerimiento") Requerimiento requerimiento){
		if(!requerimiento.isCerrarSolicitud()){
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("requerimiento", requerimiento);
			crearModal(BasePackageSistemaFunc+"emitidos/enviarRequerimientoProv.zul", parametros);
		}
		else
			mostrarMensaje("Informaci\u00F3n", "Ha expirado el tiempo para Enviar a Proveedores", null, null, null, null);
	}
	
	/**
	 * Descripcion: permitira crear el emergente (modal) necesario para ver los requerimientos
	 * Parametros: Requerimiento @param view: listaRequerimientosGeneral.zul   
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void verRequerimiento(@BindingParam("requerimiento") Requerimiento requerimiento){
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("requerimiento", requerimiento);
		parametros.put("editar", false);
		crearModal(BasePackageSistemaFunc+"emitidos/editarRequerimiento.zul", parametros);
	}
	
	/**SETTERS Y GETTERS*/
	public STransaccion getsTransaccion() {
		return sTransaccion;
	}

	public void setsTransaccion(STransaccion sTransaccion) {
		this.sTransaccion = sTransaccion;
	}

	public List<Requerimiento> getListaRequerimientos() {
		return listaRequerimientos;
	}

	public void setListaRequerimientos(List<Requerimiento> listaRequerimientos) {
		this.listaRequerimientos = listaRequerimientos;
	}

	public Requerimiento getRequerimientoFiltro() {
		return requerimientoFiltro;
	}

	public void setRequerimientoFiltro(Requerimiento requerimientoFiltro) {
		this.requerimientoFiltro = requerimientoFiltro;
	}

	public List<ModeloCombo<String>> getListaEstatus() {
		return listaEstatus;
	}

	public void setListaEstatus(List<ModeloCombo<String>> listaEstatus) {
		this.listaEstatus = listaEstatus;
	}

	public ModeloCombo<String> getEstatusFiltro() {
		return estatusFiltro;
	}

	public void setEstatusFiltro(ModeloCombo<String> estatusFiltro) {
		this.estatusFiltro = estatusFiltro;
	}

}
