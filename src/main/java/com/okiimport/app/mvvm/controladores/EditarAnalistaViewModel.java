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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;
import com.okiimport.app.model.Analista;
import com.okiimport.app.model.Ciudad;
import com.okiimport.app.model.Estado;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.model.ModeloCombo;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.transaccion.STransaccion;

public class EditarAnalistaViewModel extends AbstractRequerimientoViewModel{

	//Servicios
	@BeanInjector("sTransaccion")
	private STransaccion sTransaccion;
	
	//GUI
	@Wire("#winFormularioAnalista")
	private Window winFormularioAnalista;
	
	//Atributos
	private Boolean editar;
	private Analista analista;
	private Ciudad ciudad;
	private List<ModeloCombo<Boolean>> listaTipoPersona;
	private ModeloCombo<Boolean> tipoPersona;
	private List<Estado> listaEstados;
	
	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: editarAnalistas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view,  @ExecutionArgParam("analista") Analista analista ) {
		super.doAfterCompose(view);
		limpiar();
		
		listaEstados = llenarListaEstados();
		listaTipoPersona = llenarListaTipoPersona();
		this.tipoPersona = listaTipoPersona.get(1);
		this.analista = analista;
		this.ciudad = analista.getCiudad();
		if(this.estado!=null)
		this.estado = this.ciudad.getEstado();
		if(this.analista.getCiudad() != null)
			this.estado = this.analista.getCiudad().getEstado();
		
	}
	
	/**
	 * Descripcion: Llama a registrar analista
	 * Parametros: @param view: editarAnalistas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange({ "analista" })
	public void registrar(@BindingParam("btnGuardar") Button btnGuardar,
			@BindingParam("btnLimpiar") Button btnLimpiar) {
		
		if (checkIsFormValid()){
			
			    btnGuardar.setDisabled(true);
			    btnLimpiar.setDisabled(true);
			    String tipo = (this.tipoPersona.getValor())? "J" : "V";
			    analista.setCedula(tipo + analista.getCedula());
				analista.setCiudad(ciudad);
				analista = sMaestros.registrarAnalista(analista);

				Map<String, Object> model = new HashMap<String, Object>();
				model.put("nombreSolicitante", analista.getNombre());
				model.put("cedula", analista.getCedula());
				
				
				
				mostrarMensaje("Informaci\u00F3n", "El Analista ha sido registrado existosamente ", null, null, new EventListener()
				{
							public void onEvent(Event event) throws Exception {
								
									winFormularioAnalista.onClose();
							}
						},null);
				
		}
				
	}
	
	/**
	 * Descripcion: Llama a limpiar los campos
	 * Parametros: @param view: editarAnalistas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange({ "analista" })
	public void limpiar() {
		analista = new Analista();
		super.cleanConstraintForm();
	}

	
	/**
	 * Descripcion: Llama a Cerrar el Modal 
	 * Parametros: @param view: editarAnalistas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void CerrarVista()
	{
		ejecutarGlobalCommand("cambiarAnalistas", null);
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	
	/**SETTERS Y GETTERS*/
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

	public Boolean getEditar() {
		return editar;
	}

	public void setEditar(Boolean editar) {
		this.editar = editar;
	}

	
}
