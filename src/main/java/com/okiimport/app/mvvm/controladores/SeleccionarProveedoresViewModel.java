package com.okiimport.app.mvvm.controladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Default;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.event.SortEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;

import com.okiimport.app.model.Cotizacion;
import com.okiimport.app.model.DetalleCotizacion;
import com.okiimport.app.model.DetalleCotizacionInternacional;
import com.okiimport.app.model.DetalleRequerimiento;
import com.okiimport.app.model.Proveedor;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.constraint.CustomConstraint;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.mail.MailProveedor;
import com.okiimport.app.service.transaccion.STransaccion;

public class SeleccionarProveedoresViewModel extends AbstractRequerimientoViewModel implements EventListener<SortEvent> {
	
	//Servicios	
	@BeanInjector("sTransaccion")
	private STransaccion sTransaccion;
	
	@BeanInjector("mailProveedor")
	private MailProveedor mailProveedor;
	
	//GUI
	@Wire("#winListProveedores")
	private Window winListProveedores;
	
	@Wire("#pagProveedores")
	private Paging pagProveedores;
	
	@Wire("#gridProveedores")
	private Listbox gridProveedores;
		
	//Atributos
	private CustomConstraint constraintMensaje;
	
	private List<Proveedor> proveedores;
	private List<Proveedor> proveedoresSeleccionados;
	private Set<Proveedor> proveedoresSeleccionadosARegistrar;
	private List<DetalleRequerimiento> listaDetalleRequerimientos;
	private List <Integer> idsClasificacionRepuesto;
	
	private Proveedor proveedor;
	private Cotizacion cotizacion;
	private Requerimiento requerimiento;
	
	private Boolean enviar;

	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: seleccionarProveedores.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("enviar") Boolean enviar,
			@ExecutionArgParam("repuestosseleccionados") List <DetalleRequerimiento> repuestosseleccionados){
		super.doAfterCompose(view);
		this.enviar = enviar;
		this.cotizacion = new Cotizacion("Estimado Proveedor le hacemos el envio de un nuevo requerimiento para su posterior revisi\u00F3n ", enviar);
		this.listaDetalleRequerimientos = repuestosseleccionados;
		this.proveedoresSeleccionadosARegistrar = new LinkedHashSet<Proveedor>();
		this.constraintMensaje = (this.enviar) ? this.getNotEmptyValidator() : null;
		
		agregarGridSort(gridProveedores);
		limpiar();
		pagProveedores.setPageSize(pageSize=5);
		
		idsClasificacionRepuesto = new ArrayList<Integer>();
		for(DetalleRequerimiento detalle:repuestosseleccionados){
			requerimiento = detalle.getRequerimiento();
			idsClasificacionRepuesto.add(detalle.getClasificacionRepuesto().getIdClasificacionRepuesto());
		}
		
		consultarProveedores(0, null, null);
	}
	
	/**Interface: EventListener<SortEvent>*/
	@Override
	@NotifyChange({"proveedores"})
	public void onEvent(SortEvent event) throws Exception {
		if(event.getTarget() instanceof Listheader){
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("fieldSort", ((Listheader) event.getTarget()).getValue().toString());
			parametros.put("sortDirection", event.isAscending());
			ejecutarGlobalCommand("consultarProveedores", parametros );
		}
		
	}	
	/**GLOBAL COMMAND*/
	
	/**
	 * Descripcion: Permite consultar los proveedores existentes en la base de datos
	 * Parametros: @param view: seleccionarProveedores.zul
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@GlobalCommand
	@NotifyChange({"proveedores"})
	public void consultarProveedores(@Default("0") int page,
			@BindingParam("fieldSort") String fieldSort, 
			@BindingParam("sortDirection") Boolean sortDirection){
		agregarProveedoresSeleccionados();
		Map<String, Object> Parametros= sMaestros.ConsultarProveedoresListaClasificacionRepuesto(proveedor, fieldSort, sortDirection, requerimiento.getIdRequerimiento(), idsClasificacionRepuesto,page, pageSize);
		proveedores = (List<Proveedor>) Parametros.get("proveedores");
		Integer total = (Integer) Parametros.get("total");
		gridProveedores.setMultiple(true);
		gridProveedores.setCheckmark(true);
		pagProveedores.setActivePage(page);
		pagProveedores.setTotalSize(total);
		
	}
	
	/**COMMAND*/
	/**
	 * Descripcion: Permite limpiar los campos del formulario Seleccionar Proveedores
	 * Parametros: @param view: seleccionarProveedores.zul
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange({"proveedor"})
	public void limpiar(){
		proveedor = new Proveedor();
		super.cleanConstraintForm();
	}
	
	/**
	 * Descripcion: Cambia la paginacion de acuerdo a la pagina activa
	 * de Paging pagProveedores
	 * Parametros: @param view: seleccionarProveedores.zul
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange({"proveedores"})
	public void paginarLista(){
		consultarProveedores(pagProveedores.getActivePage(), null, null);
	}
	
	/**
	 * Descripcion: Permite filtrar por los diferentes campos del formulario segun solicite el evento
	 * Parametros: Ninguno
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange({"proveedores"})
	public void aplicarFiltro(){
		consultarProveedores(0, null, null);
	}

	/**
	 * Descripcion: Permite capturar el evento al seleccionar un proveedor y verifica si se encuentra
	 * dentro de la lista de seleccionados, si no esta lo elimina de los proveedores 
	 * a enviar la solicitud de cotizacion.
	 * Parametros: @param selectEvent: evento de seleccion
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void seleccionarProveedor(@ContextParam(ContextType.TRIGGER_EVENT) SelectEvent<Listitem, Proveedor> selectEvent){
		Listitem item = (Listitem) selectEvent.getReference();
		if(item!=null){
			Proveedor proveedor = proveedores.get(item.getIndex());
			if(proveedoresSeleccionados!=null && !proveedoresSeleccionados.contains(proveedor))
				this.proveedoresSeleccionadosARegistrar.remove(proveedor);
		}
	}
	
	/**
	 * Descripcion: Permite capturar el evento al seleccionar todos los proveedores y verifica si se encuentran
	 * dentro de la lista de seleccionados, si no estan los elimina de los proveedores 
	 * a enviar la solicitud de cotizacion.
	 * Parametros: @param checkEvent: evento de seleccion
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void seleccionarProveedores(){
		if(proveedoresSeleccionados==null)
			proveedoresSeleccionadosARegistrar.removeAll(proveedores);
	}
	
	/**
	 * Descripcion: Permite registrar un proveedor llamando al modal formularioProveedor
	 * Parametros: Ninguno
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void registrarProveedor(){
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("cerrar", false);
		crearModal(BasePackageSistemaMaest+"formularioProveedor.zul", parametros);
	}
		
	/**
	 * Descripcion: Permite enviar la solicitu de cotizacion a los proveedores
	 * Parametros: Ninguno
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void enviar(@BindingParam("btnEnviar") Button btnEnviar, @BindingParam("btnLimpiar") Button btnLimpiar){
		agregarProveedoresSeleccionados();
		if(!this.proveedoresSeleccionadosARegistrar.isEmpty()){
			btnEnviar.setDisabled(true);
			
			Cotizacion cotizacion = null;
			List<DetalleCotizacion> detalleCotizacions = null;
			for(Proveedor proveedor : proveedoresSeleccionadosARegistrar){

				cotizacion =  this.cotizacion.clon();
				cotizacion.setProveedor(proveedor);
				detalleCotizacions = new ArrayList<DetalleCotizacion>();

				for(DetalleRequerimiento detalleRequerimiento : listaDetalleRequerimientos){
					DetalleCotizacion detalleCotizacion = (proveedor.getTipoProveedor()) ? new DetalleCotizacion() : new DetalleCotizacionInternacional();
					detalleCotizacion.setDetalleRequerimiento(detalleRequerimiento);
					detalleCotizacions.add(detalleCotizacion);
				}
				sTransaccion.registrarSolicitudCotizacion(cotizacion, detalleCotizacions);

				if(enviar)
					this.mailProveedor.enviarRequerimientoProveedor(proveedor, requerimiento, detalleCotizacions, mailService);
			}
			
			mostrarMensaje("Informaci\u00F3n", "Cotizaci\u00F3n enviada Exitosamente ", null, null, null, null);
			closeModal();
		}
		else
			mostrarMensaje("Informaci\u00F3n", "Seleccione al menos un Proveedor ", null, null, null, null);

	}
	
	/**METODOS OVERRIDE*/
	@Command
	@Override
	public void closeModal() {
		super.closeModal();
		ejecutarGlobalCommand("removerSeleccionados", null);
	}
	
    /**METODOS PROPIOS DE LA CLASE*/
	private void agregarProveedoresSeleccionados(){
		if(proveedoresSeleccionados!=null)
			proveedoresSeleccionadosARegistrar.addAll(proveedoresSeleccionados);
	}

	/**GETTERS Y SETTERS*/
	public STransaccion getsTransaccion() {
		return sTransaccion;
	}

	public void setsTransaccion(STransaccion sTransaccion) {
		this.sTransaccion = sTransaccion;
	}
	
	public MailProveedor getMailProveedor() {
		return mailProveedor;
	}

	public void setMailProveedor(MailProveedor mailProveedor) {
		this.mailProveedor = mailProveedor;
	}
	
	public CustomConstraint getConstraintMensaje() {
		return constraintMensaje;
	}

	public void setConstraintMensaje(CustomConstraint constraintMensaje) {
		this.constraintMensaje = constraintMensaje;
	}

	public List<Proveedor> getProveedores() {
		return proveedores;
	}

	public void setProveedores(List<Proveedor> proveedores) {
		this.proveedores = proveedores;
	}

	public List<Proveedor> getProveedoresSeleccionados() {
		return proveedoresSeleccionados;
	}

	public void setProveedoresSeleccionados(List<Proveedor> proveedoresSeleccionados) {
		this.proveedoresSeleccionados = proveedoresSeleccionados;
	}
	
	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}
	
	public Cotizacion getCotizacion() {
		return cotizacion;
	}

	public void setCotizacion(Cotizacion cotizacion) {
		this.cotizacion = cotizacion;
	}

	public Boolean getEnviar() {
		return enviar;
	}

	public void setEnviar(Boolean enviar) {
		this.enviar = enviar;
	}	
}
