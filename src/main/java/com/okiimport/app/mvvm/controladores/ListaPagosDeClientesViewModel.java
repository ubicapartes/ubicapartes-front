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

import com.okiimport.app.model.Cliente;
import com.okiimport.app.model.Compra;
import com.okiimport.app.model.PagoCliente;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.transaccion.STransaccion;


public class ListaPagosDeClientesViewModel extends AbstractRequerimientoViewModel implements EventListener<SortEvent> {

	//Servicios
	@BeanInjector("sTransaccion")
	protected STransaccion sTransaccion;
			
	//GUI
	@Wire("#gridPagosDeClientes")
	protected Listbox gridPagosDeClientes;
			
	@Wire("#pagPagosDeClientes")
	protected Paging pagPagosDeClientes;
		
	//Atributos	
	protected List <PagoCliente> listaDePagos;
	protected PagoCliente pagoClienteFiltro;
	private PagoCliente pagoCliente;	
		
	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: listaMisRequerimientosOfertados.zul 
	 * Retorno: Ninguno 
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
		Compra compra = new Compra(new Requerimiento(new Cliente()), null);
		pagoClienteFiltro = new PagoCliente(compra, null);
		pagPagosDeClientes.setPageSize(pageSize);
		agregarGridSort(gridPagosDeClientes);
		cambiarPagos(0, null, null);
		
		
	}
	
	/**Interface: EventListener<SortEvent>*/
	@Override
	@NotifyChange("listaDePagos")
	public void onEvent(SortEvent event) throws Exception {
		// TODO Auto-generated method stub		
		if(event.getTarget() instanceof Listheader){
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("fieldSort", ((Listheader) event.getTarget()).getValue().toString());
			parametros.put("sortDirection", event.isAscending());
			ejecutarGlobalCommand("cambiarPagos", parametros );
		}
		
	}
	
	/**GLOBAL COMMAND*/
	/**
	 * Descripcion: Llama a consultar pagos de clientes
	 * Parametros: @param view: listaPagosDeCientes.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@GlobalCommand
	@NotifyChange("listaDePagos")
	public void cambiarPagos(@Default("0") @BindingParam("page") int page, 
			@BindingParam("fieldSort") String fieldSort, 
			@BindingParam("sortDirection") Boolean sortDirection){
		Map<String, Object> parametros = sTransaccion.consultarPagosClientes(pagoClienteFiltro,  fieldSort, sortDirection, 
				 page, pageSize);
		Integer total = (Integer) parametros.get("total");
		listaDePagos = (List<PagoCliente>) parametros.get("pagoClientes");
		pagPagosDeClientes.setActivePage(page);
		pagPagosDeClientes.setTotalSize(total);
	}
	
	/**COMMAND*/
	/**
	 * Descripcion: Permitira cambiar la paginacion de acuerdo a la pagina activa del Paging 
	 * Parametros: @param view: listaPagosDeClientes.zul   
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange("*")
	public void paginarLista(){
		int page=pagPagosDeClientes.getActivePage();
		cambiarPagos(page, null, null);
	}
	
	/**
	 * Descripcion: Permitira filtrar por los campos del proveedor
	 * Parametros: @param view: listaPagosDeClientes.zul    
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange("*")
	public void aplicarFiltro(){
		cambiarPagos(0, null, null);
	}
	
	@Command
	public void verCompra(@BindingParam("pago") PagoCliente pago){
		Requerimiento requerimiento = pago.getCompra().getRequerimiento();
		
		Compra compra = pago.getCompra();
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("requerimiento", requerimiento);
		parametros.put("compra", compra);
		super.crearModal(BasePackageSistemaFunc+"ofertados/formularioVerCompra.zul", parametros);
	}

	public List<PagoCliente> getListaDePagos() {
		return listaDePagos;
	}

	public void setListaDePagos(List<PagoCliente> listaDePagos) {
		this.listaDePagos = listaDePagos;
	}

	public STransaccion getsTransaccion() {
		return sTransaccion;
	}

	public void setsTransaccion(STransaccion sTransaccion) {
		this.sTransaccion = sTransaccion;
	}

	public Listbox getGridPagosDeClientes() {
		return gridPagosDeClientes;
	}

	public void setGridPagosDeClientes(Listbox gridPagosDeClientes) {
		this.gridPagosDeClientes = gridPagosDeClientes;
	}

	public Paging getPagPagosDeClientes() {
		return pagPagosDeClientes;
	}

	public void setPagPagosDeClientes(Paging pagPagosDeClientes) {
		this.pagPagosDeClientes = pagPagosDeClientes;
	}

	public PagoCliente getPagoClienteFiltro() {
		return pagoClienteFiltro;
	}

	public void setPagoClienteFiltro(PagoCliente pagoClienteFiltro) {
		this.pagoClienteFiltro = pagoClienteFiltro;
	}

	public PagoCliente getPagoCliente() {
		return pagoCliente;
	}

	public void setPagoCliente(PagoCliente pagoCliente) {
		this.pagoCliente = pagoCliente;
	}
}