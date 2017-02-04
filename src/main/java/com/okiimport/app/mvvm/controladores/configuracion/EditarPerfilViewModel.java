package com.okiimport.app.mvvm.controladores.configuracion;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Image;
import org.zkoss.zul.Textbox;

import com.okiimport.app.model.Usuario;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;


public class EditarPerfilViewModel extends AbstractRequerimientoViewModel implements EventListener<UploadEvent> {

	//Servicios

	//GUI
	@Wire("#imgFoto")
	private Image imgFoto;

	@Wire("#btnCambFoto")
	private Button btnCambFoto;

	@Wire("#txtClaveNueva")
	private Textbox txtClaveNueva;
	
	@Wire("#txtClaveAct")
	private Textbox txtClaveAct;

	@Wire("#txtClaveNuevaConf")
	private Textbox txtClaveNuevaConf;
	
	@Wire("#txtUsername")
	private Textbox txtUsername;

	@Wire("#txtNombre")
	private Textbox txtNombre;

	@Wire("#txtApellido")
	private Textbox txtApellido;

	@Wire("#txtDireccion")
	private Textbox txtDireccion;
	
	@Wire("#closeFoto")
	private Component closeFoto;
	
	private String originalUsername;
	


	//Modelos
	private Usuario usuario;

	//Atributos
	private boolean isValidFoto;

	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
		btnCambFoto.addEventListener("onUpload", this);
		usuario = super.getUsuario();
		setOriginalUsername(usuario.getUsername());
		if(usuario.getFoto64()!=null && !usuario.getFoto64().isEmpty()){
			setValidFoto(true);
			closeFoto.setVisible(true);
		} else {
			setValidFoto(false);
			closeFoto.setVisible(false);
		}
	}

	/**INTERFACES*/
	//1. EventListener<UploadEvent>
	@Override
	public void onEvent(UploadEvent event) throws Exception {
		// TODO Auto-generated method stub
		Media media = event.getMedia();
		if (media instanceof org.zkoss.image.Image){
			imgFoto.setContent((org.zkoss.image.Image) media);
			closeFoto.setVisible(true);
			setValidFoto(true);
		}else if (media != null){
			mostrarMensaje("Error", "No es una imagen: " + media, null, null, null, null);
			closeFoto.setVisible(false);
			setValidFoto(false);
		}
	}

	/**COMMAND*/
	@Command
	@NotifyChange("usuario")
	public void enviar(){
		String severidad ="";
		String msg="";
		String nuevaClave = txtClaveNueva.getValue();
		String nuevaClaveConf = txtClaveNuevaConf.getValue();
		org.zkoss.image.Image foto = this.imgFoto.getContent();
		
		if(foto!=null && isValidFoto())
			usuario.setFoto(foto.getByteData());
		
		usuario.getPersona().setApellido(txtApellido.getValue());
		usuario.getPersona().setNombre(txtNombre.getValue());
		usuario.getPersona().setDireccion(txtDireccion.getValue());
		
		if (!txtUsername.getValue().equalsIgnoreCase("")){
			try {
				if(validarClaveAct()){
					if (validarCambioUsuario() && sControlUsuario.verificarUsername(txtUsername.getValue())){
						severidad = "Error";
						msg ="El username ya se encuentra registrado";
					}else{
						if(!(nuevaClave.equalsIgnoreCase("") && nuevaClaveConf.equalsIgnoreCase(""))){
							if((nuevaClave.equals(nuevaClaveConf))){
								if(verificarContrasenia(nuevaClaveConf)){
									usuario.setPasword(nuevaClave);
									usuario=sControlUsuario.actualizarUsuario(usuario, false);
									severidad = "Informacion";
									msg ="Datos guardados satisfactoriamente";
									setOriginalUsername(txtUsername.getValue());
									cambiarUsuarioAutenticado(usuario);
									
								}else{
									severidad = "Error";
									msg ="La clave debe contener al menos un numero y una letra mayuscula";
								}
							}else{
								severidad = "Error";
								msg ="Las claves no coinciden, por favor verifique";
							}
						}else{
							BindUtils.postGlobalCommand("perfil", EventQueues.APPLICATION, "updateProfile", null);	
							usuario=sControlUsuario.actualizarUsuario(usuario, false);
							severidad = "Informacion";
							msg ="Datos guardados satisfactoriamente";
							setOriginalUsername(txtUsername.getValue());
							cambiarUsuarioAutenticado(usuario);
						}
					}
				} else {
					severidad="Error";
					msg="Clave actual invalida, por favor coloque su clave actual para poder realizar cambios";
				}
			}catch(Exception e){
				System.out.println(e.getMessage());
				severidad="Error";
				msg="Error interno";
			}
		}else{
			severidad = "Error";
			msg ="Campos obligatorios vacios";
		}
			mostrarMensaje(severidad, msg, null, null, null, null);
			txtClaveNueva.setValue("");
			txtClaveNuevaConf.setValue("");
		
	}
	
	/**
	 * Metodo para limpiar los campos de texto de la vista
	 */
	@Command
	public void limpiar(){
		txtClaveAct.setValue("");
		txtClaveNueva.setValue("");
		txtClaveNuevaConf.setValue("");
		txtNombre.setValue("");
		txtApellido.setValue("");
		txtDireccion.setValue("");
	}

	/**
	 * Metodo que verifica si el formato de la contrase�a es correcto
	 * @param clave contrasenia a verificar
	 * @return true si el formato es correcto, false caso contrario
	 */
	public boolean verificarContrasenia(String clave){
		boolean m=false;
		boolean n=false;
		for(char x: clave.toCharArray()){
			if(Character.isUpperCase(x)){
				m=true;
			}
			if(Character.isDigit(x)){
				n=true;
			}
		}
		return n && m?true:false;
	}
	
	@Command
	@NotifyChange("usuario")
	public void eliminarFoto(){
		usuario.setFoto(null);
		closeFoto.setVisible(false);
		setValidFoto(true);
	}
	
	public boolean validarCambioUsuario(){
		boolean respuesta = false;
		if(!getOriginalUsername().equalsIgnoreCase(txtUsername.getValue())){
			respuesta = true;
			
		}
		return respuesta;
	}
	
	public boolean validarClaveAct(){
		boolean respuesta = false;
		if(usuario.getPasword().equalsIgnoreCase(txtClaveAct.getValue())){
			respuesta = true;
		}
		return respuesta;
	}
	
	public void cambiarUsuarioAutenticado(Usuario usuario){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		UserDetails origUser = (UserDetails)securityContext.getAuthentication().getPrincipal();
		UserDetails newUser = new User(usuario.getUsername(),usuario.getPasword(),origUser.isEnabled(),	origUser.isAccountNonExpired(),	origUser.isCredentialsNonExpired(),origUser.isAccountNonLocked(),origUser.getAuthorities());
		securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(newUser, newUser.getPassword()));

	}
	
	/**SETTERS Y GETTERS*/	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public boolean isValidFoto() {
		return isValidFoto;
	}

	public void setValidFoto(boolean isValidFoto) {
		this.isValidFoto = isValidFoto;
	}
	

	public String getOriginalUsername() {
		return originalUsername;
	}

	public void setOriginalUsername(String originalUsername) {
		this.originalUsername = originalUsername;
	}
}
