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
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

import com.okiimport.app.model.Cliente;
import com.okiimport.app.model.DetalleRequerimiento;
import com.okiimport.app.model.Estado;
import com.okiimport.app.model.Motor;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.model.Usuario;
import com.okiimport.app.model.Vehiculo;
import com.okiimport.app.mvvm.carga_masiva.PDDetalleRequerimientoEstrategy;
import com.okiimport.app.mvvm.model.ModeloCombo;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.mail.MailCliente;
import com.okiimport.app.service.transaccion.STransaccion;

public class RegistrarRequerimientoViewModel extends AbstractCargaMasivaViewModel {

	//Servicios
	@BeanInjector("sTransaccion")
	private STransaccion sTransaccion;
	@BeanInjector("mailCliente")
	private MailCliente mailCliente;
	
	// GUI
	@Wire("#cedulaRif")
	public Intbox cedulaRif;
	
	@Wire("#annoV")
	private Datebox annoV;
	
	@Wire("#comboTipoPersona")
	private Combobox comboTipoPersona;
	
	@Wire("#msgCorreo")
	private Label lblMsgCorreo;
	
	@Wire("#winERequerimiento")
	private Window winERequerimiento;
	

	//Atributos
	private List<DetalleRequerimiento> eliminarDetalle;
	
	private List<Vehiculo> vehiculos;
	private List<ModeloCombo<Boolean>> listaTipoRepuesto;
	private ModeloCombo<Boolean> tipoRepuesto;
	private Requerimiento requerimiento;
	private Cliente cliente;
	private Motor motor;
	private Vehiculo vehiculo;
	protected static final String BaseURL = "/WEB-INF/views/portal/";

	/**
	 * Descripcion: Llama a inicializar la clase 
	 * Parametros: @param view: formularioRequerimiento.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view) {
		super.doAfterCompose(view);
		UserDetails user = this.getUser();
		Usuario usuario = sControlUsuario.consultarUsuario(user.getUsername(), user.getPassword(), null);
		cliente = (Cliente) usuario.getPersona();
		
		limpiar();
		//tipoPersona = listaTipoPersona.get(1);
		listaTipoRepuesto = llenarListaTipoRepuesto();
		vehiculos = (List<Vehiculo>) sMaestros.consultarVehiculos(new Vehiculo(cliente), 0, -1).get("vehiculos");
	}
	
	/**GLOBAL COMMAND*/
	@GlobalCommand
	@NotifyChange("limpiarCamposReq")
	public void limpiarCamposRequerimiento(){
		//System.out.println("entre al LIMPIAR");
		recargar();
//		motor = new Motor();
//		requerimiento = new Requerimiento();
//		cliente = new Cliente();
//		requerimiento.setCliente(cliente);
//		super.cleanConstraintForm();
		
		
		//limpiar();
	}
	
	
	/**COMMAND*/
	/**
	 * Descripcion: Permite limpiar los campos del formulario registrar Requerimiento
	 * Parametros: @param view: formularioRequerimiento.zul 
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange({ "requerimiento", "cliente", "tipoRepuesto", "vehiculo" })
	public void limpiar() {
		try{
			vehiculo = null;
			requerimiento = new Requerimiento();
			requerimiento.setCliente(cliente);
			tipoRepuesto=new ModeloCombo<Boolean>();
			super.cleanConstraintForm();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	 /**
		 * Descripcion: Permite Registrar el requerimiento
		 * Parametros: @param view: formularioRequerimiento.zul  
		 * Retorno: Ninguno
		 * Nota: Ninguna
		 * */
	@Command
	public void registrar(@BindingParam("btnEnviar") Button btnEnviar,
			@BindingParam("btnLimpiar") Button btnLimpiar) {
		try{
			if (checkIsFormValid()) {
				if (requerimiento.getDetalleRequerimientos().size() > 0) {
					btnEnviar.setDisabled(true);
					btnLimpiar.setDisabled(true);

					requerimiento.setCliente(cliente);
					requerimiento.setAnnoV(vehiculo.getAnno());
					requerimiento.setModeloV(vehiculo.getModelo());
					requerimiento.setMotor(vehiculo.getMotor());
					requerimiento.setMarcaVehiculo(vehiculo.getMarcaVehiculo());
					requerimiento.setTraccionV(vehiculo.getTraccion());
					requerimiento.setTransmisionV(vehiculo.getTransmision());
					requerimiento.setSerialCarroceriaV(vehiculo.getSerialCarroceria());
					
					if (tipoRepuesto != null)
						requerimiento.setTipoRepuesto(tipoRepuesto.getValor());

					sTransaccion.registrarRequerimiento(requerimiento, true, sMaestros);

					// El Objecto que se envia debe declararse final, esto quiere
					// decir que no puede instanciarse sino solo una vez

					mailCliente.registrarRequerimiento(requerimiento, mailService);

					Map<String, Object> parametros = new HashMap<String, Object>();
					crearModal(BaseURL+"avisoRequerimientoRegistrado.zul", parametros);
					ejecutarGlobalCommand("cambiarRequerimientos", null);
					winERequerimiento.onClose();

				} else
					mostrarMensaje("Informaci\u00F3n",
							"Agregue al Menos un Requerimiento", null, null, null,
							null);
			}

		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	 private void ejecutarGlobalCommand(String string) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Descripcion: Permite poder agregar un nuevo repuesto al requerimiento
	 * Parametros: @param view: formularioRequerimiento.zul  
	 * Retorno: Ninguno
	 * Nota: Ninguna
	 * */
	@Command
	@NotifyChange({ "requerimiento", "cliente" })
	public void agregarRepuesto() {
		if (requerimiento.getDetalleRequerimientos().size() < 10)
			requerimiento.addDetalleRequerimiento(new DetalleRequerimiento());
	}

	 /**
		 * Descripcion: Permite poder eliminar un repuesto del requerimiento
		 * Parametros: @param view: formularioRequerimiento.zul  
		 * Retorno: Ninguno
		 * Nota: Ninguna
		 * */
	@Command
	@NotifyChange({ "requerimiento", "cliente" })
	public void eliminarRepuesto() {
		if (eliminarDetalle != null) {
			for (DetalleRequerimiento detalle : eliminarDetalle)
				requerimiento.removeDetalleRequerimiento(detalle);
		}

	}
	
	@Command
	@NotifyChange("requerimiento")
	public void cargarArchivoRepuesto(){
		Fileupload.setTemplate("/WEB-INF/views/sistema/configuracion/fileupload.zul");
		Fileupload.get(new EventListener<UploadEvent>(){

			@Override
			public void onEvent(UploadEvent event) throws Exception {
				onUpload(new PDDetalleRequerimientoEstrategy(), event, "requerimiento");
				notifyChange("*");
			}
		});
		
	}
	
	@Command
	public void descargarArchivoRepuesto(){
		super.download(BaseResources+"documentos/Formato Cargar Requerimientos.rar");
	}
	
	/**METODOS OVERRIDE*/
	/**CARGA MASIVA*/
	@Override
	protected void prepararCarga() {
		requerimiento.removeAllDetalleRequerimiento();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void archivosProcesados(List<?> registros, Media media, Component component) {
		List<DetalleRequerimiento> detalles = (List<DetalleRequerimiento>) registros;
		for(DetalleRequerimiento detalle : detalles){
			requerimiento.addDetalleRequerimiento(detalle);
		}
	}

	@Override
	protected void archivoVacio(Component component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void archivoNoValido(Component component) {
		// TODO Auto-generated method stub
		
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	
	
	/**GETTERS Y SETTERS*/
	public Requerimiento getRequerimiento() {
		return requerimiento;
	}

	public void setRequerimiento(Requerimiento requerimiento) {
		this.requerimiento = requerimiento;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public STransaccion getsTransaccion() {
		return sTransaccion;
	}

	public void setsTransaccion(STransaccion sTransaccion) {
		this.sTransaccion = sTransaccion;
	}

	public void recargar() {
		redireccionar("/");
	}

	public List<DetalleRequerimiento> getEliminarDetalle() {
		return eliminarDetalle;
	}

	public void setEliminarDetalle(List<DetalleRequerimiento> eliminarDetalle) {
		this.eliminarDetalle = eliminarDetalle;
	}

	public List<ModeloCombo<Boolean>> getListaTipoRepuesto() {
		return listaTipoRepuesto;
	}

	public void setListaTipoRepuesto(
			List<ModeloCombo<Boolean>> listaTipoRepuesto) {
		this.listaTipoRepuesto = listaTipoRepuesto;
	}

	public ModeloCombo<Boolean> getTipoRepuesto() {
		return tipoRepuesto;
	}

	public void setTipoRepuesto(ModeloCombo<Boolean> tipoRepuesto) {
		this.tipoRepuesto = tipoRepuesto;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public MailCliente getMailCliente() {
		return mailCliente;
	}

	public void setMailCliente(MailCliente mailCliente) {
		this.mailCliente = mailCliente;
	}

	public Motor getMotor() {
		return motor;
	}

	public void setMotor(Motor motor) {
		this.motor = motor;
	}

	public List<Vehiculo> getVehiculos() {
		return vehiculos;
	}

	public void setVehiculos(List<Vehiculo> vehiculos) {
		this.vehiculos = vehiculos;
	}

	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}
	
	
	
}
