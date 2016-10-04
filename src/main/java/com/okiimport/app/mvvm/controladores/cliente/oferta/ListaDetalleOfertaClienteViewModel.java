package com.okiimport.app.mvvm.controladores.cliente.oferta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
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
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.SortEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;

import com.okiimport.app.model.DetalleCotizacion;
import com.okiimport.app.model.DetalleOferta;
import com.okiimport.app.model.Oferta;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.mail.MailCliente;
import com.okiimport.app.service.transaccion.STransaccion;

public class ListaDetalleOfertaClienteViewModel extends
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
		private List<DetalleCotizacion> listaDetalleCotizaciones;
		private List<Oferta> listaOferta;
		private List<DetalleOferta> listaDetalleOfertas;
		private Requerimiento requerimiento;
		private String titulo = "Ofertas del Requerimiento N� ";

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
			this.listaDetalleCotizaciones=sTransaccion.consultarDetalleContizacionEmitidosPorRequerimiento(requerimiento.getIdRequerimiento());
			
			
			this.titulo = this.titulo + requerimiento.getIdRequerimiento();
			obtenerOfertas(this.requerimiento.getIdRequerimiento());
			agregarGridSort(gridOfertasCliente);
			pagOfertasCliente.setPageSize(pageSize);
			//cambiarOfertas(0, null, null);
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

		@NotifyChange("*")
		private void obtenerOfertas(Integer idRequerimiento) {
			this.listaOferta=sTransaccion.consultarOfertasEnviadaPorRequerimiento(idRequerimiento);
		    if(listaOferta!=null && !listaOferta.isEmpty()){
		    	int i=0;
		    	this.listaDetalleOfertas= new ArrayList<DetalleOferta>();
		    	for(Oferta oferta : listaOferta){
		    		i++;
		    		//System.out.println("oferta --------- "+oferta.getIdOferta());
		    		//System.out.println("primer detalle de la oferta : "+oferta.getDetalleOfertas().get(0).getIdDetalleOferta()+" del detalle cotizacion numero: "+oferta.getDetalleOfertas().get(0).getDetalleCotizacion().getIdDetalleCotizacion());
		    		this.listaDetalleOfertas.addAll(oferta.getDetalleOfertas());
		    		if (i==3) break;
				}
		    	
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
		@NotifyChange("listaOferta")
		public void cambiarOfertas(@Default("0") @BindingParam("page") int page,
				@BindingParam("fieldSort") String fieldSort,
				@BindingParam("sortDirection") Boolean sortDirection) {
			Map<String, Object> parametros = sTransaccion.consultarOfertasPorRequerimiento(requerimiento.getIdRequerimiento(), fieldSort, sortDirection, page, pageSize);
			Integer total = (Integer) parametros.get("total");
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
		 * Descripcion: Actualiza el registro de detalle de la cotizacion, cambiando su estatusFavorito a true
		 * Parametros: DetalleCotizacion detalleCotizacion
		 * Retorno: Ninguno
		 * Nota: Ninguna
		 * */
		@Command
		@NotifyChange("*")
		public void agregarFavorito(@BindingParam("detalle") DetalleOferta detalle){
			try{
				
				if(detalle.getCantidadSeleccionada()!=0 && detalle.getCantidadSeleccionada()<=detalle.getDetalleCotizacion().getCantidad()){
						detalle.setEstatusFavorito(true);
						sTransaccion.actualizarDetallesOferta(detalle);
						refrescarCarrito();
				}
				else{
					if(detalle.getCantidadSeleccionada()==0)
						mostrarMensaje("Advertencia", "La cantidad seleccionada debe ser mayor que cero", null, null, null, null);
					else mostrarMensaje("Advertencia", "La cantidad seleccionada es superior a la cantidad disponible", null, null, null, null);
					} 
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
		}
		
		/**
		 * Descripcion: Actualiza el registro de detalle de la cotizacion, cambiando su estatusFavorito a false
		 * Parametros: DetalleCotizacion detalleCotizacion
		 * Retorno: Ninguno
		 * Nota: Ninguna
		 * */
		@Command
		@NotifyChange("*")
		public void quitarFavorito(@BindingParam("detalle") DetalleOferta detalle){
			try{
				detalle.setEstatusFavorito(false);
				detalle.setCantidadSeleccionada((long) 0);
				sTransaccion.actualizarDetallesOferta(detalle);
				refrescarCarrito();
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
		}
		
		private void refrescarCarrito(){
		    BindUtils.postGlobalCommand("perfil", EventQueues.APPLICATION, "getSizeShoppingCar", null);
		}
		
		@Command
		@NotifyChange("*")
		public void refrescar(){
			return;
		}
		
		/**
		 * Descripcion: Actualiza el registro de detalle de la cotizacion, cambiando su estatusFavorito a false
		 * Parametros: DetalleCotizacion detalleCotizacion
		 * Retorno: Ninguno
		 * Nota: Ninguna
		 * */
		@Command
		@NotifyChange("*")
		public void cerrar(){
			try{
				winListaOfertas.onClose();
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
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

		

		public List<DetalleCotizacion> getListaDetalleCotizaciones() {
			return listaDetalleCotizaciones;
		}

		public void setListaDetalleCotizaciones(
				List<DetalleCotizacion> listaDetalleCotizaciones) {
			this.listaDetalleCotizaciones = listaDetalleCotizaciones;
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

		public List<Oferta> getListaOferta() {
			return listaOferta;
		}

		public void setListaOferta(List<Oferta> listaOferta) {
			this.listaOferta = listaOferta;
		}

		public List<DetalleOferta> getListaDetalleOfertas() {
			return listaDetalleOfertas;
		}

		public void setListaDetalleOfertas(List<DetalleOferta> listaDetalleOfertas) {
			this.listaDetalleOfertas = listaDetalleOfertas;
		}
		

}
