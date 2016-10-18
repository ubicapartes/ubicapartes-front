package com.okiimport.app.mvvm.controladores.configuracion;


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
	

	//Modelos
	private Usuario usuario;

	//Atributos
	private boolean isValidFoto;

	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
		btnCambFoto.addEventListener("onUpload", this);
		usuario = super.getUsuario();
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
				if (sControlUsuario.verificarUsername(txtUsername.getValue())
						& !(usuario.getUsername().equalsIgnoreCase(txtUsername.getValue()))){
					severidad = "Error";
					msg ="El username ya se encuentra registrado";
				}else{
					if(!(nuevaClave.equalsIgnoreCase("") && nuevaClaveConf.equalsIgnoreCase(""))){
						if((nuevaClave.equals(nuevaClaveConf))){
							if(verificarContrase�a(nuevaClaveConf)){
								usuario.setPasword(nuevaClave);
								usuario=sControlUsuario.actualizarUsuario(usuario, false);
								severidad = "Informacion";
								msg ="Datos guardados satisfactoriamente";
							}else{
								severidad = "Error";
								msg ="La contrase�a debe contener al menos un n�mero y una letra may�scula";
							}
						}else{
							severidad = "Error";
							msg ="Las contrase�as no coinciden, por favor verifique";
						}
					}else{
						BindUtils.postGlobalCommand("perfil", EventQueues.APPLICATION, "updateProfile", null);	
						usuario=sControlUsuario.actualizarUsuario(usuario, false);
						severidad = "Informacion";
						msg ="Datos guardados satisfactoriamente";
					}
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
		txtClaveNueva.setValue("");
		txtClaveNuevaConf.setValue("");
		txtNombre.setValue("");
		txtApellido.setValue("");
		txtDireccion.setValue("");
		txtUsername.setValue("");
	}

	/**
	 * Metodo que verifica si el formato de la contrase�a es correcto
	 * @param clave contrase�a a verificar
	 * @return true si el formato es correcto, false caso contrario
	 */
	public boolean verificarContrase�a(String clave){
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
}
