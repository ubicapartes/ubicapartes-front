package com.okiimport.app.mvvm.controladores.seguridad.configuracion;

import org.zkoss.bind.BindUtils;
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
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.okiimport.app.model.Analista;
import com.okiimport.app.model.Cuenta;
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
	
	@Wire("#btnCambFoto2")
	private Button btnCambFoto2;
	
	@Wire("#msgUsername") 
	  private Label lblMsgUsername;
	
	@Wire("#msgPassword") 
	  private Label lblMsgPassword;
	
	@Wire("#closeFoto")
	private Component closeFoto;
	
	@Wire("#txtClaveNueva")
	private Textbox txtClaveNueva;
	
	@Wire("#txtClaveNuevaConf")
	private Textbox txtClaveNuevaConf;
	
	@Wire("#txtUsername")
	private Textbox txtUsername;
	
	
	//Modelos
	private Persona persona;
	private Usuario usuario;
	
	//Atributos
	private AbstractValidator validadorUsername;
	private String username;
	private String repetirPassword;
	private boolean isValidFoto;
	private String originalUsername;

	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("usuario") Usuario usuario){
		super.doAfterCompose(view);
		
		this.cuenta = (usuario==null) ? new Usuario() : u;
		this.cerrar = (cerrar==null) ? true : cerrar;
		tipoCuenta= (cuenta==null? "CORRIENTE": cuenta.getTipo());
		this.valor=valor;
		
		persona = usuario.getPersona();
		this.usuario = usuario;
		this.usuario.setPasword(null);
		//username = this.usuario.getUsername();
		originalUsername = this.usuario.getUsername();
		btnCambFoto2.addEventListener("onUpload", this);
		this.lblMsgPassword.setVisible(false);
		this.lblMsgUsername.setVisible(false);
		
		/*super.doAfterCompose(view);
		btnCambFoto.addEventListener("onUpload", this);
		usuario = super.getUsuario();
		setOriginalUsername(usuario.getUsername());
		if(usuario.getFoto64()!=null && !usuario.getFoto64().isEmpty()){
			setValidFoto(true);
			closeFoto.setVisible(true);
		} else {
			setValidFoto(false);
			closeFoto.setVisible(false);
		}*/
		
		if(this.usuario.getFoto64()!=null && !this.usuario.getFoto64().isEmpty()){
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
	
	@Command
	@NotifyChange("usuario")
	public void eliminarFoto(){
		usuario.setFoto(null);
		closeFoto.setVisible(false);
		setValidFoto(true);
	}
	
	/**COMMAND*/
	@Command
	@NotifyChange("usuario")
	public void guardar(){
		//org.zkoss.image.Image foto = this.imgFoto.getContent();
		String nuevaClave = txtClaveNueva.getValue();
		String nuevaClaveConf = txtClaveNuevaConf.getValue();
		org.zkoss.image.Image foto = this.imgFoto.getContent();
		String severidad ="";
		String msg="";
		System.out.println("foto -> "+foto);
		System.out.println("isValidFoto() -> "+isValidFoto());
		if(foto!=null && isValidFoto())
			usuario.setFoto(foto.getByteData());
		
		if (!txtUsername.getValue().equalsIgnoreCase("")){
			try {
				
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
									
								}else{
									severidad = "Error";
									msg ="La clave debe contener al menos un numero y una letra mayuscula";
								}
							}else{
								severidad = "Error";
								msg ="Las claves no coinciden, por favor verifique";
							}
						}else{
							ejecutarGlobalCommand("cambiarUsuarios", null);
							usuario=sControlUsuario.actualizarUsuario(usuario, false);
							severidad = "Informacion";
							msg ="Datos guardados satisfactoriamente";
							setOriginalUsername(txtUsername.getValue());
							winEditarUsuario.onClose();
							
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
			
				/*if(usuario.getPasword().equals(usuario.getPaswordRepeat())){
							if (checkIsFormValid()) {
							
									if(foto!=null)
										usuario.setFoto(foto.getByteData());
									usuario=sControlUsuario.actualizarUsuario(usuario, false);
									if(usuario.getPersona() instanceof Analista)
										mailUsuario.enviarUsuarioyPassword(usuario, mailService);
									mostrarMensaje("Informacion", "El usuario se ha actualizado exitosamente", null, null, null, null);
									winEditarUsuario.detach();
									ejecutarGlobalCommand("cambiarUsuarios", null);
									
							}
				}else{
					this.lblMsgPassword.setValue("Las contrase�as no coinciden");
					this.lblMsgPassword.setVisible(true);
					mostrarMensaje("Informaci\u00F3n", "Las contrase�as no coinciden", Messagebox.EXCLAMATION, null, null, null);
					
				}*/
		
		
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
	
	public boolean validarCambioUsuario(){
		boolean respuesta = false;
		if(!getOriginalUsername().equalsIgnoreCase(txtUsername.getValue())){
			respuesta = true;
			
		}
		return respuesta;
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

	public String getRepetirPassword() {
		return repetirPassword;
	}

	public void setRepetirPassword(String repetirPassword) {
		this.repetirPassword = repetirPassword;
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