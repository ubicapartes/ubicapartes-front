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
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.A;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.okiimport.app.model.DetalleOferta;
import com.okiimport.app.model.Oferta;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.model.enumerados.EEstatusOferta;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.transaccion.STransaccion;

public class VerOfertaViewModel extends AbstractRequerimientoViewModel {
	
	//Enum de la clase que contiene los enumerados del carrito de compra
	public enum Carrito {
		AGREGAR_CARRITO("fa fa-cart-arrow-down bigger-200 cyan", "Agregar respuesto al carrito"),
		QUITAR_CARRITO("fa fa-share bigger-200 cyan", "Quitar repuesto del carrito");
		
		private String css;
		private String tool;
		
		Carrito(String css, String tool){
			this.css=css;
			this.tool=tool;
		}

		public String getCss() {
			return css;
		}

		public String getTool() {
			return tool;
		}
	}
	
	//Servicios
    @BeanInjector("sTransaccion")
	private STransaccion sTransaccion;
    
    //GUI
    @Wire("#winOferta")
	private Window winOferta;
    
    //Variables Estaticas
    public static final Carrito AGREGAR_CARRITO = Carrito.AGREGAR_CARRITO;
    public static final Carrito QUITAR_CARRITO = Carrito.QUITAR_CARRITO;
	
    //Atributos
	private Requerimiento requerimiento;
	private List<Oferta> ofertas; //Ofertas Disponibles
	private List<DetalleOferta> listaDetOferta; //Carrito de Compra
    private Oferta oferta; //Oferta a Mostrar
    
    private boolean cerrar = false;
    private boolean visbOfertaAnterior = false, visbOfertaSiguiente = false;
    private int page, cantArticulos;
    private float totalArticulos;
    
    /**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: formularioOferta.zul 
	 * Retorno: Clase Inicializada 
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view, 
			@ExecutionArgParam("requerimiento") Requerimiento requerimiento)
	{	
		super.doAfterCompose(view);	
		//this.parametros = new HashMap<String, Object>();
		this.requerimiento = requerimiento;
		this.listaDetOferta = new ArrayList<DetalleOferta>(); //Veremos Luego si Se carga
		this.cantArticulos = this.listaDetOferta.size();
		this.ofertas = this.sTransaccion.consultarOfertasEnviadaPorRequerimiento(requerimiento.getIdRequerimiento());
		cargarOferta(0);
	}
	
	/**COMMAND*/
	/**
	 * Descripcion: Permite Aprobar el detalle de la oferta
	 * Parametros: @param a: componente de la vista A que fue presionado
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange({"oferta", "cantArticulos", "totalArticulos"})
	public void aprobarDetalleOferta(@ContextParam(ContextType.COMPONENT) A a,
			@BindingParam("detalleOferta") DetalleOferta detalleOferta)
	{
		boolean aprobado = (a.getSclass().equalsIgnoreCase(AGREGAR_CARRITO.getCss())) ? true : false;
		totalArticulos += (aprobado) ? detalleOferta.calcularPrecioVentaConverter() : -detalleOferta.calcularPrecioVentaConverter();
		detalleOferta.setAprobado(aprobado);
		if(aprobado)
			listaDetOferta.add(detalleOferta);
		else {
			int index = listaDetOferta.indexOf(detalleOferta);
			if(index!=-1)
				listaDetOferta.remove(index);
		}
		
		cantArticulos = listaDetOferta.size();
	}
	
	/**
	 * Descripcion: Permitira limpiar el campo aceptar de cada uno de los repuestos de la oferta
	 * Parametros: Ninguno
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange({"oferta", "cantArticulos"})
	public void limpiar(){
		for ( DetalleOferta detalleOferta : this.oferta.getDetalleOfertas() ){
			detalleOferta.setAprobado(null);
		}
		listaDetOferta.clear();
		cantArticulos = listaDetOferta.size();
	}
	
	@Command
	@NotifyChange({"oferta", "visbOfertaAnterior", "visbOfertaSiguiente"})
	public void mostrarOferta(@BindingParam("tipo") int tipo){
		page += (tipo == 2) ? 1 : -1; 
		this.cargarOferta(page);
	}
	
	/**
	 * Descripcion: Permite Registrar Una Oferta
	 * Parametros: @param btnEnviar: boton presionado
	 * Retorno: Oferta Registrada
	 * Nota: Ninguna
	 * */
	@Command
	public void enviar() {		
		if ( checkIsFormValid() ) {
			
			oferta.setEstatus(EEstatusOferta.RECIBIDA);
			llenarListAprobados();
			oferta = sTransaccion.actualizarOferta(oferta);
			//sTransaccion.actualizarRequerimiento(requerimiento);  Falta definir estatus
						
			if(listaDetOferta.size()>0){
				Map<String, Object> parametros  = cargarParametros();
				redireccionarASolicitudDePedido(parametros);
			}
			else
				reactivarRequerimiento();
		}
	}
	
	/**METODOS OVERRIDE*/
	/**
	 * Descripcion: Evento que se ejecuta al cerrar la ventana y que valida si el proceso actual de la compra se perdera o no
	 * Parametros: Ninguno
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@Override
	public void closeModal(){
		if(!cerrar){
			super.mostrarMensaje("Informaci\u00F3n", "Si cierra la ventana el proceso realizado se perdera, ¿Desea continuar?", null, 
					new Messagebox.Button[]{Messagebox.Button.YES, Messagebox.Button.NO}, new EventListener<Event>(){
				@Override
				public void onEvent(Event event) throws Exception {
					Messagebox.Button button = (Messagebox.Button) event.getData();
					if (button == Messagebox.Button.YES) {
						requerimiento.cerrarSolicitud();
						sTransaccion.actualizarRequerimiento(requerimiento);
						ejecutarGlobalCommand("cambiarRequerimientos", null);
						cerrarVentana();
					}
				}
			}, null);
		}
		else {
			ejecutarGlobalCommand("cambiarRequerimientos", null);
			super.closeModal();
		}
	}
	
	/**METODOS PRIVADOS DE LA CLASE*/	
	/**
	 * Descripcion: Permite Cargar La Oferta
	 * Parametros: @param view: formularioOferta.zul 
	 * Retorno: Oferta Cargada
	 * Nota: Ninguna
	 * */
	@NotifyChange("oferta")
	private void cargarOferta(int page){
		this.page = page;
		List<DetalleOferta> detallesOfertasEliminar = new ArrayList<DetalleOferta>();
		oferta = ofertas.get(page);
		oferta.recoveryCopyDetallesOfertas();
		if(!listaDetOferta.isEmpty()){
			oferta.copyDetallesOfertas();
			for(DetalleOferta detalle : oferta.getDetalleOfertas()){
				for(DetalleOferta detalleAprobado : listaDetOferta){
					if(detalle.getDetalleCotizacion().getDetalleRequerimiento().getIdDetalleRequerimiento()
							.equals(detalleAprobado.getDetalleCotizacion().getDetalleRequerimiento().getIdDetalleRequerimiento())
							&& !detalle.getIdDetalleOferta().equals(detalleAprobado.getIdDetalleOferta()))
						detallesOfertasEliminar.add(detalle);
				}
			}
		}
		oferta.removeAll(detallesOfertasEliminar);
		mostrarVisibiladOfertas(page);
	}
	
	@NotifyChange({"visbOfertaAnterior", "visbOfertaSiguiente"})
	private void mostrarVisibiladOfertas(int page) {
		int size = ofertas.size();
		this.visbOfertaAnterior = this.visbOfertaSiguiente = false;
		
		if(page==0 && size>1)
			this.visbOfertaSiguiente = true;
		else if(page > 0 && page+1!=size)
			this.visbOfertaAnterior = this.visbOfertaSiguiente = true;
		else if(page > 0 && page+1==size)
			this.visbOfertaAnterior = true;
	}

	/**
	 * Descripcion: Permite llenar la lista con las ofertas aprobadas eliminando archivos repetidos
	 * Parametros: Ninguno.
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	private void llenarListAprobados() {
		Set<DetalleOferta> listaDetOferta = new LinkedHashSet<DetalleOferta>(this.listaDetOferta);
		this.listaDetOferta.clear();
		this.listaDetOferta.addAll(listaDetOferta);
	}
	
	/**
	 * Descripcion: metodo que permitira cargar los parametros a enviar para la siguiente interfaz
	 * Parametros: Ninguno
	 * Retorno: @param parametros: lista de map que contendra los objetos a enviar a la otra interfaz
	 * Nota: Ninguna
	 * */
	private Map<String, Object> cargarParametros(){
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("requerimiento", requerimiento);
		parametros.put("detallesOfertas", listaDetOferta);
		return parametros;
	}
	
	/**
	 * Descripcion: Permitira redirigir a la pantalla de solicitud de pedido
	 * Parametros: @param parametros: parametros a pasar al .zul
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 */
	private void redireccionarASolicitudDePedido(final Map<String, Object> parametros){
		super.mostrarMensaje("Informaci\u00F3n", "¿Desea continuar con la compra?", null, 
				new Messagebox.Button[]{Messagebox.Button.YES, Messagebox.Button.NO}, new EventListener<Event>(){
			@Override
			public void onEvent(Event event) throws Exception {
				Messagebox.Button button = (Messagebox.Button) event.getData();
				if (button == Messagebox.Button.YES) {
					cerrarVentana();
					crearModal(BasePackagePortal+"formularioSolicituddePedido.zul", parametros);
				}
				else
					mostrarMensaje("Informaci\u00F3n", "El proceso realizado se perdera, ¿Desea continuar?", null, 
							new Messagebox.Button[]{Messagebox.Button.YES, Messagebox.Button.NO}, new EventListener<Event>(){
								@Override
								public void onEvent(Event event) throws Exception {
									Messagebox.Button button = (Messagebox.Button) event.getData();
									if (button == Messagebox.Button.YES)
										cerrarRequerimiento();
								}
					}, null);
			}
		}, null);
	}
	
	/**
	 * Descripcion: Permitira reactivar el requerimiento con otro analista
	 * Parametros: Ninguno.
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	private void reactivarRequerimiento(){
		super.mostrarMensaje("Informaci\u00F3n", "¿Desea que volvamos a reactivar su requerimiento?", null, 
				new Messagebox.Button[]{Messagebox.Button.YES, Messagebox.Button.NO, Messagebox.Button.CANCEL}, new EventListener<Event>(){
					@Override
					public void onEvent(Event event) throws Exception {
						Messagebox.Button button = (Messagebox.Button) event.getData();
						if (button == Messagebox.Button.YES) {
							ejecutarGlobalCommand("cambiarRequerimientos", null);
							sTransaccion.reactivarRequerimiento(requerimiento, sMaestros);
							cerrarVentana();
						}
						else if(button == Messagebox.Button.NO )
							cerrarRequerimiento();
					}
		}, null);
	}
	
	/**
	 * Descripcion: permitira cerrar el requerimiento asociado a la oferta.
	 * Parametros: Ninguno
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	private void cerrarRequerimiento(){
		requerimiento.cerrarSolicitud();
		sTransaccion.actualizarRequerimiento(requerimiento);
		sTransaccion.cerrarRequerimiento(requerimiento, sMaestros, sControlUsuario, false);
		mostrarMensaje("Informaci\u00F3n", "Requerimiento Cerrado!", null, null, new EventListener<Event>(){
			@Override
			public void onEvent(Event event) throws Exception {
				cerrarVentana();
			}	
		}, null);
	}
	
	/**
	 * Descripcion: metodo que actualiza la variable cerrar y llama al comman respectivo al cerrar la ventana.
	 * Parametros: Ninguno
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	private void cerrarVentana(){
		cerrar = true;
		closeModal();
	}
	
	/**GETTERS Y SETTERS*/
	public Carrito getAgregarCarrito() {
		return AGREGAR_CARRITO;
	}

	public Carrito getQuitarCarrito() {
		return QUITAR_CARRITO;
	}
	
	public STransaccion getsTransaccion() {
		return sTransaccion;
	}

	public void setsTransaccion(STransaccion sTransaccion) {
		this.sTransaccion = sTransaccion;
	}

	public Requerimiento getRequerimiento() {
		return requerimiento;
	}

	public void setRequerimiento(Requerimiento requerimiento) {
		this.requerimiento = requerimiento;
	}

	public Oferta getOferta() {
		return oferta;
	}

	public void setOferta(Oferta oferta) {
		this.oferta = oferta;
	}

	public int getCantArticulos() {
		return cantArticulos;
	}

	public void setCantArticulos(int cantArticulos) {
		this.cantArticulos = cantArticulos;
	}

	public boolean isVisbOfertaAnterior() {
		return visbOfertaAnterior;
	}

	public void setVisbOfertaAnterior(boolean visbOfertaAnterior) {
		this.visbOfertaAnterior = visbOfertaAnterior;
	}

	public boolean isVisbOfertaSiguiente() {
		return visbOfertaSiguiente;
	}

	public void setVisbOfertaSiguiente(boolean visbOfertaSiguiente) {
		this.visbOfertaSiguiente = visbOfertaSiguiente;
	}

	public float getTotalArticulos() {
		return totalArticulos;
	}

	public void setTotalArticulos(float totalArticulos) {
		this.totalArticulos = totalArticulos;
	}
}
