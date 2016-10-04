package com.okiimport.app.mvvm.controladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;

import com.okiimport.app.model.Configuracion;
import com.okiimport.app.model.Cotizacion;
import com.okiimport.app.model.DetalleCotizacion;
import com.okiimport.app.model.DetalleOferta;
import com.okiimport.app.model.DetalleRequerimiento;
import com.okiimport.app.model.Oferta;
import com.okiimport.app.model.Proveedor;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.model.enumerados.EEstatusOferta;
import com.okiimport.app.model.enumerados.EEstatusRequerimiento;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.mvvm.resource.MessageboxEventListener;
import com.okiimport.app.mvvm.resource.decorator.ofertas.DecoratorTabOferta;
import com.okiimport.app.mvvm.resource.estrategia.detalles_cotizacion.ResolveEstrategiaSortDetalleCotizacion;
import com.okiimport.app.service.mail.MailCliente;
import com.okiimport.app.service.mail.MailProveedor;
import com.okiimport.app.service.transaccion.STransaccion;
import com.okiimport.app.service.web.SLocalizacion;

@SuppressWarnings("rawtypes")
public class ListaCreacionOfertasViewModel extends AbstractRequerimientoViewModel
	implements DecoratorTabOferta.OnComunicatorOfertaListener, MessageboxEventListener.OnComunicatorListener {
	
	// Servicios
	@BeanInjector("sTransaccion")
	private STransaccion sTransaccion;
	
	@BeanInjector("sLocalizacion")
	private SLocalizacion sLocalizacion;
	
	@BeanInjector("mailCliente")
	private MailCliente mailCliente;
	
	
	@BeanInjector("mailProveedor")
	private MailProveedor mailProveedor;
	
	//Atributos
	private ResolveEstrategiaSortDetalleCotizacion resolve;
	private Map<DetalleRequerimiento, List<DetalleCotizacion>> listasDetalleCotizacion;
	private List<DetalleOferta> detallesOfertas;
	private List<Oferta> ofertas;
	private Requerimiento requerimiento;
	
	private boolean guardar;
	
	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: listaCreacionOfertas.zul
	 * @param requerimiento: requerimiento seleccionado para la creacion de oferta
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("requerimiento") Requerimiento requerimiento) {
		super.doAfterCompose(view);
		this.guardar = false;
		this.requerimiento = requerimiento;
		this.resolve = new ResolveEstrategiaSortDetalleCotizacion(sLocalizacion);
		this.ofertas = sTransaccion.consultarOfertasNoEnviada(requerimiento);
		crearOfertas();
	}
	
	/** Interface: DecoratorTabOferta.OnComunicatorOfertaListener */
	@Override
	public void registrarRecotizacion(Oferta oferta) {
		List<DetalleCotizacion> detalles = oferta.getDetallesCotizacionParaRecotizacion(false);
		Proveedor proveedor = detalles.get(0).getCotizacion().getProveedor();
		Cotizacion cotizacion = sTransaccion.registrarRecotizacion(requerimiento, proveedor, detalles);
		oferta.setCotizacion(cotizacion);
		oferta.setEstatus(EEstatusOferta.RECHAZADA);
		sTransaccion.actualizarOferta(oferta);
		this.mailProveedor.enviarRecotizacionProveedor(proveedor, requerimiento, cotizacion, cotizacion.getDetalleCotizacions(), mailService);
	}
	
	@Override
	public void mostrarMensajeInvalidaRecotizacion(DecoratorTabOferta decorator, Oferta oferta) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("decorator", decorator);
		params.put("oferta", oferta);
		this.mostrarMensaje("Informacion", "Algunos articulos no se les ha aprobado, se tomaran como articulos rechazados ¿desea continuar con la recotizacion?", 
				Messagebox.EXCLAMATION, new Messagebox.Button[]{ Messagebox.Button.YES, Messagebox.Button.NO, Messagebox.Button.CANCEL }, 
				new MessageboxEventListener(this, params), null);
	}
	
	/** Interface: MessageboxEventListener.OnComunicatorListener */
	@Override
	public void onEvent(Event event, Map<String, Object> params) {
		Messagebox.Button button = (Messagebox.Button) event.getData();
		if (button == Messagebox.Button.YES) {
			Oferta oferta = (Oferta) params.get("oferta");
			registrarRecotizacion(oferta);
		}
	}

	
	/**COMMAND*/
	@Command
	public void cancelar(){
		//Verificar luego
		closeModal();
	}
	
	@Command
	public void enviarCliente(){
		if(validarOfertas()){
			if(guardar=guardarOfertas(true)){
				this.requerimiento.setEstatus(EEstatusRequerimiento.OFERTADO);
				this.sTransaccion.actualizarRequerimiento(requerimiento);
				this.sTransaccion.actualizarDetallesRequerimiento(requerimiento);
				this.mailCliente.enviarOfertas(requerimiento, mailService);
			}
			closeModal();
		}
	}
	
	@Command
	public void guardarEnviarLuego(){
		if(validarOfertas()){
			guardar=guardarOfertas(false);
			closeModal();
		}
	}
	
	/**METODOS OVERRIDE*/
	@Override
	@Command
	public void closeModal(){
		super.closeModal();
		if(guardar){
			mostrarMensaje("Informacion", "Las ofertas fueron guardadas con exito", null, null, null, null);
			ejecutarGlobalCommand("cambiarRequerimientos", null);
		}
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	@SuppressWarnings("unchecked")
	private void crearOfertas() {
		Integer cantOfertas = ofertas.size();
		int pos = cantOfertas + 1; //rectificar calculo con los estatus
		Oferta oferta;
		List<DetalleCotizacion> detallesCotizacion;
		List<DetalleRequerimiento> keys = new ArrayList<DetalleRequerimiento>();
		Configuracion configuracion = sControlConfiguracion.consultarConfiguracionActual();
		
		if(ofertas==null || ofertas.isEmpty())
			ofertas = new ArrayList<Oferta>();
		
		listasDetalleCotizacion = sTransaccion.consultarDetallesCotizacionEmitidos(requerimiento.getIdRequerimiento());
		
		while(cantOfertas<3 && pos<=6 && !listasDetalleCotizacion.isEmpty()){
			//1. Creamos una oferta nueva
			oferta = new Oferta(ofertas.size()+1, configuracion.getPorctIva(), configuracion.getPorctGanancia(), false);
			
			//2. Actualizamos el ordenamiento de cada una de las listas
			for(DetalleRequerimiento key: listasDetalleCotizacion.keySet()){
				detallesCotizacion = listasDetalleCotizacion.get(key);
				this.resolve.resolveEstrategia(detallesCotizacion, pos);
				
				//2.1 Agregamos la mejor opcion a la oferta respectiva
				if(detallesCotizacion.size()>0){
					oferta.addDetalleOferta(new DetalleOferta(detallesCotizacion.get(0)));
					
					//2.1.1 Removemos la mejor oferta de la lista
					detallesCotizacion.remove(0);
					
					//2.1.2 Verificamos el vacio de la lista y Actualizamos
					if(detallesCotizacion.isEmpty())
						keys.add(key);
				}
			}
			
			//3. Eliminamos los keys del map
			if(!keys.isEmpty())
				for(DetalleRequerimiento key:keys)
					listasDetalleCotizacion.remove(key);
			
			//4. Agregamos la nueva oferta a la lista de ofertas
			if(oferta.isNotEmpty()){
				this.ofertas.add(oferta);
				cantOfertas++;
			}
			
			//5. Actualizamos la pos de la estrategia a usar en la siguiente iteracion
			pos++;
		}
	
	}
	
	private boolean validarOfertas() {
		if(!ofertas.isEmpty()){
			for(Oferta oferta : ofertas)
				if(oferta.isCreada()){
					mostrarMensaje("Error", "Algunas ofertas no se han completado.", null, null, null, null);
					return false;
				}
			return true;
		}
		
		return false;
	}
	
	private boolean guardarOfertas(boolean enviar){
		boolean enviada = false;
		for(Oferta oferta: ofertas){
			enviada |= oferta.isInvalida() || oferta.enviar();
			if(enviar)
				if(!oferta.isInvalida())
					oferta.setEstatus(EEstatusOferta.ENVIADA);
				else
					oferta.setEstatus(EEstatusOferta.RECHAZADA);
			sTransaccion.actualizarOferta(oferta);
		}
		return enviada;
	}

	/**GETTERS Y SETTERS*/
	public STransaccion getsTransaccion() {
		return sTransaccion;
	}

	public void setsTransaccion(STransaccion sTransaccion) {
		this.sTransaccion = sTransaccion;
	}

	public SLocalizacion getsLocalizacion() {
		return sLocalizacion;
	}

	public void setsLocalizacion(SLocalizacion sLocalizacion) {
		this.sLocalizacion = sLocalizacion;
	}

	public MailCliente getMailCliente() {
		return mailCliente;
	}

	public void setMailCliente(MailCliente mailCliente) {
		this.mailCliente = mailCliente;
	}

	public MailProveedor getMailProveedor() {
		return mailProveedor;
	}

	public void setMailProveedor(MailProveedor mailProveedor) {
		this.mailProveedor = mailProveedor;
	}

	public List<Oferta> getOfertas() {
		return ofertas;
	}

	public void setOfertas(List<Oferta> ofertas) {
		this.ofertas = ofertas;
	}

	public List<DetalleOferta> getDetallesOfertas() {
		return detallesOfertas;
	}

	public void setDetallesOfertas(List<DetalleOferta> detallesOfertas) {
		this.detallesOfertas = detallesOfertas;
	}
}
