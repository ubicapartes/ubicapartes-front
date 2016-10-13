package com.okiimport.app.mvvm.controladores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

import com.okiimport.app.model.Analista;
import com.okiimport.app.model.Ciudad;
import com.okiimport.app.model.Estado;
import com.okiimport.app.model.factory.persona.EstatusPersonaFactory;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.model.ModeloCombo;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.transaccion.STransaccion;

public class RegistrarAnalistasViewModel extends AbstractRequerimientoViewModel {
	
	//Servicios
	@BeanInjector("sTransaccion")
	private STransaccion sTransaccion;
	
	//GUI
	@Wire("#winFormularioAnalista")
	private Window winFormularioAnalista;
	
		@Wire("#msgCorreoA") 
	  private Label lblMsgCorreo; 
	 
	  @Wire("#msgCedulaRifA") 
	  private Label lblMsgCedulaRif;
	
	//Atributos
	private Analista analista;
	private Ciudad ciudad;
	private List<ModeloCombo<Boolean>> listaTipoPersona;
	private ModeloCombo<Boolean> tipoPersona;
	private List<Estado> listaEstados;
	private String recordMode;
	private Boolean cerrar;
	private String valor=null;
	private Boolean validacionCorreo=false; 
	  private Boolean validacionCedulaRif=false; 
	private boolean makeAsReadOnly;
	
	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: formularioAnalistas.zul 
	 * Retorno: Ninguno 
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("analista") Analista analista,
			@ExecutionArgParam("recordMode") String recordMode,
			@ExecutionArgParam("valor") String valor,
			@ExecutionArgParam("cerrar") Boolean cerrar) {
		super.doAfterCompose(view);
		this.recordMode = (recordMode == null) ? "EDIT" : recordMode;
		
		this.analista = (analista==null) ? new Analista() :  analista;
		this.cerrar = (cerrar==null) ? true : cerrar;
		makeAsReadOnly = (recordMode != null && recordMode.equalsIgnoreCase("READ"))? true : false; 
		//limpiar();
		this.valor=valor;
	
		if(this.analista.getCiudad()==null){
			this.ciudad=new Ciudad();
			this.estado=new Estado();
		}else{
			this.ciudad=this.analista.getCiudad();
			this.estado=this.analista.getCiudad().getEstado();
		}
		
		/*if(recordMode.equalsIgnoreCase("READ")){
			this.ciudad=analista.getCiudad();
		}*/
		
		listaEstados = llenarListaEstados();
		listaTipoPersona = llenarListaTipoPersona();
		this.tipoPersona = listaTipoPersona.get(1);
		 tipoPersona=consultarTipoPersona(this.analista.getCedula(),listaTipoPersona); 
		    String cedula = this.analista.getCedula(); 
		    this.valor=valor;
		    if(cedula!=null) 
		        this.analista.setCedula(this.analista.getCedula().substring(1)); 
		      if(this.analista.getCiudad() != null) 
		        this.estado = this.analista.getCiudad().getEstado();
		
		
	}
	
	/**
	 * Descripcion: Permite registrar un analista en el sistema
	 * Parametros: @param view: formularioAnalistas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange({ "analista" })
	public void registrar(@BindingParam("btnEnviar") Button btnEnviar,
			@BindingParam("btnLimpiar") Button btnLimpiar,
			@BindingParam("recordMode") String recordMode,
			@BindingParam("edicion") String valor) {
		if (checkIsFormValid()) {
				
				btnEnviar.setDisabled(true);
				btnLimpiar.setDisabled(true);
				String tipo = (this.tipoPersona.getValor()) ? "J" : "V";
				analista.setCedula(tipo + analista.getCedula());
				analista.setCiudad(ciudad);
				analista.setiEstatus(EstatusPersonaFactory.getEstatusActivo());
				analista.setAdministrador(false);
				analista = sMaestros.registrarAnalista(analista);

				Map<String, Object> model = new HashMap<String, Object>();
				model.put("nombreSolicitante", analista.getNombre());
				model.put("cedula", analista.getCedula());
				
				this.valor=valor;
				if(this.valor!=null)
					mostrarMensaje("Informaci\u00F3n", "Analista Actualizado con Exito", null, null, null, null);
				else mostrarMensaje("Informaci\u00F3n", "Analista Registrado con Exito", null, null, null, null);
				this.setValor(null);
				this.recargar();
				
			}	
	}
	
	/**
	 * Descripcion: Permite recargar la pantalla al cerrar
	 * Parametros: @param view: formularioAnalistas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	public void recargar() {
		winFormularioAnalista.onClose();
		ejecutarGlobalCommand("cambiarAnalistas", null);
	}
	
	/**
	 * Descripcion: Permite limpiar los campos del formulario Analistas
	 * Parametros: @param view: formularioAnalistas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange({ "analista", "ciudad", "estado" })
	public void limpiar() {
		analista = new Analista();
		this.ciudad=new Ciudad();
		this.estado=new Estado();
		 this.validacionCorreo=false; 
		 this.validacionCedulaRif=false;
		super.cleanConstraintForm();
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	/**
	 * Descripcion: Permitira verificar que el correo ingresado existe o no en la BD 
	   * Parametros: Ninguno 
	   * Retorno Ninguno 
	   * Nota: Ninguna 
	   * */ 
	 @Command 
	  public void verificarCorreo(){ 
	     
	    this.lblMsgCorreo.setValue("El correo ya existe"); 
	    this.validacionCorreo=this.sMaestros.consultarCorreoAnalista(analista.getCorreo()); 
	    //llamada al metodo del validar  
	    if(this.validacionCorreo){ 
	      this.lblMsgCorreo.setVisible(true); 
	    }else{ 
	        this.lblMsgCorreo.setVisible(false); 
	        this.validacionCorreo=false; 
	      } 
	    } 
	 
	 
	 @Command 
	  public void verificarCedulaRif(){ 
	     
	    this.lblMsgCedulaRif.setValue("La cedula/Rif ya existe"); 
	    //System.out.println("cedula que ingresada es: "+analista.getCedula()); 
	    this.validacionCedulaRif=this.sMaestros.consultarCedulaRifAnalista(this.getCedulaCompleta()); 
	    //llamada al metodo del validar  
	    if(this.validacionCedulaRif){ 
	    	 this.lblMsgCedulaRif.setVisible(true); 
	    }else{ 
	      this.lblMsgCedulaRif.setVisible(false); 
	      this.validacionCedulaRif=false; 
	    } 
	  } 
	    
	 private String getCedulaCompleta(){ 
		    //String tipo = (this.tipoPersona.getValor()) ? "J" : "V"; 
		    return this.tipoPersona.getNombre() + analista.getCedula(); 
		  } 
	 
	 
	 /** 
	   * Descripcion: Permite Consultar el tipo de persona 
	   * Parametros: @param view: formularioAnalista.zul  
	   * Retorno: Ninguno 
	   * Nota: Ninguna 
	   * */
	 private ModeloCombo<Boolean> consultarTipoPersona(String cedula, List <ModeloCombo<Boolean>> listaTipoPersona){ 
		    if (cedula!=null){ 
		      String tipoPersona = cedula.substring(0, 1); 
		      for(ModeloCombo<Boolean> tipoPersonal: listaTipoPersona ) 
		        if (tipoPersonal.getNombre().equalsIgnoreCase(tipoPersona)) 
		          return tipoPersonal; 
		    } 
		    return this.tipoPersona; 
	  } 
	
    /**METODOS SETTERS AND GETTERS */
	
	public STransaccion getsTransaccion() {
		return sTransaccion;
	}

	public void setsTransaccion(STransaccion sTransaccion) {
		this.sTransaccion = sTransaccion;
	}

	public Analista getAnalista() {
		return analista;
	}

	public void setAnalista(Analista analista) {
		this.analista = analista;
	}

	public Ciudad getCiudad() {
		return ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

	public List<ModeloCombo<Boolean>> getListaTipoPersona() {
		return listaTipoPersona;
	}

	public void setListaTipoPersona(List<ModeloCombo<Boolean>> listaTipoPersona) {
		this.listaTipoPersona = listaTipoPersona;
	}

	public ModeloCombo<Boolean> getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(ModeloCombo<Boolean> tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public List<Estado> getListaEstados() {
		return listaEstados;
	}

	public void setListaEstados(List<Estado> listaEstados) {
		this.listaEstados = listaEstados;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	
	public List<Ciudad> getListaCiudades() {
		return listaCiudades;
	}

	public void setListaCiudades(List<Ciudad> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}

	public String getRecordMode() {
		return recordMode;
	}

	public void setRecordMode(String recordMode) {
		this.recordMode = recordMode;
	}

	public Boolean getCerrar() {
		return cerrar;
	}

	public void setCerrar(Boolean cerrar) {
		this.cerrar = cerrar;
	}

	public boolean isMakeAsReadOnly() {
		return makeAsReadOnly;
	}

	public void setMakeAsReadOnly(boolean makeAsReadOnly) {
		this.makeAsReadOnly = makeAsReadOnly;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
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
