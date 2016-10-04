package com.okiimport.app.mvvm.controladores.seguridad.configuracion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Default;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SortEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;

import com.okiimport.app.model.Analista;
import com.okiimport.app.model.Persona;
import com.okiimport.app.model.Usuario;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.model.ModeloCombo;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.resource.service.PasswordGenerator;
import com.okiimport.app.service.mail.MailUsuario;

public class FormularioUsuariosViewModel extends AbstractRequerimientoViewModel implements  EventListener<SortEvent>{
	
	//Servicios
	@BeanInjector("mailUsuario")
	private MailUsuario mailUsuario;
	
	//GUI
	@Wire("#gridPersonas")
	public Listbox gridPersonas;
	
	@Wire("#pagPersonas")
	private Paging pagPersonas;
	
	//Modelos
	private List<Persona> personasSinUsuario;
	private Persona personaSeleccionada;
	private Usuario usuario;
	private Persona personaFiltro;
	
	//Atributos
	private List<ModeloCombo<Integer>> tiposUsuario;
	private ModeloCombo<Integer> tipoSeleccionado;

	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
		pagPersonas.setPageSize(pageSize);
		usuario = new Usuario();
		personaFiltro = Persona.getNewInstance();
		personaSeleccionada = Persona.getNewInstance();
		tiposUsuario = new ArrayList<ModeloCombo<Integer>>();
		tiposUsuario.add(new ModeloCombo<Integer>("Analistas", 1));
		tiposUsuario.add(new ModeloCombo<Integer>("Administradores", 2));
		tiposUsuario.add(new ModeloCombo<Integer>("Proveedores", 3));
		agregarGridSort(gridPersonas);
	}
	
	@Override
	public void onEvent(SortEvent event) throws Exception {
		// TODO Auto-generated method stub
		if(event.getTarget() instanceof Listheader){
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("fieldSort",  event.getTarget().getId().toString());
			parametros.put("sortDirection", event.isAscending());
			ejecutarGlobalCommand("cambiarPersonas", parametros );
		}
	}
	
	/**GLOBAL COMMAND*/
	@GlobalCommand
	@SuppressWarnings("unchecked")
	@NotifyChange({"personasSinUsuario", "usuario"})
	public void cambiarPersonas(@Default("0") @BindingParam("page") int page, 
			@BindingParam("fieldSort") String fieldSort, 
			@BindingParam("sortDirection") Boolean sortDirection){
		
		if(tipoSeleccionado!=null){
			usuario = new Usuario();
			Map<String, Object> parametros = null;
			switch (tipoSeleccionado.getValor()) {
			case 1:
				parametros = (Map<String, Object>) sMaestros.consultarAnalistasSinUsuarios(personaFiltro, fieldSort, sortDirection, 
						page, pageSize);
				personasSinUsuario = (List<Persona>) parametros.get("analistas");
				break;
			case 2:
				parametros = (Map<String, Object>) sMaestros.consultarAdministradoresSinUsuarios(personaFiltro, fieldSort, sortDirection,
						page, pageSize);
				personasSinUsuario = (List<Persona>) parametros.get("administradores");
				break;
			case 3:
				parametros = (Map<String, Object>) sMaestros.consultarProveedoresSinUsuarios(personaFiltro, 
						fieldSort, sortDirection, page, pageSize);
				personasSinUsuario = (List<Persona>) parametros.get("proveedores");
				break;
			default: return;
			}
			
			Integer total = (Integer) parametros.get("total");
			pagPersonas.setActivePage(page);
			pagPersonas.setTotalSize(total);
		}
	}
	
	/**COMMAND*/
	@Command
	@NotifyChange("personasSinUsuario")
	public void consultarPersonas(){
		cambiarPersonas(0, null, null);
	}
	
	@Command
	@NotifyChange("*")
	public void aplicarFiltro(){
		cambiarPersonas(0, null, null);
	}
	
	@Command
	@NotifyChange("*")
	public void paginarLista(){
		int page=pagPersonas.getActivePage();
		cambiarPersonas(page, null, null);
	}
	
	@Command
	@NotifyChange({"personaSeleccionada", "usuario"})
	public void verInfoPersona(@BindingParam("persona") Persona persona){
		personaSeleccionada = persona;
		this.usuario.setUsername(sControlUsuario.buscarUsername(personaSeleccionada));
		this.usuario.setPasword(PasswordGenerator.getPassword(PasswordGenerator.MINUSCULAS+PasswordGenerator.MAYUSCULAS
				+PasswordGenerator.NUMEROS,10));
	}
	
	@Command
	@NotifyChange({"personasSinUsuario", "usuario", "personaSeleccionada"})
	public void guardar(@BindingParam("txtPassword") Textbox txtPassword){
		if(personaSeleccionada.getCedula()!=null && !txtPassword.getValue().toString().equalsIgnoreCase("")){
			personaSeleccionada.setEstatus("activo");
			usuario.setActivo(true);
			usuario.setPersona(personaSeleccionada);
			usuario=sControlUsuario.grabarUsuario(usuario, sMaestros);
			if(usuario.getPersona() instanceof Analista)
				mailUsuario.enviarUsuarioyPassword(usuario, mailService);
			mostrarMensaje("Informacion", "Usuario Creado Exitosamente", null, null, null, null);
			personaSeleccionada = Persona.getNewInstance();
			int page = pagPersonas.getActivePage();
			cambiarPersonas(page, null, null);
		}
		else if(txtPassword.getValue().toString().equalsIgnoreCase(""))
			mostrarNotification("No se permiten campos en blanco.", "error", 5000, true, txtPassword);
		else
			mostrarMensaje("Error", "No se ha seleccionado ninguna persona", Messagebox.ERROR , null, null, null);
	}
	
	@Command
	@Override
	public void closeModal(){
		super.closeModal();
		ejecutarGlobalCommand("cambiarUsuarios", null);
	}

	/**SETTERS Y GETTERS*/
	public List<Persona> getPersonasSinUsuario() {
		return personasSinUsuario;
	}

	public void setPersonasSinUsuario(List<Persona> personasSinUsuario) {
		this.personasSinUsuario = personasSinUsuario;
	}

	public Persona getPersonaSeleccionada() {
		return personaSeleccionada;
	}
	
	public void setPersonaSeleccionada(Persona personaSeleccionada) {
		this.personaSeleccionada = personaSeleccionada;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Persona getPersonaFiltro() {
		return personaFiltro;
	}

	public void setPersonaFiltro(Persona personaFiltro) {
		this.personaFiltro = personaFiltro;
	}

	public ModeloCombo<Integer> getTipoSeleccionado() {
		return tipoSeleccionado;
	}

	public void setTipoSeleccionado(ModeloCombo<Integer> tipoSeleccionado) {
		this.tipoSeleccionado = tipoSeleccionado;
	}

	public List<ModeloCombo<Integer>> getTiposUsuario() {
		return tiposUsuario;
	}

	public void setTiposUsuario(List<ModeloCombo<Integer>> tiposUsuario) {
		this.tiposUsuario = tiposUsuario;
	}

	public MailUsuario getMailUsuario() {
		return mailUsuario;
	}

	public void setMailUsuario(MailUsuario mailUsuario) {
		this.mailUsuario = mailUsuario;
	}
	
	
}