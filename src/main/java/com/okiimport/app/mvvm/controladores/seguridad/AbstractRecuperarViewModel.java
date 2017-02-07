package com.okiimport.app.mvvm.controladores.seguridad;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.okiimport.app.model.Usuario;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.mail.MailUsuario;
import com.okiimport.app.service.seguridad.SAcceso;

public abstract class AbstractRecuperarViewModel extends AbstractRequerimientoViewModel {
	
	//Servicios
	@BeanInjector("sAcceso")
	protected SAcceso sAcceso;
		
	@BeanInjector("mailUsuario")
	protected MailUsuario mailUsuario;
		
	//GUI
	@Wire("#txtEmail")
	protected Textbox txtEmail;
	
	@Command
	public void enviar(){
		Usuario usuario = this.sAcceso.consultarUsuario(txtEmail.getText());
		if(usuario != null){
			//Send Email
			onSendSuccess(usuario);
		}
		else {
			super.mostrarMensaje("Error", "Email No Registrado", Messagebox.ERROR, null, null, null);
		}
	}
	
	/**ABSTRACT METHODS*/
	protected abstract void onSendSuccess(Usuario usuario);
	
	/**GETTERS Y SETTERS*/
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
