package com.okiimport.app.mvvm.controladores.seguridad.configuracion;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Image;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.okiimport.app.model.Analista;
import com.okiimport.app.model.Persona;
import com.okiimport.app.model.Usuario;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.mail.MailCliente;
import com.okiimport.app.service.mail.MailUsuario;

public class EditarUsuarioViewModel extends AbstractRequerimientoViewModel implements EventListener<UploadEvent> {
	
	//Servicios
	@BeanInjector("mailUsuario")
	private MailUsuario mailUsuario;
	
	//GUI
	@Wire("#winEditarUsuario")
	private Window winEditarUsuario;
	
	@Wire("#imgFoto")
	private Image imgFoto;
	
	@Wire("#btnCambFoto")
	private Button btnCambFoto;
	
	private Textbox txtUsername;
	
	//Modelos
	private Persona persona;
	private Usuario usuario;
	
	//Atributos
	private AbstractValidator validadorUsername;
	private String username;

	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("usuario") Usuario usuario){
		super.doAfterCompose(view);
		persona = usuario.getPersona();
		this.usuario = usuario;
		this.usuario.setPasword(null);
		username = this.usuario.getUsername();
		
		btnCambFoto.addEventListener("onUpload", this);
		
		validadorUsername = new AbstractValidator() {
			
			@Override
			public void validate(ValidationContext ctx) {
				// TODO Auto-generated method stub
				String username = (String) ctx.getProperty().getValue();
				txtUsername = (Textbox) ctx.getBindContext().getValidatorArg("txtUsername");
				if(username!=null)
					if(sControlUsuario.verificarUsername(username) && 
							!username.equalsIgnoreCase(EditarUsuarioViewModel.this.username)){
						String mensaje = "El Username "+username+" ya esta en uso!";
						mostrarNotification(mensaje, "error", 5000, true, txtUsername);
						addInvalidMessage(ctx, mensaje);
					}
			}
		};
	}
	
	/**INTERFACES*/
	//1. EventListener<UploadEvent>
	@Override
	public void onEvent(UploadEvent event) throws Exception {
		// TODO Auto-generated method stub
		Media media = event.getMedia();
		if (media instanceof org.zkoss.image.Image)
			imgFoto.setContent((org.zkoss.image.Image) media);
		else if (media != null)
			mostrarMensaje("Error", "No es una imagen: " + media, null, null, null, null);
	}
	
	/**COMMAND*/
	@Command
	@NotifyChange("usuario")
	public void guardar(){
		org.zkoss.image.Image foto = this.imgFoto.getContent();
		
		if(foto!=null)
			usuario.setFoto(foto.getByteData());
		usuario=sControlUsuario.actualizarUsuario(usuario, false);
		if(usuario.getPersona() instanceof Analista)
			mailUsuario.enviarUsuarioyPassword(usuario, mailService);
		mostrarMensaje("Informacion", "El usuario se ha actualizado exitosamente", null, null, null, null);
		winEditarUsuario.detach();
		ejecutarGlobalCommand("cambiarUsuarios", null);
	}

	/**SETTERS Y GETTERS*/	
	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public AbstractValidator getValidadorUsername() {
		return validadorUsername;
	}

	public void setValidadorUsername(AbstractValidator validadorUsername) {
		this.validadorUsername = validadorUsername;
	}

	public MailUsuario getMailUsuario() {
		return mailUsuario;
	}

	public void setMailUsuario(MailUsuario mailUsuario) {
		this.mailUsuario = mailUsuario;
	}
	
}