package com.okiimport.app.mvvm.controladores.seguridad;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Messagebox;

import com.okiimport.app.model.Usuario;

public class RecuperarPasswordViewModel extends AbstractRecuperarViewModel {
	
	private String serverUrl;
	
	@Init
	public void init(@ExecutionArgParam("serverUrl") String serverUrl){
		this.serverUrl = serverUrl;
	}
	
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
	}

	@Override
	protected void onSendSuccess(Usuario usuario) {
		usuario = sAcceso.generarTokenParaCambiarPassword(usuario);
		this.mailUsuario.recuperarPassword(serverUrl, usuario, mailService);
		super.mostrarMensaje("Informacion", "Te hemos enviado un correo.", Messagebox.INFORMATION, null, null, null);
	}

	

}
