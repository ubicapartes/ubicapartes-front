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
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SortEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;

import com.okiimport.app.model.Cliente;
import com.okiimport.app.model.MarcaVehiculo;
import com.okiimport.app.model.Motor;
import com.okiimport.app.model.Usuario;
import com.okiimport.app.model.Vehiculo;
import com.okiimport.app.model.enumerados.EEstatusGeneral;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.model.ModeloCombo;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.maestros.SMaestros;
import com.okiimport.app.service.transaccion.STransaccion;

public class RegistrarVehiculoViewModel extends AbstractRequerimientoViewModel
		implements EventListener<Event> {

	// Servicios
	@BeanInjector("sTransaccion")
	private STransaccion sTransaccion;
	@BeanInjector("sMaestros")
	private SMaestros sMaestros;
	// GUI
	@Wire("#winFormularioVehiculo")
	private Window winFormularioVehiculo;

	@Wire("#bdMotor")
	private Bandbox bdMotor;

	@Wire("#pagMotores")
	private Paging pagMotores;

	@Wire("#gridMotores")
	private Listbox gridMotores;

	// Atributos
	private Vehiculo vehiculo;
	private List<MarcaVehiculo> listaMarcasVehiculo;
	private List<Motor> listaMotores;
	private List<ModeloCombo<Boolean>> listaTraccion;
	private ModeloCombo<Boolean> traccion;
	private List<ModeloCombo<Boolean>> listaTransmision;
	private ModeloCombo<Boolean> transmision;
	private Motor motor;
	private String recordMode;
	private Boolean cerrar;
	private String valor = null;
	private boolean makeAsReadOnly;
	private Cliente cliente;

	/**
	 * Descripcion: Llama a inicializar la clase Parametros: @param view:
	 * formularioVehiculo.zul Retorno: Ninguno Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("vehiculo") Vehiculo vehiculo,
			@ExecutionArgParam("recordMode") String recordMode,
			@ExecutionArgParam("valor") String valor,
			@ExecutionArgParam("cerrar") Boolean cerrar) {
		super.doAfterCompose(view);
		this.recordMode = (recordMode == null) ? "EDIT" : recordMode;
		this.vehiculo = (vehiculo == null) ? new Vehiculo() : vehiculo;
		this.traccion = new ModeloCombo<Boolean>(this.vehiculo.determinarTraccion(), this.vehiculo.getTraccion());
		this.transmision = new ModeloCombo<Boolean>(this.vehiculo.determinarTransmision(), this.vehiculo.getTransmision());
		this.cerrar = (cerrar == null) ? true : cerrar;
		makeAsReadOnly = (recordMode != null && recordMode.equalsIgnoreCase("READ")) ? true : false;
		this.valor = valor;
		this.motor = new Motor();
		agregarGridSort(gridMotores);
		pagMotores.setPageSize(pageSize = 3);

		listaMarcasVehiculo = (List<MarcaVehiculo>) sMaestros.consultarMarcas(
				0, -1).get("marcas");
		listaTraccion = llenarListaTraccion();
		listaTransmision = llenarListaTransmision();
		UserDetails user = this.getUser();
		Usuario usuario = sControlUsuario.consultarUsuario(user.getUsername(),
				user.getPassword(), null);
		cliente = (Cliente) usuario.getPersona();

		cambiarMotores(0, null, null);
	}
	

	/**
	 * Descripcion: Permite Registrar el vehiculo Parametros: @param view:
	 * formularioVehiculo.zul Retorno: Ninguno Nota: Ninguna
	 * */
	@Command
	public void registrar(@BindingParam("btnEnviar") Button btnEnviar,
			@BindingParam("btnLimpiar") Button btnLimpiar,
			@BindingParam("recordMode") String recordMode,
			@BindingParam("edicion") String valor) {
		try {
			if (checkIsFormValid()) {

				btnEnviar.setDisabled(true);
				btnLimpiar.setDisabled(true);

				vehiculo.setCliente(cliente);
				if(traccion!=null)
					vehiculo.setTraccion(traccion.getValor());
				if(transmision!=null)
					vehiculo.setTransmision(transmision.getValor());
				vehiculo.setEstatus(EEstatusGeneral.ACTIVO);

				sTransaccion.actualizarVehiculo(vehiculo);

				// El Objecto que se envia debe declararse final, esto quiere
				// decir que no puede instanciarse sino solo una vez
			
				this.valor = valor;
				if (this.valor != null)
					mostrarMensaje("Informaci\u00F3n",
							"Vehiculo Actualizado con Exito", null, null, null,
							null);
				else
					mostrarMensaje("Informaci\u00F3n",
							"Vehiculo Registrado con Exito", null, null, null,
							null);
				this.setValor(null);
				this.recargar();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Descripcion: Permite recargar la pantalla al cerrar
	 * Parametros: @param view: formularioAnalistas.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	public void recargar() {
		winFormularioVehiculo.onClose();
		ejecutarGlobalCommand("cambiarVehiculos", null);
	}

	/** COMMAND */
	/**
	 * Descripcion: Permite limpiar los campos del formulario registrar vehiculo
	 * Parametros: @param view: formularioVehiculo.zul Retorno: Ninguno Nota:
	 * Ninguna
	 * */
	@Command
	@NotifyChange({ "*" })
	public void limpiar() {
		try {
			vehiculo = new Vehiculo();
			vehiculo.setCliente(cliente);
			traccion = new ModeloCombo<Boolean>();
			transmision = new ModeloCombo<Boolean>();
			super.cleanConstraintForm();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** INTERFACE */
	// 1.EventListener<Event>
	@Override
	public void onEvent(Event event) throws Exception {
		if (event instanceof ClickEvent) {
			winFormularioVehiculo.detach();
			ejecutarGlobalCommand("cambiarVehiculos", null);
		} else if (event instanceof SortEvent) {
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("fieldSort", ((Listheader) event.getTarget())
					.getValue().toString());
			parametros.put("sortDirection", ((SortEvent) event).isAscending());
			ejecutarGlobalCommand("cambiarMotores", parametros);
		}
	}

	/** GLOBAL COMMAND */
	@GlobalCommand
	@SuppressWarnings("unchecked")
	@NotifyChange({ "listaMotores", "pagMotores" })
	public void cambiarMotores(@Default("0") @BindingParam("page") int page,
			@BindingParam("fieldSort") String fieldSort,
			@BindingParam("sortDirection") Boolean sortDirection) {
		Map<String, Object> parametros = sMaestros.consultarMotores(motor,
				fieldSort, sortDirection, page, pageSize);
		Integer total = (Integer) parametros.get("total");
		listaMotores = (List<Motor>) parametros.get("motores");
		pagMotores.setActivePage(page);
		pagMotores.setTotalSize(total);
	}

	@Command
	@NotifyChange("listaMotores")
	public void paginarListaMotores() {
		int page = pagMotores.getActivePage();
		cambiarMotores(page, null, null);
	}

	@Command
	@NotifyChange("listaMotores")
	public void aplicarFiltroMotor() {
		cambiarMotores(0, null, null);
	}

	// METODOS PROPIOS DE LA CLASE

	public STransaccion getsTransaccion() {
		return sTransaccion;
	}

	public void setsTransaccion(STransaccion sTransaccion) {
		this.sTransaccion = sTransaccion;
	}

	public SMaestros getsMaestros() {
		return sMaestros;
	}

	public void setsMaestros(SMaestros sMaestros) {
		this.sMaestros = sMaestros;
	}

	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

	public List<MarcaVehiculo> getListaMarcasVehiculo() {
		return listaMarcasVehiculo;
	}

	public void setListaMarcasVehiculo(List<MarcaVehiculo> listaMarcasVehiculo) {
		this.listaMarcasVehiculo = listaMarcasVehiculo;
	}

	public List<ModeloCombo<Boolean>> getListaTraccion() {
		return listaTraccion;
	}

	public void setListaTraccion(List<ModeloCombo<Boolean>> listaTraccion) {
		this.listaTraccion = listaTraccion;
	}

	public ModeloCombo<Boolean> getTraccion() {
		return traccion;
	}

	public void setTraccion(ModeloCombo<Boolean> traccion) {
		this.traccion = traccion;
	}

	public List<ModeloCombo<Boolean>> getListaTransmision() {
		return listaTransmision;
	}

	public void setListaTransmision(List<ModeloCombo<Boolean>> listaTransmision) {
		this.listaTransmision = listaTransmision;
	}

	public ModeloCombo<Boolean> getTransmision() {
		return transmision;
	}

	public void setTransmision(ModeloCombo<Boolean> transmision) {
		this.transmision = transmision;
	}

	public String getRecordMode() {
		return recordMode;
	}

	public void setRecordMode(String recordMode) {
		this.recordMode = recordMode;
	}

	public boolean isMakeAsReadOnly() {
		return makeAsReadOnly;
	}

	public void setMakeAsReadOnly(boolean makeAsReadOnly) {
		this.makeAsReadOnly = makeAsReadOnly;
	}

	public Boolean getCerrar() {
		return cerrar;
	}

	public void setCerrar(Boolean cerrar) {
		this.cerrar = cerrar;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public List<Motor> getListaMotores() {
		return listaMotores;
	}

	public void setListaMotor(List<Motor> listaMotores) {
		this.listaMotores = listaMotores;
	}

	public Motor getMotor() {
		return motor;
	}

	public void setMotor(Motor motor) {
		this.motor = motor;
	}

}
