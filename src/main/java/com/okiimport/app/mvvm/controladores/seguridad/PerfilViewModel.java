package com.okiimport.app.mvvm.controladores.seguridad;

import org.springframework.security.core.userdetails.UserDetails;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Span;

import com.okiimport.app.model.Usuario;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.transaccion.STransaccion;


public class PerfilViewModel extends AbstractRequerimientoViewModel {
	
	//Servicios
	@BeanInjector("sTransaccion")
	private STransaccion sTransaccion;
	
	//GUI
	@Wire("#menInfoUsuario")
	private Menuitem menInfoUsuario;
	
	@Wire("#menCarrito")
	private Menuitem menCarrito;
	
	private Integer idCliente = -1;
	
	private Div hrefEnlacesMenu;
	
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
		
		UserDetails user = super.getUser();
		if(user!=null){
			Usuario usuario = sControlUsuario.consultarUsuario(user.getUsername(), user.getPassword(), null);
			menInfoUsuario.setTooltiptext(usuario.getUsername()+" Avatar");
			menInfoUsuario.setLabel(usuario.getUsername().toUpperCase());
			setIdCliente(usuario.getPersona().getId());
			getSizeShoppingCar();
		}
	}
	
	@GlobalCommand
	public void getSizeShoppingCar(){
		Integer size = 0;
		size = sTransaccion.consultarDetallesOfertaInShoppingCar(getIdCliente()).size();
		menCarrito.setLabel("("+size+")");
	}
	
	@Command
	public void editarPerfil(){
		if(hrefEnlacesMenu==null)
        	hrefEnlacesMenu = (Div) findComponent(menInfoUsuario.getPage(), "#hrefEnlacesMenu");
		
		//Borramos los Hijos
        hrefEnlacesMenu.getChildren().clear();
        
        //Nuevo Enlace del Menu
        Div nuevoEnlace = new Div();
        nuevoEnlace.setSclass("breadcrumb");
        Label label = new Label();
        label.setValue("Editar Perfil");
		nuevoEnlace.appendChild(label);
		
		hrefEnlacesMenu.appendChild(nuevoEnlace);
		
		insertComponent(menInfoUsuario.getPage(), "#mainInclude", BasePackageSistema+"configuracion/editarPerfil.zul");
	}
	

	@Command
	public void mostrarCarrito(){
		if(hrefEnlacesMenu==null)
        	hrefEnlacesMenu = (Div) findComponent(menInfoUsuario.getPage(), "#hrefEnlacesMenu");
		
		//Borramos los Hijos
        hrefEnlacesMenu.getChildren().clear();
        
        //Nuevo Enlace del Menu
        Div nuevoEnlace = new Div();
        nuevoEnlace.setSclass("breadcrumb");
        Span icono = new Span();
		icono.setSclass("z-icon-fa fa-shopping-cart");
		nuevoEnlace.appendChild(icono);
        Label label = new Label();
        label.setValue(" Carrito de Compra");
		nuevoEnlace.appendChild(label);
		
		hrefEnlacesMenu.appendChild(nuevoEnlace);
		
		insertComponent(menInfoUsuario.getPage(), "#mainInclude", BasePackageSistema+"funcionalidades/usuario/miCarrito.zul");
	}
	
	@Command
	public void logout(){
		redireccionar("/logout");
	}
	
	
	//GETTERS Y SETTERS	

	public Integer getIdCliente() {
		return idCliente;
	}


	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}
	
	public STransaccion getsTransaccion() {
		return sTransaccion;
	}

	public void setsTransaccion(STransaccion sTransaccion) {
		this.sTransaccion = sTransaccion;
	}

}
