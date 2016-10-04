package com.okiimport.app.mvvm.controladores;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Borderlayout;

import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;

public class InicioViewModel extends AbstractRequerimientoViewModel {
	
	//GUI	
	@Wire("#principal")
	private Borderlayout principal;
	
	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: index.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
		registrarRequerimiento();
	}
	
	/**
	 * Descripcion: Llama a formulario para registrar solicitud de proveedor 
	 * Parametros: @param view: index.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void registrarProveedor(){
		insertComponent(principal.getPage(), "#mainInclude", BasePackagePortal+"formularioProveedor.zul");
	}
	
	/**
	 * Descripcion: Llama a formulario para registrar solicitud de usuario 
	 * Parametros: @param view: index.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void registrarUsuario(){
		insertComponent(principal.getPage(), "#mainInclude", BasePackagePortal+"formularioRegistroUsuario.zul");
	}
	
	/**
	 * Descripcion: Llama a formulario para verificar solicitud de requerimiento
	 * Parametros: @param view: index.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void verificarRequerimiento(){
		insertComponent(principal.getPage(), "#mainInclude", BasePackagePortal+"formularioVerificarRequerimiento.zul");
	}
	
	/**
	 * Descripcion: Llama a formulario registrar requerimiento
	 * Parametros: @param view: index.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void registrarRequerimiento(){
		insertComponent(principal.getPage(), "#mainInclude", BasePackagePortal+"formularioRequerimiento.zul");
	}

}
