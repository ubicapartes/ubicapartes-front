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

import com.okiimport.app.model.OrdenCompra;
import com.okiimport.app.model.Proveedor;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.transaccion.STransaccion;

public class ListaOrdenesCompraProveedorViewModel extends AbstractRequerimientoViewModel implements EventListener<SortEvent>{
	
	//Servicios
	@BeanInjector("sTransaccion")
	private STransaccion sTransaccion;
	
	//GUI
	@Wire("#gridMisOrdenesDeCompra")
	protected Listbox gridMisOrdenesDeCompra;
			
	@Wire("#pagMisOrdenesDeCompra")
	protected Paging pagMisOrdenesDeCompra;
	
	//Atributos
	private List<OrdenCompra> listaOrdenesDeCompra;
	private OrdenCompra ordenCompra;
	
	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: listaOrdenesCompraProveedor.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);	
		this.ordenCompra = new OrdenCompra((Proveedor) this.getUsuario().getPersona());
		cambiarOrdenesDeCompra(0, null, null);
		agregarGridSort(gridMisOrdenesDeCompra);
		pagMisOrdenesDeCompra.setPageSize(pageSize);
	}

	/**Interface: EventListener<SortEvent>*/
	@Override
	@NotifyChange("listaOrdenesDeCompra")
	public void onEvent(SortEvent event) throws Exception {	
		if(event.getTarget() instanceof Listheader){
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("fieldSort", ((Listheader) event.getTarget()).getValue().toString());
			parametros.put("sortDirection", event.isAscending());
			ejecutarGlobalCommand("cambiarOrdenesDeCompra", parametros);
		}
	}
	
	/**GLOBAL COMMAND*/
	@GlobalCommand
	@SuppressWarnings("unchecked")
	@NotifyChange("listaOrdenesDeCompra")
	private void cambiarOrdenesDeCompra(@Default("0") @BindingParam("page") int page, 
			@BindingParam("fieldSort") String fieldSort, 
			@BindingParam("sortDirection") Boolean sortDirection) {
		Map<String, Object> parametros = sTransaccion.consultarOrdenesCompraProveedor(ordenCompra, null, fieldSort, sortDirection, page, pageSize);
		Integer total = (Integer) parametros.get("total");
		listaOrdenesDeCompra = (List<OrdenCompra>) parametros.get("ordenesCompra");
		pagMisOrdenesDeCompra.setActivePage(page);
		pagMisOrdenesDeCompra.setTotalSize(total);
	}
	
	/**COMMAND*/
	/*
	 * Descripcion: permitira cambiar la paginacion de acuerdo a la pagina activa del Paging
	 * @param Ninguno
	 * Retorno: Ninguno
	 * */
	@Command
	@NotifyChange("*")
	public void paginarLista(){
		int page=pagMisOrdenesDeCompra.getActivePage();
		cambiarOrdenesDeCompra(page, null, null);
	}

	/**GETTERS Y SETTERS*/
	public STransaccion getsTransaccion() {
		return sTransaccion;
	}

	public void setsTransaccion(STransaccion sTransaccion) {
		this.sTransaccion = sTransaccion;
	}
	
	public List<OrdenCompra> getListaOrdenesDeCompra() {
		return listaOrdenesDeCompra;
	}

	public void setListaOrdenesDeCompra(List<OrdenCompra> listaOrdenesDeCompra) {
		this.listaOrdenesDeCompra = listaOrdenesDeCompra;
	}
	
}