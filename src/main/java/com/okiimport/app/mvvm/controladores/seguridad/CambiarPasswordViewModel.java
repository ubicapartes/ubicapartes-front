package com.okiimport.app.mvvm.controladores.seguridad;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;

import com.okiimport.app.model.Usuario;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.constraint.CustomConstraint;

public class CambiarPasswordViewModel extends AbstractRequerimientoViewModel {
	
	private Usuario usuario;

	@Init
	public void init(@ExecutionArgParam("usuario") Usuario usuario){
		this.usuario = usuario;
		this.usuario.setPasword(null);
	}
	
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
	}
	
	@Command
	public void enviar(){
		if (checkIsFormValid()) {
			 this.sControlUsuario.actualizarUsuario(usuario, true);
			 this.redireccionar("/inicioSession");
		}
	}
	
	public CustomConstraint getPasswordValidator(){
		return super.getPasswordValidator(usuario.getPersona());
	}
	
	public CustomConstraint getRepeatPasswordValidator(){
		return super.getRepeatPasswordValidator(usuario);
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	

}
