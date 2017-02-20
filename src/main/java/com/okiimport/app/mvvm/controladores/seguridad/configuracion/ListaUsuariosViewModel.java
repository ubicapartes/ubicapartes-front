package com.okiimport.app.mvvm.controladores.seguridad.configuracion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Default;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SortEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;

import com.okiimport.app.model.Usuario;
import com.okiimport.app.model.factory.persona.EstatusPersonaFactory;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;

public class ListaUsuariosViewModel extends AbstractRequerimientoViewModel implements EventListener<SortEvent>{
	
	//Servicios
	
	//GUI
	@Wire("#gridUsuarios")
	private Listbox gridUsuarios;
	
	@Wire("#pagUsuarios")
	private Paging pagUsuarios;
	
	@Wire("#radEstado")
	private Radiogroup radEstado;
	
	//Modelos
	private List<Usuario> usuarios;
	private Usuario usuarioFiltro;
	
	//Atributos
	private static final int PAGE_SIZE = 10;

	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
		usuarioFiltro = new Usuario();
		pagUsuarios.setPageSize(PAGE_SIZE);
		agregarGridSort(gridUsuarios);
		cambiarUsuarios(0, null, null);
	}
	
	/**Interface: EventListener<SortEvent>*/
	@Override
	@NotifyChange("usuarios")
	public void onEvent(SortEvent event) throws Exception {
		// TODO Auto-generated method stub		
		if(event.getTarget() instanceof Listheader){
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("fieldSort",  event.getTarget().getId().toString());
			parametros.put("sortDirection", event.isAscending());
			ejecutarGlobalCommand("cambiarUsuarios", parametros );
		}
		
	}
	
	/**GLOBAL COMMAND*/
	@GlobalCommand
	@NotifyChange("usuarios")
	public void cambiarUsuarios(@Default("0") @BindingParam("page") int page, 
			@BindingParam("fieldSort") String fieldSort, 
			@BindingParam("sortDirection") Boolean sortDirection){
		Map<String, Object> parametros = sControlUsuario.consultarUsuarios(usuarioFiltro, fieldSort, sortDirection, page, PAGE_SIZE);
		Integer total = (Integer) parametros.get("total");
		usuarios = (List<Usuario>) parametros.get("usuarios");
		pagUsuarios.setActivePage(page);
		pagUsuarios.setTotalSize(total);
		System.out.print("pasa por el cambiar script......");
	}
	
	/**COMMAND*/
	@Command
	@NotifyChange("*")
	public void paginarLista(){
		int page=pagUsuarios.getActivePage();
		cambiarUsuarios(page, null, null);
	}
	
	@Command
	@NotifyChange("usuarios")
	public void aplicarFiltro(){
		Radio selectedItem = radEstado.getSelectedItem();
		this.usuarioFiltro.setActivo((selectedItem!=null) ? Boolean.valueOf((String)selectedItem.getValue()) : null);
		cambiarUsuarios(0, null, null);
	}
	
	@Command
	@NotifyChange("usuarios")
	public void limpiarRadios(){
		this.usuarioFiltro.setActivo(null);
		radEstado.setSelectedIndex(-1);
		aplicarFiltro();
	}
	
	@Command
	public void nuevoUsuario(){
		llamarFormulario("formularioUsuarios.zul", null);
	}
	
	@Command
	public void editarUsuario(@BindingParam("usuario") Usuario usuario){
		Usuario userSession = consultarUsuarioSession();
		if(userSession.getId()!=usuario.getId()){
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("usuario", usuario);
			llamarFormulario("editarUsuario.zul", parametros);
		}
		else
			mostrarMensaje("Error", "No se puede Editar el Usuario de la Session", Messagebox.ERROR, null, null, null);
	}
	
	@SuppressWarnings("rawtypes")
	@Command
	@NotifyChange("usuarios")
	public void actualizarEstado(@BindingParam("usuario") final Usuario usuario, @BindingParam("estado") final Boolean estado){
				
		super.mostrarMensaje("Confirmacion", "�Est� seguro que desea cambiar el estatus del usuario?", Messagebox.EXCLAMATION, new Messagebox.Button[]{Messagebox.Button.YES,Messagebox.Button.NO}, 
				new EventListener(){

					
					public void onEvent(Event event) throws Exception {
						// TODO Auto-generated method stub
						Messagebox.Button button = (Messagebox.Button) event.getData();
						if (button == Messagebox.Button.YES) {
							
							Usuario userSession = consultarUsuarioSession();
							if(userSession.getId()!=usuario.getId()){
								if(sControlUsuario.cambiarEstadoUsuario(usuario, estado)){
									String mensaje = (estado) ? "Activado" : "Desactivado";
									mostrarMensaje("Informacion", "Usuario "+mensaje+" Exitosamente", null, null, null, null);
									paginarLista();
									notifyChange("usuarios");
								}
							}
							else
								mostrarMensaje("Error", "No se puede Desactivar el Usuario de la Session", Messagebox.ERROR, null, null, null);
						
							}
							
				}
					
			
		}, null);
		
		
	
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	private Usuario consultarUsuarioSession(){
		UserDetails user = this.getUser();
		return sControlUsuario.consultarUsuario(user.getUsername(), user.getPassword(), null);
	}
	
	private void llamarFormulario(String ruta, Map<String, Object> parametros){
		crearModal(BasePackageSistema+"seguridad/configuracion/usuarios/"+ruta, parametros);
	}

	/**SETTERS Y GETTERS*/
	public List<Usuario> getUsuarios() {
		return usuarios;
	}
	
	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	
	public Usuario getUsuarioFiltro() {
		return usuarioFiltro;
	}
}
