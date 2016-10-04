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
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SortEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;

import com.okiimport.app.model.Configuracion;
import com.okiimport.app.model.DetalleRequerimiento;
import com.okiimport.app.model.Oferta;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.model.enumerados.EEstatusCompra;
import com.okiimport.app.model.enumerados.EEstatusDetalleRequerimiento;
import com.okiimport.app.model.enumerados.EEstatusOferta;
import com.okiimport.app.model.enumerados.EEstatusRequerimiento;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.mail.MailCliente;
import com.okiimport.app.service.transaccion.STransaccion;

public class ListaOfertasClienteViewModel extends
		AbstractRequerimientoViewModel implements EventListener<SortEvent> {

	// Servicios
	@BeanInjector("sTransaccion")
	private STransaccion sTransaccion;
	
	@BeanInjector("mailCliente")
	private MailCliente mailCliente;

	// GUI
	@Wire("#gridOfertasCliente")
	private Listbox gridOfertasCliente;

	@Wire("#pagOfertasCliente")
	private Paging pagOfertasCliente;
	
	@Wire("#winListaOfertas")
	private Window winListaOfertas;

	// Atributos
	private List<Oferta> listaOfertas;
	private Requerimiento requerimiento;
	private String titulo = "Ofertas del Requerimiento Nro ";
	
	private boolean showButton = true;


	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: listaOfertasCliente.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("requerimiento") Requerimiento requerimiento) {
		super.doAfterCompose(view);
		this.requerimiento = requerimiento;
		
		this.titulo = this.titulo + requerimiento.getIdRequerimiento();
		agregarGridSort(gridOfertasCliente);
		pagOfertasCliente.setPageSize(pageSize);
		cambiarOfertas(0, null, null);
	}
	
	/**Interface: EventListener<SortEvent>*/
	@Override
	public void onEvent(SortEvent event) throws Exception {
		// TODO Auto-generated method stub
		if(event.getTarget() instanceof Listheader){
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("fieldSort", ((Listheader) event.getTarget()).getValue().toString());
			parametros.put("sortDirection", event.isAscending());
			ejecutarGlobalCommand("cambiarOfertas", parametros );
		}
	}

	/**GLOBAL COMMAND*/
	/**
	 * Descripcion: Llama a consultar las ofertas por requerimiento  
	 * Parametros: @param view: listaOfertasCliente.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@GlobalCommand
	@SuppressWarnings("unchecked")
	@NotifyChange("*")
	public void cambiarOfertas(@Default("0") @BindingParam("page") int page,
			@BindingParam("fieldSort") String fieldSort,
			@BindingParam("sortDirection") Boolean sortDirection) {
		Map<String, Object> parametros = sTransaccion.consultarOfertasPorRequerimiento(requerimiento.getIdRequerimiento(), fieldSort, sortDirection, page, pageSize);
		Integer total = (Integer) parametros.get("total");
		listaOfertas = (List<Oferta>) parametros.get("ofertas");
		recorrerListadoOfertas();
		pagOfertasCliente.setActivePage(page);
		pagOfertasCliente.setTotalSize(total);
	}

	/** COMMAND */
	/**
	 * Descripcion: Permitira cambiar la paginacion de acuerdo a la pagina activa del Paging 
	 * Parametros: @param view: listaOfertasCliente.zul  
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange("*")
	public void paginarLista() {
		int page = pagOfertasCliente.getActivePage();
		cambiarOfertas(page, null, null);
	}
	
	/**
	 * Descripcion: Llama a un modal para ver los datos de la oferta
	 * Parametros: Oferta @param view: listaOfertasCliente.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange("*")
	public void verOferta(@BindingParam("oferta") Oferta oferta){
		Map<String, Object> parametros = new HashMap<String, Object>();
		oferta.setDetalleOfertas(this.sTransaccion.consultarDetallesOferta(oferta));
		parametros.put("oferta", oferta);
		parametros.put("requerimiento", this.requerimiento);
		
		crearModal(BasePackageSistemaFunc+"ofertados/verDetalleOferta.zul", parametros);
	}

	/**
	 * Descripcion: Llama a enviar las ofertas al cliente
	 * Parametros: @param view: listaOfertasCliente.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void enviarCliente(){
		Configuracion configuracion = sControlConfiguracion.consultarConfiguracionActual();
		for(Oferta oferta : listaOfertas ){
			if(oferta.getEstatus().equals(EEstatusOferta.SELECCIONADA)){
				oferta.setPorctIva(configuracion.getPorctIva());
				oferta.setPorctGanancia(configuracion.getPorctGanancia());
				oferta.setEstatus(EEstatusOferta.ENVIADA);
				sTransaccion.actualizarOferta(oferta);
			}
		}
		requerimiento.setEstatus(EEstatusRequerimiento.OFERTADO);
		sTransaccion.actualizarRequerimiento(requerimiento);
		cambiarRequerimientos();
		//No es el servicio que se usara
		//Aqui se colocara el servicio para enviar el correo enviarOfertas.html 
		//Miguel
		mailCliente.enviarOfertas(requerimiento, mailService);
		
		winListaOfertas.detach();
		
		mostrarMensaje("Informaci\u00F3n", "Ofertas Enviadas al Cliente", null, null, null, null);

	}
	
	/**
	 * Descripcion: Llama a ejecutar globalCommand
	 * Parametros: @param view: listaOfertasCliente.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void cambiarRequerimientos(){
		ejecutarGlobalCommand("cambiarRequerimientos", null);
	}

	
	/**
	 * Descripcion: Llama a enviar a Cliente
	 * Parametros: @param view: listaOfertasCliente.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	public boolean enviarACliente(){
		boolean enviar = false;
		for(Oferta oferta : listaOfertas)
			if(oferta.enviar()){
				enviar = true;
				break;
			}
		return enviar;
	}
	
	private void recorrerListadoOfertas(){
		for(Oferta oferta: this.listaOfertas){
			if(oferta.getEstatus().equals(EEstatusOferta.ENVIADA)){
				setShowButton(false);
				break;
			}
		}
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	
	/**METODOS GETTERS AND SETTERS*/
	
	public STransaccion getsTransaccion() {
		return sTransaccion;
	}

	public void setsTransaccion(STransaccion sTransaccion) {
		this.sTransaccion = sTransaccion;
	}

	public MailCliente getMailCliente() {
		return mailCliente;
	}

	public void setMailCliente(MailCliente mailCliente) {
		this.mailCliente = mailCliente;
	}

	public List<Oferta> getListaOfertas() {
		return listaOfertas;
	}

	public void setListaOfertas(List<Oferta> listaOfertas) {
		this.listaOfertas = listaOfertas;
	}

	public Requerimiento getRequerimiento() {
		return requerimiento;
	}

	public void setRequerimiento(Requerimiento requerimiento) {
		this.requerimiento = requerimiento;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public boolean isShowButton() {
		return showButton;
	}

	public void setShowButton(boolean showButton) {
		this.showButton = showButton;
	}
}
