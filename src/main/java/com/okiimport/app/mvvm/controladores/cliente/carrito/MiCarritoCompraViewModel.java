package com.okiimport.app.mvvm.controladores.cliente.carrito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.security.core.userdetails.UserDetails;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.okiimport.app.model.Compra;
import com.okiimport.app.model.DetalleOferta;
import com.okiimport.app.model.HistoricoMoneda;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.model.Usuario;
import com.okiimport.app.model.enumerados.EEstatusCompra;
import com.okiimport.app.model.enumerados.EEstatusRequerimiento;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.mail.MailCliente;
import com.okiimport.app.service.transaccion.STransaccion;

public class MiCarritoCompraViewModel extends
AbstractRequerimientoViewModel {
	
	// Servicios
		@BeanInjector("sTransaccion")
		private STransaccion sTransaccion;
		
		@BeanInjector("mailCliente")
		private MailCliente mailCliente;

		// GUI
		@Wire("#gridOfertasCliente")
		private Listbox gridOfertasCliente;


		@Wire("#winCarrito")
		private Window winCarrito;
		
		// Atributos
		private List<DetalleOferta> listaDetalleOfertas;
		
		private Integer idCliente = -1;
		
		private Double totalCarrito = 0.0;
		
		private HistoricoMoneda historicoMoneda;
		
		private Map<Integer, Compra> hash_compras_requerimiento = new HashMap<Integer, Compra>();
		
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
			UserDetails user = super.getUser();
			if(user!=null){
				Usuario usuario = sControlUsuario.consultarUsuario(user.getUsername(), user.getPassword(), null);
				setIdCliente(usuario.getPersona().getId());
				llenarCarrito();
			}
		}		

	
		@NotifyChange("*")
		private void llenarCarrito() {
			this.listaDetalleOfertas=sTransaccion.consultarDetallesOfertaInShoppingCar(getIdCliente());
			refrescarCarrito();
		}

		@NotifyChange("*")
		private void refrescarCarrito(){
		    BindUtils.postGlobalCommand("perfil", EventQueues.APPLICATION, "getSizeShoppingCar", null);
		    setTotalCarrito(refrescarMontoTotalCarrito());
		}
		
		@NotifyChange("*")
		private Double refrescarMontoTotalCarrito(){
			Double total =0.00;
			for(DetalleOferta detalle: this.listaDetalleOfertas){
				total+=detalle.calcularPrecioVentaUnit() * detalle.getCantidadSeleccionada();
				setHistoricoMoneda(detalle.getDetalleCotizacion().getCotizacion().getHistoricoMoneda());
			}
			return total;
		}
		
		public HistoricoMoneda getHistoricoMoneda(){
			if (this.historicoMoneda==null){
				this.historicoMoneda = new HistoricoMoneda();
				this.historicoMoneda.setMontoConversion((float) 0);
				this.historicoMoneda.convert(0);
			}
			return historicoMoneda;
		}
		
		public void setHistoricoMoneda(HistoricoMoneda historicoMoneda){
			this.historicoMoneda= historicoMoneda;
		}
		
		
		/**
		 * Descripcion: Actualiza el registro de detalle de la cotizacion, cambiando su estatusFavorito a false
		 * Parametros: DetalleCotizacion detalleCotizacion
		 * Retorno: Ninguno
		 * Nota: Ninguna
		 * */
		@Command
		@NotifyChange("*")
		public void eliminarElemento(@BindingParam("detalle") DetalleOferta detalle){
			try{
				detalle.setEstatusFavorito(false);
				detalle.setCantidadSeleccionada((long) 0);
				sTransaccion.actualizarDetallesOferta(detalle);
				llenarCarrito();
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
		}
		
		
		@Command
		@NotifyChange("*")
		public void procesarCompra(){
			if(registarCompra()){
				mostrarMensaje("Compra", "Su compra fue procesada correctamente, por favor dirijase a una de nuestras oficinas para consignar el pago en efectivo.", null,null,null,null);
				enviarMail();
			} else {
				mostrarMensaje("Compra", "ERROR PROCESANDO SU COMPRA FAVOR COMUNICARSE CON ADMINISTRADOR.", Messagebox.ERROR,null,null,null);			
			}
				
		}
		
		@Command
		@NotifyChange("*")
		public void enviarMail(){
			try{
				for(Entry<Integer, Compra> entry : hash_compras_requerimiento.entrySet()) {
				    Compra compra = entry.getValue();
				    mailCliente.enviarInformacionCompra(compra, mailService);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
				
		}
		
		
		@Command
		@NotifyChange("*")
		public Boolean registarCompra(){
			Boolean respuesta = true;
			try{
				float total = 0;
				
				for(DetalleOferta detalle: this.listaDetalleOfertas){
					Requerimiento req = detalle.getDetalleCotizacion().getDetalleRequerimiento().getRequerimiento();
					Compra compra;
					if(!hash_compras_requerimiento.containsKey(req.getIdRequerimiento())){
						
						//gestionar requerimiento
						req.setEstatus(EEstatusRequerimiento.COMPRADO);
						req =  sTransaccion.actualizarRequerimiento(req);
						
						//gestionar compra
						compra = new Compra();
						compra.setRequerimiento(req);
						compra.setEstatus(EEstatusCompra.EN_ESPERA);
						compra.setPrecioVenta(detalle.calcularPrecioVentaUnit() * detalle.getCantidadSeleccionada());
						
						compra.addDetalleOferta(detalle);
						compra = sTransaccion.registrarOActualizarCompra(compra);
						
						System.out.println("lo que tiene detalle oferta en precio es "+ detalle.calcularPrecioVentaUnit());
						System.out.println("la cantidad seleccionada es: " + detalle.getCantidadSeleccionada());
						System.out.println("compra id:"+ compra.getIdCompra() +" lo que tiene precioVenta: "+compra.getPrecioVenta());
						
						//gestionar detalle oferta
						detalle.setEstatusFavorito(false);
						sTransaccion.actualizarDetallesOferta(detalle);
						
						//agrego a la lista de compras para luego enviar email
						hash_compras_requerimiento.put(req.getIdRequerimiento(), compra);
						
						
					} else {
						compra = hash_compras_requerimiento.get(req.getIdRequerimiento());
						total = compra.getPrecioVenta();
						total+= (detalle.calcularPrecioVentaUnit() * detalle.getCantidadSeleccionada());
						compra.setPrecioVenta(total);
						compra.addDetalleOferta(detalle);
						compra = sTransaccion.registrarOActualizarCompra(compra);
						
						System.out.println("lo que tiene detalle oferta en precio es "+ detalle.calcularPrecioVentaUnit());
						System.out.println("la cantidad seleccionada es: " + detalle.getCantidadSeleccionada());
						System.out.println("compra id:"+ compra.getIdCompra() +" lo que tiene precioVenta: "+compra.getPrecioVenta());
						
						
						//gestionar detalle oferta
						detalle.setEstatusFavorito(false);
						sTransaccion.actualizarDetallesOferta(detalle);
						
						//agrego a la lista de compras para luego enviar email					
						hash_compras_requerimiento.put(req.getIdRequerimiento(), compra);
					}
					
					
				}
				llenarCarrito();
			}catch(Exception e){
				respuesta = false;
			}
			return respuesta;
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
				winCarrito.onClose();
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

		public List<DetalleOferta> getListaDetalleOfertas() {
			return listaDetalleOfertas;
		}

		public void setListaDetalleOfertas(List<DetalleOferta> listaDetalleOfertas) {
			this.listaDetalleOfertas = listaDetalleOfertas;
		}
		
		public Integer getIdCliente() {
			return idCliente;
		}


		public void setIdCliente(Integer idCliente) {
			this.idCliente = idCliente;
		}
		

		public Double getTotalCarrito() {
			return totalCarrito;
		}


		public void setTotalCarrito(Double totalCarrito) {
			this.totalCarrito = totalCarrito;
		}

}
