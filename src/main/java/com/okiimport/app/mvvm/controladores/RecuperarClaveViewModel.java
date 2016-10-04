package com.okiimport.app.mvvm.controladores;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;

import com.okiimport.app.model.Cliente;
import com.okiimport.app.model.Persona;
import com.okiimport.app.model.Usuario;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.mail.MailUsuario;
import com.okiimport.app.service.transaccion.STransaccion;

public class RecuperarClaveViewModel extends AbstractRequerimientoViewModel{
	
	//Servicios
			@BeanInjector("sTransaccion")
			private STransaccion sTransaccion;
			
			@BeanInjector("mailUsuario")
			private MailUsuario mailUsuario;
			
	protected Usuario usuario;
	protected Persona persona;
	protected Cliente cliente;
	
	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: recuperarClave.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("persona") Persona persona) {
		super.doAfterCompose(view);
		
	}

	public STransaccion getsTransaccion() {
		return sTransaccion;
	}

	public void setsTransaccion(STransaccion sTransaccion) {
		this.sTransaccion = sTransaccion;
	}

	public MailUsuario getMailUsuario() {
		return mailUsuario;
	}

	public void setMailUsuario(MailUsuario mailUsuario) {
		this.mailUsuario = mailUsuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	
}
