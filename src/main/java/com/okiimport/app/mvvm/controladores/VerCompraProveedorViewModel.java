package com.okiimport.app.mvvm.controladores;

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
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SortEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;

import com.okiimport.app.model.DetalleOferta;
import com.okiimport.app.model.Proveedor;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.model.Usuario;

import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.transaccion.STransaccion;

public class VerCompraProveedorViewModel extends AbstractRequerimientoViewModel implements EventListener<SortEvent> {

	// Servicios
	@BeanInjector("sTransaccion")
	private STransaccion sTransaccion;

	// GUI
	@Wire("#gridComprasProveedor")
	private Listbox gridComprasProveedor;

	@Wire("#pagComprasProveedor")
	private Paging pagComprasProveedor;
	
	@Wire("#winVerCompraProveedor")
	private Window winVerCompraProveedor;

	// Atributos
	private List<DetalleOferta> listaCompras;
	private Requerimiento requerimiento;
	private Proveedor proveedor;
	private String titulo = "Solicitudes de Compra del Requerimiento N° ";

	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: listaComprasCliente.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("requerimiento") Requerimiento requerimiento) {
		super.doAfterCompose(view);
		UserDetails user = this.getUser();
		Usuario usuario = sControlUsuario.consultarUsuario(user.getUsername(), user.getPassword(), null); 
		proveedor=new Proveedor(usuario.getPersona());
		this.requerimiento = requerimiento;
		this.titulo = this.titulo + requerimiento.getIdRequerimiento();
		agregarGridSort(gridComprasProveedor);
		pagComprasProveedor.setPageSize(pageSize);
		
		cambiarCompras(0, null, null);
		System.out.println("numero de requerimiento: "+requerimiento.getIdRequerimiento());
		System.out.println("id Proveedor: "+proveedor.getId());
	}

	/**Interface: EventListener<SortEvent>*/
	@Override
	public void onEvent(SortEvent event) throws Exception {
		// TODO Auto-generated method stub
		if(event.getTarget() instanceof Listheader){
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("fieldSort", ((Listheader) event.getTarget()).getValue().toString());
			parametros.put("sortDirection", event.isAscending());
			ejecutarGlobalCommand("cambiarCompras", parametros );
		}
	}

	/**GLOBAL COMMAND*/
	/**
	 * Descripcion: 
	 * Parametros: @param view: verComprasProveedor.zul 
	 * Retorno: Ninguno 
	 * Nota: Ninguna
	 * */
	@GlobalCommand
	@SuppressWarnings("unchecked")
	@NotifyChange("listaCompras")
	public void cambiarCompras(@Default("0") @BindingParam("page") int page,
			@BindingParam("fieldSort") String fieldSort,
			@BindingParam("sortDirection") Boolean sortDirection) {
		Map<String, Object> parametros = sTransaccion.consultarSolicitudesCompraProveedor(requerimiento, proveedor, page, pageSize);
		Integer total = (Integer) parametros.get("total");
		listaCompras = (List<DetalleOferta>) parametros.get("detallesOferta");
		pagComprasProveedor.setActivePage(page);
		pagComprasProveedor.setTotalSize(total);
	}

	/** COMMAND */
	/**
	 * Descripcion: Permitira cambiar la paginacion de acuerdo a la pagina
	 * activa del Paging 
	 * Parametros: @param view: listaComprasCliente.zul 
	 * Retorno: Ninguno 
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange("*")
	public void paginarLista() {
		int page = pagComprasProveedor.getActivePage();
		cambiarCompras(page, null, null);
	}	

	/**
	 * Descripcion: 
	 * Parametros: compra @param view: listaComprasCliente.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	public void cambiarRequerimientos(){
		ejecutarGlobalCommand("cambiarRequerimientos", null);
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	public STransaccion getsTransaccion() {
		return sTransaccion;
	}

	public void setsTransaccion(STransaccion sTransaccion) {
		this.sTransaccion = sTransaccion;
	}

	public List<DetalleOferta> getListaCompras() {
		return listaCompras;
	}

	public void setListaCompras(List<DetalleOferta> listaCompras) {
		this.listaCompras = listaCompras;
	}

	public Requerimiento getRequerimiento() {
		return requerimiento;
	}

	public void setRequerimiento(Requerimiento requerimiento) {
		this.requerimiento = requerimiento;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
}
