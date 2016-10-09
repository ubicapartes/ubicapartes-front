package com.okiimport.app.mvvm.controladores;

import java.util.Iterator;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.okiimport.app.model.Analista;
import com.okiimport.app.model.Ciudad;
import com.okiimport.app.model.Estado;
import com.okiimport.app.model.Persona;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.maestros.SMaestros;
import com.okiimport.app.service.mail.MailAdmin;
import com.okiimport.app.service.mail.MailProveedor;
import com.okiimport.app.service.transaccion.STransaccion;

public class ContactarViewModel extends AbstractRequerimientoViewModel {

	
	//Servicios
		@BeanInjector("sMaestros")
		private SMaestros sMaestros;
		
		@BeanInjector("mailAdmin")
		private MailAdmin mailAdmin;
		
		@BeanInjector("mailProveedor")
		private MailProveedor mailProveedor;
		
		@Wire("#datosContact")
		private Vlayout vFormulario;
	
	private String nombre;
	private String correo;
	private String telefono;
	private String mensaje;
	private boolean valor;
	
	
	


	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: contactanos.zul 
	 * Retorno: Ninguno 
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
		this.valor=true;
		this.limpiar();
		
	}


	/**
	 * Descripcion: Permite enviar el mensaje al admin 
	 * Parametros: Ninguno
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void enviarMensaje() {
		try {
			if (checkIsFormValid()){
				System.out.println("enviando msj...."+this.correo+" "+this.nombre+" "+this.telefono+" "+this.mensaje);
				
				List<Analista> admins = this.sMaestros.consultarAdministradores();
				
				Iterator<Analista> iter=admins.iterator();
				Analista p=new Analista();
				while (iter.hasNext() ) {
		            p = iter.next();
		            System.out.println("correo del admin es "+p.getCorreo());
		            this.mailProveedor.enviarInformacionContacto(p.getCorreo(), this.nombre, this.telefono, this.correo, this.mensaje, mailService);
		            
		        }
				//limpiar();
				mostrarMensaje("Informaci\u00F3n", "Mensaje enviado Exitosamente"
						+ " (Message sent successfully)",
						null, null, new org.zkoss.zk.ui.event.EventListener(){
					@Override
					public void onEvent(Event e) throws Exception {
						// TODO Auto-generated method stub
						 if("onOK".equals(e.getName())){
							  redireccionar("/contacto");
			                }else if("onCancel".equals(e.getName())){
			                    
			                }}
					}, null);
				//wait(6000);
				//redireccionar("/contacto");
				
				}
		} catch (Exception e) {
			mostrarMensaje("Informaci\u00F3n", "Verifique que todos los campos esten llenos con la informacion correcta", null, null, null, null);
		}
		
	}
	
	
	@Command
	@NotifyChange({ "valor"})
	public void validarCampos() {
		if(this.correo!=null && this.telefono!=null && this.nombre!=null && this.mensaje!=null){
			if(!this.correo.isEmpty() && !this.telefono.isEmpty() && !this.nombre.isEmpty() && !this.mensaje.isEmpty() ){
				if (checkIsFormValid()) this.valor=false;
				else this.valor=true;
			}
			else this.valor=true;
		}
		else this.valor=true;
	}
	
	/**COMMAND*/
	/**
	 * Descripcion: Permite limpiar los campos del formulario
	 * Parametros: @param view: contactanos.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange({ "*" })
	public void limpiar() {
		this.correo=new String();
		this.telefono=new String();
		this.mensaje=new String();
		this.nombre=new String();
	}
	

	public MailAdmin getMailAdmin() {
		return mailAdmin;
	}


	public void setMailAdmin(MailAdmin mailAdmin) {
		this.mailAdmin = mailAdmin;
	}

	public MailProveedor getMailProveedor() {
		return mailProveedor;
	}

	public void setMailProveedor(MailProveedor mailProveedor) {
		this.mailProveedor = mailProveedor;
	}

	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public String getCorreo() {
		return correo;
	}



	public void setCorreo(String correo) {
		this.correo = correo;
	}



	public String getTelefono() {
		return telefono;
	}



	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}



	public String getMensaje() {
		return mensaje;
	}



	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public SMaestros getsMaestros() {
		return sMaestros;
	}


	public void setsMaestros(SMaestros sMaestros) {
		this.sMaestros = sMaestros;
	}


	public boolean getValor() {
		return valor;
	}


	public void setValor(boolean valor) {
		this.valor = valor;
	}

	
}
