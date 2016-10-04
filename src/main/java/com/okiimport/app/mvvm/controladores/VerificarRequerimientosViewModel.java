package com.okiimport.app.mvvm.controladores;

import java.sql.Timestamp;
import java.util.Date;
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
import com.okiimport.app.model.DetalleOferta;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.model.ModeloCombo;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.transaccion.STransaccion;

public class VerificarRequerimientosViewModel extends AbstractRequerimientoViewModel implements EventListener<SortEvent> {

	//Servicios
	@BeanInjector("sTransaccion")
	private STransaccion sTransaccion;

	//GUI
	@Wire("#gridRequerimientosCliente")
	private Listbox gridRequerimientosCliente;
	@Wire("#pagRequerimientosCliente")
	private Paging pagRequerimientosCliente;
	
	//Atributos
	private Date fechaCreacion;
	private Cliente cliente;
	private Requerimiento requerimientoFiltro;
	private List <ModeloCombo<Boolean>> listaTipoPersona;
	private ModeloCombo<Boolean> tipoPersona;
	private List <Requerimiento> listaRequerimientos;

	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: formularioVerificarRequerimiento.zul 
	 * Retorno: Ninguno 
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view)
	{
		super.doAfterCompose(view);
		cliente = new Cliente();
		requerimientoFiltro = new Requerimiento();
		pagRequerimientosCliente.setPageSize(pageSize);
		agregarGridSort(gridRequerimientosCliente);
		listaTipoPersona = llenarListaTipoPersona();
		tipoPersona = listaTipoPersona.get(1);
	}

	/**Interface: EventListener<SortEvent>*/
	@Override
	@NotifyChange("listaRequerimientos")
	public void onEvent(SortEvent event) throws Exception {	
		if(event.getTarget() instanceof Listheader){
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("fieldSort", ((Listheader) event.getTarget()).getValue().toString());
			parametros.put("sortDirection", event.isAscending());
			ejecutarGlobalCommand("cambiarRequerimientos", parametros );
		}
	}


	/**GLOBAL COMMAND*/
	@GlobalCommand
	@NotifyChange("listaRequerimientos")
	public void cambiarRequerimientos(@Default("0") @BindingParam("page") int page,
			@BindingParam("fieldSort") String fieldSort, 
			@BindingParam("sortDirection") Boolean sortDirection){
		String cedula = obtenerCedulaConTipoPersona();
		Map<String, Object> parametros = sTransaccion.consultarRequerimientosCliente(requerimientoFiltro,fieldSort, sortDirection, cedula, page, pageSize);
		Integer total = (Integer) parametros.get("total");
		listaRequerimientos = (List<Requerimiento>) parametros.get("requerimientos");
		gridRequerimientosCliente.setMultiple(true);
		pagRequerimientosCliente.setActivePage(page);
		pagRequerimientosCliente.setTotalSize(total);
	}
	
	/**
	 * Descripcion: Permite Ver la Oferta al cliente
	 * Parametros: Requerimiento: requerimiento @param view: formularioVerificarRequerimiento.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@GlobalCommand
	public void verOferta(@BindingParam("requerimiento") Requerimiento requerimiento,
			@BindingParam("detallesOfertas") List<DetalleOferta> detallesOfertas){

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("requerimiento", requerimiento);
		parametros.put("detallesOfertas", detallesOfertas);
		llamarFormulario("formularioOferta.zul", parametros);

	}

	/**
	 * Descripcion: Permite Ver el detalle del requerimiento
	 * Parametros: Requerimiento: requerimiento @param view: formularioVerificarRequerimiento.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void verDetalleRequerimiento(@BindingParam("requerimiento") Requerimiento requerimiento){
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("requerimiento", requerimiento);
		llamarFormulario("verDetalleRequerimiento.zul", parametros);

	}
	
	/**
	 * Descripcion: Permitira cambiar la paginacion de acuerdo a la pagina activa del Paging 
	 * Parametros: @param view: formularioVerificarRequerimiento.zul  
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange("*")
	public void paginarLista(){
		int page=pagRequerimientosCliente.getActivePage();
		cambiarRequerimientos(page, null, null);
	} 

	/**
	 * Descripcion: Permitira filtrar por los campos del proveedor
	 * Parametros: @param view: formularioVerificarRequerimiento.zul    
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange("*")
	public void aplicarFiltro()
	{
		if(fechaCreacion!=null)
			this.requerimientoFiltro.setFechaCreacion(new Timestamp(fechaCreacion.getTime()));
		else
			this.requerimientoFiltro.setFechaCreacion(null);
		cambiarRequerimientos(0, null, null);
	}

	/**
	 * Descripcion: Permitira obtener la cedula con tipo de persona
	 * Parametros: @param view: formularioVerificarRequerimiento.zul    
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	private String obtenerCedulaConTipoPersona(){
		String cedula = null;
		if(checkIsFormValid()){
			String tipo = (this.tipoPersona.getValor())?"J":"V";
			cedula = tipo+cliente.getCedula();
		}
		return cedula;
	}
	
	private void llamarFormulario(String ruta, Map<String, Object> parametros){
		crearModal(BasePackagePortal+ruta, parametros);
	}


	/**METODOS PROPIOS DE LA CLASE*/
	
	/**GETTERS Y SETTERS*/
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
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Requerimiento getRequerimientoFiltro() {
		return requerimientoFiltro;
	}

	public void setRequerimientoFiltro(Requerimiento requerimientoFiltro) {
		this.requerimientoFiltro = requerimientoFiltro;
	}

	public List<ModeloCombo<Boolean>> getListaTipoPersona() {
		return listaTipoPersona;
	}

	public void setListaTipoPersona(List<ModeloCombo<Boolean>> listaTipoPersona) {
		this.listaTipoPersona = listaTipoPersona;
	}

	public ModeloCombo<Boolean> getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(ModeloCombo<Boolean> tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
}
