package com.okiimport.app.mvvm.controladores;

import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;

import com.okiimport.app.model.Ciudad;
import com.okiimport.app.model.Cliente;
import com.okiimport.app.model.Estado;
import com.okiimport.app.model.Persona;
import com.okiimport.app.model.Usuario;
import com.okiimport.app.model.factory.persona.EstatusCliente;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.constraint.ClienteCedulaRifConstraint;
import com.okiimport.app.mvvm.constraint.CustomConstraint;
import com.okiimport.app.mvvm.constraint.CustomConstraint.EConstraint;
import com.okiimport.app.mvvm.constraint.EmailConstraint;
import com.okiimport.app.mvvm.constraint.PasswordConstraint;
import com.okiimport.app.mvvm.model.ModeloCombo;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.resource.service.PasswordGenerator;
import com.okiimport.app.service.configuracion.SControlUsuario;
import com.okiimport.app.service.mail.MailUsuario;

public class RegistrarUsuarioViewModel extends AbstractRequerimientoViewModel {

	//Servicios
	@BeanInjector("sControlUsuario")
	private SControlUsuario sControlUsuario;

	@BeanInjector("mailUsuario")
	private MailUsuario mailUsuario;

	
	// GUI
	@Wire("#cedulaRif")
	public Intbox cedulaRif;
	
	@Wire("#lblMsgCorreo")
	public Label lblMsgCorreo;
	
	@Wire("#msgCorreoC") 
	  private Label lblMsgCorreoC; 
	 
	  @Wire("#msgCedulaRifC") 
	  private Label lblMsgCedulaRif; 
	
	
	@Wire("#cmbEstado")
	private Combobox cmbEstado;

	@Wire("#cmbCiudad")
	private Combobox cmbCiudad;
	
	@Wire("#comboTipoPersona")
	private Combobox comboTipoPersona;

	//Atributos
	private CustomConstraint constrEstado = null;
	private CustomConstraint constrCiudad = null;
	private List<Estado> listaEstados;
	private List<Ciudad> listaCiudad;
	private ModeloCombo<Boolean>  tipoPersona;
	private List<ModeloCombo<Boolean>> listaTipoPersona;
	private Estado estadoSeleccionado;
	private boolean makeAsReadOnly;
	protected Usuario usuario;
	protected Persona persona;
	protected Cliente cliente;
	private Boolean validacionCorreo=false; 
	private Boolean validacionCedulaRif=false;


	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: formularioProveedor.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view) {
		super.doAfterCompose(view);
		limpiar();
		listaEstados = llenarListaEstados();
		listaTipoPersona = llenarListaTipoPersona();
		tipoPersona = listaTipoPersona.get(1);
		//tipoPersona=consultarTipoPersona(this.cliente.getCedula(),listaTipoPersona);
	}

	/**
	 * Descripcion: Permite limpiar los campos del formulario registrar Proveedor
	 * Parametros: @param view: formularioRegistroUsuario.zul 
	 * Retorno: Ninguno 
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange({ "usuario", "cliente", "estado", "constrEstado", "constrCiudad", "lblMsgCorreoC", "lblMsgCedulaRif" }) 
	public void limpiar() {
		this.cliente = new Cliente();
		this.usuario = new Usuario(this.cliente, true);
		limpiarEstadoYCiudad();
		 this.validacionCorreo=false; 
		    this.validacionCedulaRif=false; 
		    this.lblMsgCorreoC.setVisible(false); 
		    this.lblMsgCedulaRif.setVisible(false); 
	}
	
	/**METODOS OVERRIDE*/
	@Override
	public CustomConstraint getValidatorClienteCedulaRif() {
		return new ClienteCedulaRifConstraint(new ClienteCedulaRifConstraint.ClienteCedulaRifComunicator() {
			
			@Override
			public Boolean searchClient(String cedulaORif) {
				return sMaestros.consultarCedulaCliente(cedulaORif);
			}
			
			@Override
			public String getTypeClient() {
				return (tipoPersona.getValor()) ? "J" : "V";
			}
		});
	}
	
	@Override
	public CustomConstraint getEmailValidator() {
		return new EmailConstraint(super.getEmailValidator(), new EmailConstraint.EmailConstraintComunicator() {
			
			@Override
			public Boolean searchClient(String email) {
				return sMaestros.consultarCorreoCliente(email);
			}
		});
	}

	/**METODOS PROPIOS DE LA CLASE*/
	public CustomConstraint getPasswordValidator(){
		return new PasswordConstraint(cliente, super.getNotEmptyValidator(), new PasswordConstraint.PasswordConstraintComunicator() {
			
			@Override
			public String getNumeros() {
				return PasswordGenerator.NUMEROS;
			}
			
			@Override
			public String getMinusculas() {
				return PasswordGenerator.MINUSCULAS;
			}
			
			@Override
			public String getMayusculas() {
				return PasswordGenerator.MAYUSCULAS;
			}
		});
	}
	
	public CustomConstraint getRepeatPasswordValidator(){
		return new CustomConstraint(EConstraint.NO_EMPTY, EConstraint.CUSTOM){

			@Override
			protected void validateCustom(Component comp, Object value) throws WrongValueException {
				String password = usuario.getPasword();
				String repeatPassword = String.valueOf(value);
				
				if(!repeatPassword.equalsIgnoreCase(password)){
					String mensaje = "No coinciden las contrase\u00f1as!";
					throw new WrongValueException(comp, mensaje);
				}
			}
			
		};
	}
	
	/**
	 * Descripcion: Permite limpiar las variables que se encargan de las variables de ciudad y estado
	 * Parametros: Ninguno
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	private void limpiarEstadoYCiudad(){
		if(constrEstado!=null)
			constrEstado.hideComponentError();
		if(constrCiudad!=null)
			constrCiudad.hideComponentError();
		constrEstado = null;
		constrCiudad = null;
		estado = null;
		cliente.setCiudad(null);
	}

	/**
	 * Descripcion: Permite registrar un proveedor en el sistema
	 * Parametros: @param view: formularioProveedor.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void registrar(){
		if (checkIsFormValid()) {
			cliente.setCedula(getCedulaComleta());
			cliente.setEstatus(EstatusCliente.getEstatusActivo().getValue());
			usuario.setUsername(cliente.getCorreo());
			this.sControlUsuario.grabarUsuario(usuario, sMaestros);
			this.redireccionar("/inicioSession");
		}
	}


	/**
	 * Descripcion: Permitira obtener la cedula completa del proveedor
	 * Parametros: Ninguno
	 * Retorno Ninguno
	 * Nota: Ninguna
	 * */
	private String getCedulaComleta(){
		return this.tipoPersona.getNombre() + cliente.getCedula();
	}


	@Command 
	  public void verificarCorreo(){ 
	     
	    this.lblMsgCorreoC.setValue("El correo ya existe"); 
	    this.validacionCorreo=this.sMaestros.consultarCorreoCliente(cliente.getCorreo()); 
	    //llamada al metodo del validar  
	    if(this.validacionCorreo){ 
	      this.lblMsgCorreoC.setVisible(true); 
	    }else{ 
	        this.lblMsgCorreoC.setVisible(false); 
	        this.validacionCorreo=false; 
	      } 
	    } 
	
	
	@Command 
	  public void verificarCedulaRif(){ 
	     
	    this.lblMsgCedulaRif.setValue("La cedula/Rif ya existe"); 
	    this.validacionCedulaRif=this.sMaestros.consultarCedulaCliente(this.getCedulaComleta()); 
	    //llamada al metodo del validar  
	    if(this.validacionCedulaRif){
	    	this.lblMsgCedulaRif.setVisible(true); 
	    }else{ 
	      this.lblMsgCedulaRif.setVisible(false); 
	      this.validacionCedulaRif=false; 
	    } 
	  } 
	      
	      
	/**GETTERS Y SETTERS*/
	public List<Estado> getListaEstados() {
		return listaEstados;
	}


	public void setListaEstados(List<Estado> listaEstados) {
		this.listaEstados = listaEstados;
	}


	public List<Ciudad> getListaCiudad() {
		return listaCiudad;
	}


	public void setListaCiudad(List<Ciudad> listaCiudad) {
		this.listaCiudad = listaCiudad;
	}


	public List<ModeloCombo<Boolean>> getListaTipoPersona() {
		return listaTipoPersona;
	}


	public void setListaTipoPersona(List<ModeloCombo<Boolean>> listaTipoPersona) {
		this.listaTipoPersona = listaTipoPersona;
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

	public SControlUsuario getsControlUsuario() {
		return sControlUsuario;
	}

	public void setsControlUsuario(SControlUsuario sControlUsuario) {
		this.sControlUsuario = sControlUsuario;
	}

	public Estado getEstadoSeleccionado() {
		return estadoSeleccionado;
	}


	public void setEstadoSeleccionado(Estado estadoSeleccionado) {
		this.estadoSeleccionado = estadoSeleccionado;
	}


	public boolean isMakeAsReadOnly() {
		return makeAsReadOnly;
	}


	public void setMakeAsReadOnly(boolean makeAsReadOnly) {
		this.makeAsReadOnly = makeAsReadOnly;
	}


	public MailUsuario getMailUsuario() {
		return mailUsuario;
	}


	public void setMailUsuario(MailUsuario mailUsuario) {
		this.mailUsuario = mailUsuario;
	}


	public Cliente getCliente() {
		return cliente;
	}


	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}


	public Combobox getCmbEstado() {
		return cmbEstado;
	}


	public void setCmbEstado(Combobox cmbEstado) {
		this.cmbEstado = cmbEstado;
	}


	public Combobox getCmbCiudad() {
		return cmbCiudad;
	}


	public void setCmbCiudad(Combobox cmbCiudad) {
		this.cmbCiudad = cmbCiudad;
	}


	public ModeloCombo<Boolean> getTipoPersona() {
		return tipoPersona;
	}


	public void setTipoPersona(ModeloCombo<Boolean> tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public Combobox getComboTipoPersona() {
		return comboTipoPersona;
	}

	public void setComboTipoPersona(Combobox comboTipoPersona) {
		this.comboTipoPersona = comboTipoPersona;
	}

	public Intbox getCedulaRif() {
		return cedulaRif;
	}

	public void setCedulaRif(Intbox cedulaRif) {
		this.cedulaRif = cedulaRif;
	}
	
	 public Boolean getValidacionCorreo() { 
		    return validacionCorreo; 
		  } 
		 
	 public void setValidacionCorreo(Boolean validacionCorreo) { 
		    this.validacionCorreo = validacionCorreo; 
		  }
		  
    public Boolean getValidacionCedulaRif() { 
			    return validacionCedulaRif; 
			  } 
			 
	public void setValidacionCedulaRif(Boolean validacionCedulaRif) { 
			    this.validacionCedulaRif = validacionCedulaRif; 
	} 

}
