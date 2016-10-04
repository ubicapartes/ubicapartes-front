package com.okiimport.app.mvvm.controladores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Default;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SortEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Paging;

import com.okiimport.app.model.Analista;
import com.okiimport.app.model.Cliente;
import com.okiimport.app.model.DetalleRequerimiento;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.model.Usuario;
import com.okiimport.app.model.enumerados.EEstatusRequerimiento;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.model.ModeloCombo;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.transaccion.STransaccion;

public abstract class AbstractMisRequerimientosViewModel extends AbstractRequerimientoViewModel implements EventListener<SortEvent> {
	
	//Servicios
	@BeanInjector("sTransaccion")
	protected STransaccion sTransaccion;
		
	//GUI
	@Wire("#gridMisRequerimientos")
	protected Listbox gridMisRequerimientos;
		
	@Wire("#pagMisRequerimientos")
	protected Paging pagMisRequerimientos;
	
	//Atributos	
	protected List <Requerimiento> listaRequerimientos;
		
	protected Usuario usuario;
	protected Requerimiento requerimientoFiltro;
		
	protected List<ModeloCombo<String>> listaEstatus;
	protected ModeloCombo<String> estatusFiltro;
	
	@Override
	public void doAfterCompose(Component view){
		super.doAfterCompose(view);
		UserDetails user = this.getUser();
		requerimientoFiltro = new Requerimiento(new Cliente(), new Analista());
		usuario = sControlUsuario.consultarUsuario(user.getUsername(), user.getPassword(), null);
		cambiarRequerimientos(0, null, null);
		agregarGridSort(gridMisRequerimientos);
		pagMisRequerimientos.setPageSize(pageSize);
		estatusFiltro=new ModeloCombo<String>("No Filtrar", "");
	}
	
	/**Interface: EventListener<SortEvent>*/
	@Override
	@NotifyChange("listaRequerimientos")
	public void onEvent(SortEvent event) throws Exception {	
		if(event.getTarget() instanceof Listheader){
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("fieldSort", ((Listheader) event.getTarget()).getValue().toString());
			parametros.put("sortDirection", event.isAscending());
			ejecutarGlobalCommand("cambiarRequerimientos", parametros );
		}
	}
	
	/**GLOBAL COMMAND*/
	/**
	 * Descripcion: permitira cambiar los requerimientos de la grid de acuerdo a la pagina dada como parametro
	 * @param page: pagina a consultar, si no se indica sera 0 por defecto
	 * @param fieldSort: campo de ordenamiento, puede ser nulo
	 * @param sorDirection: valor boolean que indica el orden ascendente (true) o descendente (false) del ordenamiento
	 * Retorno: Ninguno
	 * */
	@GlobalCommand
	@SuppressWarnings("unchecked")
	@NotifyChange("listaRequerimientos")
	public void cambiarRequerimientos(@Default("0") @BindingParam("page") int page, 
			@BindingParam("fieldSort") String fieldSort, 
			@BindingParam("sortDirection") Boolean sortDirection){
		Map<String, Object> parametros = consultarMisRequerimientos(fieldSort, sortDirection, page);
				//sTransaccion.consultarMisRequerimientosEmitidos(requerimientoFiltro, 
				//fieldSort, sortDirection,usuario.getPersona().getId(), page, pageSize);
		Integer total = (Integer) parametros.get("total");
		listaRequerimientos = (List<Requerimiento>) parametros.get("requerimientos");
		pagMisRequerimientos.setActivePage(page);
		pagMisRequerimientos.setTotalSize(total);
	}
	
	/**COMMAND*/
	/**
	 * Descripcion: permitira cambiar la paginacion de acuerdo a la pagina activa del Paging
	 * @param Ninguno
	 * Retorno: Ninguno
	 * */
	@Command
	@NotifyChange("*")
	public void paginarLista(){
		int page=pagMisRequerimientos.getActivePage();
		cambiarRequerimientos(page, null, null);
	}
	
	/**
	 * Descripcion: permitira filtrar los datos de la grid de acuerdo al campo establecido en el evento
	 * @param Ninguno
	 * Retorno: Ninguno
	 * */
	@Command
	@NotifyChange("listaRequerimientos")
	public void aplicarFiltro(){
		requerimientoFiltro.setEstatus(null);
		if(estatusFiltro!=null)
			if(!estatusFiltro.getNombre().equalsIgnoreCase("No Filtrar"))
				requerimientoFiltro.setEstatus(EEstatusRequerimiento.findEEstatusRequerimiento(estatusFiltro.getValor()));
		cambiarRequerimientos(0, null, null);
	}
	
	/**
	 * Descripcion: permitira crear el emergente (modal) necesario para consultar la informacion del requerimiento seleccionado
	 * @param requerimiento: requerimiento seleccionado
	 * Retorno: Ninguno
	 * */
	@Command
	public void verRequerimiento(@BindingParam("requerimiento") final Requerimiento requerimiento){
		requerimiento.setDetalleRequerimientos(consultarDetallesRequerimiento(requerimiento));
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("requerimiento", requerimiento);
		parametros.put("editar", false);
		crearModal(BasePackageSistemaFunc+"emitidos/editarRequerimiento.zul", parametros);
	}
	
	/**
	 * Descripcion: permitira visualizar los proveedores para luego registrar la cotizacion respectiva
	 * @param requerimiento: requerimiento seleccionado
	 * Retorno: Ninguno
	 */
	@Command
	public void cotizar(@BindingParam("requerimiento") final Requerimiento requerimiento){
		requerimiento.setDetalleRequerimientos(consultarDetallesRequerimiento(requerimiento));
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("requerimiento", requerimiento);
		crearModal(BasePackageSistemaFunc+"en_proceso/listaProveedoresCotizar.zul", parametros );
	}
	
	/**METODOS ABSTRACTOS DE LA CLASE*/
	protected abstract Map<String, Object> consultarMisRequerimientos(String fieldSort, Boolean sortDirection, int page);
	
	/**METODOS PROPIOS DE LA CLASE*/
	@SuppressWarnings("unchecked")
	protected List<DetalleRequerimiento> consultarDetallesRequerimiento(final Requerimiento requerimiento){
		return (List<DetalleRequerimiento>) sTransaccion.consultarDetallesRequerimiento(requerimiento.getIdRequerimiento(), 0, -1).get("detallesRequerimiento");
	}
	
	/**SETTERS Y GETTERS*/
	public STransaccion getsTransaccion() {
		return sTransaccion;
	}

	public void setsTransaccion(STransaccion sTransaccion) {
		this.sTransaccion = sTransaccion;
	}

	public List<Requerimiento> getListaRequerimientos() {
		return listaRequerimientos;
	}

	public void setListaRequerimientos(List<Requerimiento> listaRequerimientos) {
		this.listaRequerimientos = listaRequerimientos;
	}
	
	public Requerimiento getRequerimientoFiltro() {
		return requerimientoFiltro;
	}

	public void setRequerimientoFiltro(Requerimiento requerimientoFiltro) {
		this.requerimientoFiltro = requerimientoFiltro;
	}

	public List<ModeloCombo<String>> getListaEstatus() {
		return listaEstatus;
	}

	public void setListaEstatus(List<ModeloCombo<String>> listaEstatus) {
		this.listaEstatus = listaEstatus;
	}

	public ModeloCombo<String> getEstatusFiltro() {
		return estatusFiltro;
	}

	public void setEstatusFiltro(ModeloCombo<String> estatusFiltro) {
		this.estatusFiltro = estatusFiltro;
	}
}
