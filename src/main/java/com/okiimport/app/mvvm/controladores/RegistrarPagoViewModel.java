package com.okiimport.app.mvvm.controladores;

import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;

import com.okiimport.app.model.Ciudad;
import com.okiimport.app.model.Cliente;
import com.okiimport.app.model.Estado;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.model.ModeloCombo;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.transaccion.STransaccion;

public class RegistrarPagoViewModel extends AbstractRequerimientoViewModel  {

	//Servicios
	@BeanInjector("sTransaccion")
	private STransaccion sTransaccion;
	
	//GUI
	@Wire("#cmbBancoPago")
	private Combobox cmbBancoPago;
	@Wire("#cmbEmpresaEncomiendas")
	private Combobox cmbEmpresaEncomiendas;
	@Wire("#cmbOficinaDireccion")
	private Combobox cmbOficinaDireccion;
	
	//Atributos
	private List <ModeloCombo<String>> listaBancoPago;
	private ModeloCombo<String> bancoPago;
	private List <ModeloCombo<String>> listaEmpresaEncomiendas;
	private ModeloCombo<String> empresaEncomiendas;
	private List <ModeloCombo<Boolean>> listaOficinaDireccion;
	private ModeloCombo<Boolean> oficinaDireccion;
	private List<Estado> listaEstados;
	private List<ModeloCombo<Boolean>> listaTipoPersona;
	private ModeloCombo<Boolean> tipoPersona;
	private Ciudad ciudad;
	private Cliente cliente;
	
	 /**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: formularioRegistrarPago.zul 
     * Retorno: Ninguno 
	 * Nota: Ninguna
	 * */
	@AfterCompose
	//@SuppressWarnings("unchecked")
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view)
			
	{
		
	listaBancoPago = llenarListaBancoPago();
	listaEmpresaEncomiendas = llenarListaEmpresaEncomiendas();
	listaOficinaDireccion = llenarListaOficinaDireccion();
	listaEstados = llenarListaEstados();
	listaTipoPersona = llenarListaTipoPersona();
	
	
	}
	
	 /**
		 * Descripcion: ESTO NO HACE NADA
		 * Parametros: @param view: formularioRegistrarPago.zul 
		 * Retorno: Ninguno
		 * Nota: Ninguna
		 * */
	public RegistrarPagoViewModel() {
		// TODO Auto-generated constructor stub
	}

	
     /**METODOS PROPIOS DE LA CLASE*/
	 
	 /**GETTERS Y SETTERS*/

	public STransaccion getsTransaccion() {
		return sTransaccion;
	}


	public void setsTransaccion(STransaccion sTransaccion) {
		this.sTransaccion = sTransaccion;
	}


	public List<ModeloCombo<String>> getListaBancoPago() {
		return listaBancoPago;
	}


	public void setListaBancoPago(List<ModeloCombo<String>> listaBancoPago) {
		this.listaBancoPago = listaBancoPago;
	}


	public ModeloCombo<String> getBancoPago() {
		return bancoPago;
	}


	public void setBancoPago(ModeloCombo<String> bancoPago) {
		this.bancoPago = bancoPago;
	}


	public List<ModeloCombo<String>> getListaEmpresaEncomiendas() {
		return listaEmpresaEncomiendas;
	}


	public void setListaEmpresaEncomiendas(
			List<ModeloCombo<String>> listaEmpresaEncomiendas) {
		this.listaEmpresaEncomiendas = listaEmpresaEncomiendas;
	}


	public ModeloCombo<String> getEmpresaEncomiendas() {
		return empresaEncomiendas;
	}


	public void setEmpresaEncomiendas(ModeloCombo<String> empresaEncomiendas) {
		this.empresaEncomiendas = empresaEncomiendas;
	}


	public List<ModeloCombo<Boolean>> getListaOficinaDireccion() {
		return listaOficinaDireccion;
	}


	public void setListaOficinaDireccion(
			List<ModeloCombo<Boolean>> listaOficinaDireccion) {
		this.listaOficinaDireccion = listaOficinaDireccion;
	}


	public ModeloCombo<Boolean> getOficinaDireccion() {
		return oficinaDireccion;
	}


	public void setOficinaDireccion(ModeloCombo<Boolean> oficinaDireccion) {
		this.oficinaDireccion = oficinaDireccion;
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


	public Cliente getCliente() {
		return cliente;
	}


	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
}
