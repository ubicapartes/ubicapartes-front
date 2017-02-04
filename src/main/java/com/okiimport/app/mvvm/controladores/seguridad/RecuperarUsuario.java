package com.okiimport.app.mvvm.controladores.seguridad;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.okiimport.app.model.Usuario;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.mail.MailUsuario;
import com.okiimport.app.service.seguridad.SAcceso;

public class RecuperarUsuario extends AbstractRequerimientoViewModel {
	
	//Servicios
	@BeanInjector("sAcceso")
	private SAcceso sAcceso;
	
	@BeanInjector("mailUsuario")
	private MailUsuario mailUsuario;
	
	//GUI
	@Wire("#txtEmail")
	private Textbox txtEmail;
	
	
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
	}
	
	@Command
	public void enviar(){
		Usuario usuario = this.sAcceso.consultarUsuario(txtEmail.getText());
		if(usuario != null){
			//Send Email
			this.mailUsuario.recuperarUsuario(usuario, mailService);
			super.mostrarMensaje("Informacion", "Tu usuario se ha enviado a tu correo.", Messagebox.INFORMATION, null, null, null);
		}
		else {
			super.mostrarMensaje("Error", "Email No Registrado", Messagebox.ERROR, null, null, null);
		}
	}

	public SAcceso getsAcceso() {
		return sAcceso;
	}

	public void setsAcceso(SAcceso sAcceso) {
		this.sAcceso = sAcceso;
	}

	public MailUsuario getMailUsuario() {
		return mailUsuario;
	}

	public void setMailUsuario(MailUsuario mailUsuario) {
		this.mailUsuario = mailUsuario;
	}
	
	

}
