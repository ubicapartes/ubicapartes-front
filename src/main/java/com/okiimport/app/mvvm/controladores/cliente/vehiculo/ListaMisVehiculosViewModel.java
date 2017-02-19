package com.okiimport.app.mvvm.controladores.cliente.vehiculo;

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
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SortEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;

import com.okiimport.app.model.Cliente;
import com.okiimport.app.model.MarcaVehiculo;
import com.okiimport.app.model.Motor;
import com.okiimport.app.model.Usuario;
import com.okiimport.app.model.Vehiculo;
import com.okiimport.app.model.enumerados.EEstatusGeneral;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.maestros.SMaestros;
import com.okiimport.app.service.transaccion.STransaccion;

public class ListaMisVehiculosViewModel extends AbstractRequerimientoViewModel implements EventListener<SortEvent>{

	
	//Servicios
		@BeanInjector("sMaestros")
		private SMaestros sMaestros;
		@BeanInjector("sTransaccion")
		private STransaccion sTransaccion;
		
		//GUI
		@Wire("#gridMisVehiculos")
		private Listbox gridMisVehiculos;
		
		@Wire("#pagMisVehiculos")
		private Paging pagMisVehiculos;
		
		//Modelos
		private List<Vehiculo> vehiculos;
		private Vehiculo vehiculoFiltro;
		private Cliente cliente;
		
		Window window = null;
		int idcount = 0;
		
		/**
		 * Descripcion: Llama a inicializar la clase 
		 * Parametros: @param view: listaMisVehiculos.zul 
		 * Retorno: Ninguno
		 * Nota: Ninguna
		 * */
		@AfterCompose
		public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
			super.doAfterCompose(view);
			UserDetails user = this.getUser();
			Usuario usuario = sControlUsuario.consultarUsuario(user.getUsername(), user.getPassword(), null);
			cliente = (Cliente) usuario.getPersona();
			vehiculoFiltro = new Vehiculo();
			vehiculoFiltro.setMotor(new Motor());
			vehiculoFiltro.setMarcaVehiculo(new MarcaVehiculo());
			vehiculoFiltro.setCliente(cliente);
			pagMisVehiculos.setPageSize(pageSize);
			agregarGridSort(gridMisVehiculos);
			cambiarVehiculos(0, null, null);
		}
		
		
		/**Interface: EventListener<SortEvent>*/
		@Override
		@NotifyChange("vehiculos")
		public void onEvent(SortEvent event) throws Exception {
			// TODO Auto-generated method stub		
			if(event.getTarget() instanceof Listheader){
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("fieldSort",  event.getTarget().getId().toString());
				parametros.put("sortDirection", event.isAscending());
				ejecutarGlobalCommand("cambiarVehiculos", parametros );
			}
			
		}
		
		/**GLOBAL COMMAND*/
		/**
		 * Descripcion: Llama a consultar analistas  
		 * Parametros: @param view: listaMisVehiculos.zul 
		 * Retorno: Ninguno
		 * Nota: Ninguna
		 * */
		@GlobalCommand
		@NotifyChange("vehiculos")
		public void cambiarVehiculos(@Default("0") @BindingParam("page") int page, 
				@BindingParam("fieldSort") String fieldSort, 
				@BindingParam("sortDirection") Boolean sortDirection){
			Map<String, Object> parametros = sMaestros.consultarVehiculosPorCliente(vehiculoFiltro, page, pageSize);
			Integer total = (Integer) parametros.get("total");
			vehiculos = (List<Vehiculo>) parametros.get("vehiculos");
			pagMisVehiculos.setActivePage(page);
			pagMisVehiculos.setTotalSize(total);
		}     
		
		/**
		 * Descripcion: Llama a cerrar a vista
		 * Parametros: @param view: listaMisVehiculos.zul 
		 * Retorno: Ninguno
		 * Nota: Ninguna
		 * */
		@Command
		@NotifyChange("*")
		public void cerrarvista(){
			
			cambiarVehiculos(0, null, null);
		}
		
		/**COMMAND*/
		/**
		 * Descripcion: Permitira cambiar la paginacion de acuerdo a la pagina activa del Paging 
		 * Parametros: @param view: listaMisVehiculos.zul  
		 * Retorno: Ninguno
		 * Nota: Ninguna
		 * */
		@Command
		@NotifyChange("*")
		public void paginarLista(){
			int page=pagMisVehiculos.getActivePage();
			cambiarVehiculos(page, null, null);
		}
		
		/**
		 * Descripcion: Llama a un modal para ver los datos del vehiculo
		 * Parametros: Vehiculo @param view: listaMisVehiculos.zul 
		 * Retorno: Ninguno
		 * Nota: Ninguna
		 * */
		@Command
		public void verVehiculo(@BindingParam("vehiculo") Vehiculo vehiculo){
			
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("vehiculo", vehiculo);
				parametros.put("recordMode", "READ");
				parametros.put("cerrar", false);
				Sessions.getCurrent().setAttribute("allmyvalues", parametros);
				if (window != null) {
					window.detach();
					window.setId(null);
				}
				window = this.crearModal(BasePackageSistemaFunc+"usuario/formularioVehiculo.zul", parametros);
				window.setMaximizable(true);
				window.doModal();
				window.setId("doModal" + "" + idcount + "");
		}
		
		/**
		 * Descripcion: Llama a un modal para editar los datos del vehiculo
		 * Parametros: Vehiculo @param view: listaMisVehiculos.zul 
		 * Retorno: Ninguno
		 * Nota: Ninguna
		 * */
		@Command
		public void editarVehiculo(@BindingParam("vehiculo") Vehiculo vehiculo){
			
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("vehiculo", vehiculo);
				parametros.put("recordMode", "EDIT");
				parametros.put("valor", "editar");
				//parametros.put("editar", true);
				llamarFormulario("usuario/formularioVehiculo.zul", parametros);
		}
		
		/**
		 * Descripcion: Llama a un modal para crear o registrar un vehiculo
		 * Parametros: @param view: listaMisVehiculos.zul 
		 * Retorno: Ninguno
		 * Nota: Ninguna
		 * */
		@Command
		public void nuevoVehiculo(){
			Map<String, Object> parametros = new HashMap<String, Object>();

			parametros.put("recordMode", "NEW");
			llamarFormulario("usuario/formularioVehiculo.zul", parametros);
		}
		
		/**
		 * Descripcion: Metodo de la clase que permite llamar formularios 
		 * Parametros: @param view: listaMisVehiculos.zul 
		 * Retorno: Ninguno
		 * Nota: Ninguna
		 * */
		private void llamarFormulario(String ruta, Map<String, Object> parametros){
			crearModal(BasePackageSistemaFunc+ruta, parametros);
		}
		
		/**
		 * Descripcion: Llama a un modal para eliminar los datos del vehiculo
		 * Parametros: Vehiculo @param view: listaMisVehiculos.zul 
		 * Retorno: Ninguno
		 * Nota: Ninguna
		 * */
		@Command
		public void eliminarVehiculo(@BindingParam("vehiculo") final Vehiculo vehiculo){
			super.mostrarMensaje("Confirmacion", "�Desea Eliminar el Vehiculo?", Messagebox.EXCLAMATION, new Messagebox.Button[]{Messagebox.Button.YES,Messagebox.Button.NO}, 
					new EventListener(){

						@Override
						public void onEvent(Event event) throws Exception {
							// TODO Auto-generated method stub
							Messagebox.Button button = (Messagebox.Button) event.getData();
							if (button == Messagebox.Button.YES) {
								
									vehiculo.setEstatus(EEstatusGeneral.INACTIVO);
									sMaestros.registrarVehiculo(vehiculo);
									cambiarVehiculos(0, null, null);
									notifyChange("vehiculos");
								}
								
					}
						
				
			}, null);
		}
		
		/**
		 * Descripcion: Permitira filtrar por los campos del analista
		 * Parametros: @param view: listaMisVehiculos.zul    
		 * Retorno: Ninguno
		 * Nota: Ninguna
		 * */
		@Command
		@NotifyChange("*")
		public void aplicarFiltro(){
			cambiarVehiculos(0, null, null);
		}
		
		/**METODOS PROPIOS DE LA CLASE*/
		
		/**SETTERS Y GETTERS*/
		
		public SMaestros getsMaestros() {
			return sMaestros;
		}


		public void setsMaestros(SMaestros sMaestros) {
			this.sMaestros = sMaestros;
		}


		public Listbox getGridMisVehiculos() {
			return gridMisVehiculos;
		}


		public void setGridMisVehiculos(Listbox gridMisVehiculos) {
			this.gridMisVehiculos = gridMisVehiculos;
		}


		public Paging getPagMisVehiculos() {
			return pagMisVehiculos;
		}


		public void setPagMisVehiculos(Paging pagMisVehiculos) {
			this.pagMisVehiculos = pagMisVehiculos;
		}


		public List<Vehiculo> getVehiculos() {
			return vehiculos;
		}


		public void setVehiculos(List<Vehiculo> vehiculos) {
			this.vehiculos = vehiculos;
		}


		public Vehiculo getVehiculoFiltro() {
			return vehiculoFiltro;
		}


		public void setVehiculoFiltro(Vehiculo vehiculoFiltro) {
			this.vehiculoFiltro = vehiculoFiltro;
		}


		public Cliente getCliente() {
			return cliente;
		}


		public void setCliente(Cliente cliente) {
			this.cliente = cliente;
		}
	
}
