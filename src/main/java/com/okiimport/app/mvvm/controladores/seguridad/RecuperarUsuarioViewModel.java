package com.okiimport.app.mvvm.controladores.seguridad;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Messagebox;

import com.okiimport.app.model.Usuario;

public class RecuperarUsuarioViewModel extends AbstractRecuperarViewModel {
	
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
	}
	
	/**Override Methods*/
	@Override
	protected void onSendSuccess(Usuario usuario){
		this.mailUsuario.recuperarUsuario(usuario, mailService);
		super.mostrarMensaje("Informacion", "Tu usuario se ha enviado a tu correo.", Messagebox.INFORMATION, null, null, null);
	}
	
}
