package com.okiimport.app.mvvm.controladores.compra;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SortEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Paging;

import com.okiimport.app.model.Cliente;
import com.okiimport.app.model.Compra;
import com.okiimport.app.model.Usuario;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.model.ModeloCombo;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.transaccion.STransaccion;

public class MisComprasViewModel extends AbstractRequerimientoViewModel implements EventListener<SortEvent> {
	
		//Servicios
		@BeanInjector("sTransaccion")
		private STransaccion sTransaccion;

		//GUI
		@Wire("#gridComprasCliente")
		private Listbox gridComprasCliente;
		@Wire("#pagComprasCliente")
		private Paging pagComprasCliente;
		
		//Atributos
		private Date fechaCreacion;
		private Cliente cliente;
		private Compra compraFiltro;
		private String cedula;
		
		private String requerimientoIdFiltro;
		private String fechaCreacionFiltro;
		private String estatusFiltro;
		

		

		private List <ModeloCombo<Boolean>> listaTipoPersona;
		private ModeloCombo<Boolean> tipoPersona;
		private List <Compra> listaCompras;
		private List <Compra> listaComprasFiltro;
		private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		
		/**
		 * Descripcion: Llama a inicializar la clase 
		 * Parametros: @param view: formularioVerificarRequerimiento.zul 
		 * Retorno: Ninguno 
		 * Nota: Ninguna
		 * */
		@AfterCompose
		public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view)
		{
			super.doAfterCompose(view);
			UserDetails user = super.getUser();
			if(user!=null){
				Usuario usuario = sControlUsuario.consultarUsuario(user.getUsername(), user.getPassword(), null);
				cedula = usuario.getPersona().getCedula();
			}
			compraFiltro = new Compra();
			pagComprasCliente.setPageSize(pageSize);
			agregarGridSort(gridComprasCliente);
			listaTipoPersona = llenarListaTipoPersona();
			tipoPersona = listaTipoPersona.get(1);
			cambiarCompras(0,null,true);
		}

		/**Interface: EventListener<SortEvent>*/
		@Override
		@NotifyChange("listaRequerimientos")
		public void onEvent(SortEvent event) throws Exception {	
			if(event.getTarget() instanceof Listheader){
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("fieldSort", ((Listheader) event.getTarget()).getValue().toString());
				parametros.put("sortDirection", event.isAscending());
				ejecutarGlobalCommand("cambiarCompras", parametros );
			}
		}

		/**GLOBAL COMMAND*/
		@SuppressWarnings("unchecked")
		@GlobalCommand
		@NotifyChange("listaCompras")
		public void cambiarCompras(@Default("0") @BindingParam("page") int page,
				@BindingParam("fieldSort") String fieldSort, 
				@BindingParam("sortDirection") Boolean sortDirection){
			Map<String, Object> parametros = sTransaccion.consultarComprasDelClienteTodosEstatus(cedula, fieldSort, sortDirection, page, pageSize);
			Integer total = (Integer) parametros.get("total");
			listaCompras = (List<Compra>) parametros.get("compras");
			gridComprasCliente.setMultiple(true);
			pagComprasCliente.setActivePage(page);
			pagComprasCliente.setTotalSize(total);
		}
		
		
		@SuppressWarnings("unchecked")
		@GlobalCommand
		@NotifyChange("listaCompras")
		public void refrescarListadoCompras(@Default("0") @BindingParam("page") int page,
				@BindingParam("fieldSort") String fieldSort, 
				@BindingParam("sortDirection") Boolean sortDirection){
			Map<String, Object> parametros = sTransaccion.consultarComprasDelClienteTodosEstatus(cedula, fieldSort, sortDirection, page, pageSize);
			Integer total = (Integer) parametros.get("total");
			listaCompras = (List<Compra>) parametros.get("compras");
			gridComprasCliente.setMultiple(true);
			pagComprasCliente.setActivePage(page);
			pagComprasCliente.setTotalSize(total);
		}
		/**
		 * Descripcion: Permitira cambiar la paginacion de acuerdo a la pagina activa del Paging 
		 * Parametros: @param view: formularioVerificarRequerimiento.zul  
		 * Retorno: Ninguno
		 * Nota: Ninguna
		 * */
		@Command
		@NotifyChange("*")
		public void paginarLista(){
			int page=pagComprasCliente.getActivePage();
			cambiarCompras(page, null, null);
		} 

		/**
		 * Descripcion: Permitira filtrar por los campos del proveedor
		 * Parametros: @param view: formularioVerificarRequerimiento.zul    
		 * Retorno: Ninguno
		 * Nota: Ninguna
		 * */
		@Command
		@NotifyChange("*")
		public void aplicarFiltro()
		{
			cambiarCompras(0,null,null);
	
			if(this.compraFiltro != null) {
				this.listaComprasFiltro = new ArrayList<Compra>();
				for (Compra c : listaCompras) {
					if( ( compraFiltro.getIdCompra()==null || compraFiltro.getIdCompra().toString().isEmpty() || c.getIdCompra().toString().contains(compraFiltro.getIdCompra().toString())) &&
					    ( getRequerimientoIdFiltro()==null || getRequerimientoIdFiltro().toString().isEmpty() || c.getRequerimiento().getIdRequerimiento().toString().contains(getRequerimientoIdFiltro())) &&
					    ( getFechaCreacionFiltro()==null || getFechaCreacionFiltro().toString().isEmpty() ||formatter.format(c.getFechaCreacion()).contains(getFechaCreacionFiltro())) &&
					    ( compraFiltro.getPrecioVenta()==null || compraFiltro.getPrecioVenta().toString().isEmpty() || c.getPrecioVenta().toString().contains(compraFiltro.getPrecioVenta().toString())) &&
					    ( getEstatusFiltro()==null || getEstatusFiltro().isEmpty() || c.determinarEstatus().contains(getEstatusFiltro()))
					  ) {
						listaComprasFiltro.add(c);
					}
				}
				
				this.listaCompras = listaComprasFiltro;
			} else {
				listaComprasFiltro.clear();
				listaComprasFiltro.addAll(listaCompras);
			}
		}
		
		
		/**
		 * Descripcion: Permite cargar la vista para procesar el pago de transferencia o deposito
		 * Parametros: Requerimiento: requerimiento @param view: formularioPagoTransferenciaDeposito.zul 
		 * Retorno: Ninguno
		 * Nota: Ninguna
		 * */
		@Command
		public void pagarTransferenciaDeposito(@BindingParam("compra") Compra compra){
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("compra", compra);
			llamarFormulario("/pago/formularioPagoTransferenciaDeposito.zul", parametros);
		}
		
		private void llamarFormulario(String ruta, Map<String, Object> parametros){
			crearModal(BasePackageSistemaFunc+ruta, parametros);
		}
		
		public Boolean mostrarIcono(String estatus, String estatus2 ){
			return estatus.equalsIgnoreCase(estatus2);
		}


		/**METODOS PROPIOS DE LA CLASE*/
		
		/**GETTERS Y SETTERS*/
		public STransaccion getsTransaccion() {
			return sTransaccion;
		}

		public void setsTransaccion(STransaccion sTransaccion) {
			this.sTransaccion = sTransaccion;
		}

		public List<Compra> getListaCompras() {
			return listaCompras;
		}

		public void setListaCompras(List<Compra> listaCompras) {
			this.listaCompras = listaCompras;
		}
		
		public Cliente getCliente() {
			return cliente;
		}

		public void setCliente(Cliente cliente) {
			this.cliente = cliente;
		}

		public Compra getCompraFiltro() {
			return compraFiltro;
		}

		public void setCompraFiltro(Compra compraFiltro) {
			this.compraFiltro = compraFiltro;
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

		public Date getFechaCreacion() {
			return fechaCreacion;
		}

		public void setFechaCreacion(Date fechaCreacion) {
			this.fechaCreacion = fechaCreacion;
		}
		
		public List<Compra> getListaComprasFiltro() {
			return listaComprasFiltro;
		}

		public void setListaComprasFiltro(List<Compra> listaComprasFiltro) {
			this.listaComprasFiltro = listaComprasFiltro;
		}
		
		public String getRequerimientoIdFiltro() {
			return requerimientoIdFiltro;
		}

		public void setRequerimientoIdFiltro(String requerimientoIdFiltro) {
			this.requerimientoIdFiltro = requerimientoIdFiltro;
		}

	
		public String getFechaCreacionFiltro() {
			return fechaCreacionFiltro;
		}

		public void setFechaCreacionFiltro(String fechaCreacionFiltro) {
			this.fechaCreacionFiltro = fechaCreacionFiltro;
		}
		
	
		public String getEstatusFiltro() {
			return estatusFiltro;
		}

		public void setEstatusFiltro(String estatusFiltro) {
			this.estatusFiltro = estatusFiltro;
		}
		
		public String getCedula() {
			return cedula;
		}

		public void setCedula(String cedula) {
			this.cedula = cedula;
		}

}
