package com.okiimport.app.mvvm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Default;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.Button;

import com.okiimport.app.model.Ciudad;
import com.okiimport.app.model.DetalleRequerimiento;
import com.okiimport.app.model.Estado;
import com.okiimport.app.model.Pais;
import com.okiimport.app.model.Usuario;
import com.okiimport.app.model.enumerados.EEstatusRequerimiento;
//Constraint
import com.okiimport.app.mvvm.constraint.AnnoConstraint;
import com.okiimport.app.mvvm.constraint.CustomConstraint;
import com.okiimport.app.mvvm.constraint.CustomConstraint.EConstraint;
import com.okiimport.app.mvvm.constraint.EqualsAndIntervalValueConstraint;
import com.okiimport.app.mvvm.constraint.GeneralConstraint;
import com.okiimport.app.mvvm.constraint.MayorCantidadConstraint;
import com.okiimport.app.mvvm.constraint.RegExpressionConstraint;
import com.okiimport.app.mvvm.constraint.RegExpressionConstraint.RegExpression;
import com.okiimport.app.mvvm.model.FormatedMonedaConverter;
import com.okiimport.app.mvvm.model.FormatedNumberConverter;
import com.okiimport.app.mvvm.model.ModeloCombo;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.resource.model.ICoverterMoneda;
import com.okiimport.app.service.configuracion.SControlConfiguracion;
import com.okiimport.app.service.configuracion.SControlUsuario;
import com.okiimport.app.service.maestros.SMaestros;
import com.okiimport.app.service.mail.MailService;

public abstract class AbstractRequerimientoViewModel extends AbstractViewModel {
	
	protected static final String BasePackagePortal = BaseApp+"portal/";
	protected static final String BasePackageSistema = BaseApp+"sistema/";
	protected static final String BasePackageSistemaFunc = BasePackageSistema+"funcionalidades/";
	protected static final String BasePackageSistemaMaest = BasePackageSistema+"maestros/";
	
	private static final String RUTA_MESSAGEBOX = BasePackageSistema+"configuracion/messagebox.zul";
	
	protected static final String formatDate = "dd/MM/yyyy";
	protected static final String formatNumber = "#,###.00";
	protected static final String localeNumber = null;
	
	//Converter
	@BeanInjector("formatedNumber")
	protected FormatedNumberConverter formatedNumber;
	
	@BeanInjector("formatedMoneda")
	protected FormatedMonedaConverter formatedMoneda;
	
	// Servicios
	@BeanInjector("mailService")
	protected MailService mailService;

	@BeanInjector("sMaestros")
	protected SMaestros sMaestros;
	
	@BeanInjector("sControlConfiguracion")
	protected SControlConfiguracion sControlConfiguracion;
	
	@BeanInjector("sControlUsuario")
	protected SControlUsuario sControlUsuario;

	// Atributos
	protected Calendar calendar = GregorianCalendar.getInstance();

	protected List<Ciudad> listaCiudades;

	protected Estado estado;
	
	protected ICoverterMoneda monedaBase;
	
	protected int pageSize = 10;

	/** COMMADN */
	@Command
	@NotifyChange("*")
	public void cambiarFoto(@BindingParam("media") Media media,
			@BindingParam("detalle") DetalleRequerimiento detalle) {
		if (media instanceof org.zkoss.image.Image)
			detalle.setFoto(((org.zkoss.image.Image) media).getByteData());
		else if (media != null)
			mostrarMensaje("Error", "No es una imagen: " + media, null, null,
					null, null);
	}
	
	@Command
	@SuppressWarnings("unchecked")
	public List<Pais> llenarListaPaises(){
		return (List<Pais>) sMaestros.consultarPaises(0, -1).get("paises");
	}

	@Command
	@SuppressWarnings("unchecked")
	public List<Estado> llenarListaEstados() {
		return (List<Estado>) sMaestros.consultarEstados(0, -1).get("estados");
	}

	@Command
	@SuppressWarnings("unchecked")
	@NotifyChange({ "listaCiudades" })
	public void buscarCiudades() {
		if (this.estado != null)
			listaCiudades = (List<Ciudad>) sMaestros.ConsultarCiudad(
					estado.getIdEstado(), 0, -1).get("ciudades");
	}

	@Command
	public void ampliarImagen(
			@Default("Titulo") @BindingParam("titulo") String titulo,
			@BindingParam("imagen") String imagen) {
		if(imagen!=null){
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("title", titulo);
			parametros.put("image", imagen);
			crearModal(BasePackageSistema+"configuracion/ampliarImagen.zul",
					parametros);
		}
		else
			mostrarMensaje("Error", "No es una imagen valida para mostrar", Messagebox.ERROR, null, null, null);
	}

	/** METODOS SOBREESCRITOS */
	@Override
	public void doAfterCompose(Component view){
		
		try
		{super.doAfterCompose(view);
		Usuario usuario = getUsuario();
		if(usuario!=null)
			monedaBase = sControlConfiguracion.consultarActualConversion(usuario.getPersona());
		else
			monedaBase = sControlConfiguracion.consultarActualConversionMonedaBase();
		} catch (Exception e) {
		    System.out.println(e.getMessage());
		}
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public void mostrarMensaje(String titulo, String mensaje, String icon,
			Button[] botones, EventListener clickEvent,
			Map<String, String> params) {
		Messagebox.setTemplate(RUTA_MESSAGEBOX);
		super.mostrarMensaje(titulo, mensaje, icon, botones, clickEvent, params);
	}

	/** METODOS PROPIOS DE LA CLASE */
	protected Usuario getUsuario(){
		UserDetails user = this.getUser();
		if(user!=null)
			return this.sControlUsuario.consultarUsuario(user.getUsername(), user.getPassword(), null);
		else
			return null;
	}
	
	protected static List<ModeloCombo<String>> llenarListaBancoPago() {
		List<ModeloCombo<String>> listaBancoPago = new ArrayList<ModeloCombo<String>>();
		listaBancoPago
				.add(new ModeloCombo<String>("Banco Exterior", "EXTERIOR"));
		listaBancoPago.add(new ModeloCombo<String>("Banco Provincial",
				"PROVINCIAL"));
		listaBancoPago.add(new ModeloCombo<String>("Banco Banesco", "BANESCO"));
		listaBancoPago.add(new ModeloCombo<String>("Banco de Venezuela",
				"BDVENEZUELA"));
		listaBancoPago.add(new ModeloCombo<String>("Banco Mercantil",
				"MERCANTIL"));
		listaBancoPago.add(new ModeloCombo<String>("Banco BOD", "BOD"));
		return listaBancoPago;
	}

	protected static List<ModeloCombo<String>> llenarListaEmpresaEncomiendas() {
		List<ModeloCombo<String>> listaEmpresaEncomiendas = new ArrayList<ModeloCombo<String>>();
		listaEmpresaEncomiendas.add(new ModeloCombo<String>("Zoom", "ZOOM"));
		listaEmpresaEncomiendas
				.add(new ModeloCombo<String>("Domesa", "DOMESA"));
		listaEmpresaEncomiendas.add(new ModeloCombo<String>("Mrw", "MRW"));
		return listaEmpresaEncomiendas;
	}

	protected static List<ModeloCombo<Boolean>> llenarListaOficinaDireccion() {
		List<ModeloCombo<Boolean>> listaOficinaDireccion = new ArrayList<ModeloCombo<Boolean>>();
		listaOficinaDireccion.add(new ModeloCombo<Boolean>(
				"Oficina Empresa Encomiendas", true));
		listaOficinaDireccion.add(new ModeloCombo<Boolean>(
				"Direccion Particular", false));
		return listaOficinaDireccion;
	}

	protected static List<ModeloCombo<Boolean>> llenarListaTraccion() {
		List<ModeloCombo<Boolean>> listaTraccion = new ArrayList<ModeloCombo<Boolean>>();
		listaTraccion.add(new ModeloCombo<Boolean>("4x2", true));
		listaTraccion.add(new ModeloCombo<Boolean>("4x4", false));
		return listaTraccion;
	}

	protected static List<ModeloCombo<Boolean>> llenarListaTransmision() {
		List<ModeloCombo<Boolean>> listaTransmision = new ArrayList<ModeloCombo<Boolean>>();
		listaTransmision.add(new ModeloCombo<Boolean>("Automatico", true));
		listaTransmision.add(new ModeloCombo<Boolean>("Sincronico", false));
		return listaTransmision;
	}

	protected static List<ModeloCombo<Boolean>> llenarListaTipoPersona() {
		List<ModeloCombo<Boolean>> listaTipoPersona = new ArrayList<ModeloCombo<Boolean>>();
		listaTipoPersona.add(new ModeloCombo<Boolean>("J", true));
		listaTipoPersona.add(new ModeloCombo<Boolean>("V", false));
		return listaTipoPersona;
	}
	
	protected static List<ModeloCombo<Boolean>> llenarListaTipoRepuestoProveedor(){
		List<ModeloCombo<Boolean>> listaTipoRepuesto = new ArrayList<ModeloCombo<Boolean>>();
		listaTipoRepuesto.add(new ModeloCombo<Boolean>("Reemplazo", true));
		listaTipoRepuesto.add(new ModeloCombo<Boolean>("Original", false));
		return listaTipoRepuesto;
	}

	protected static List<ModeloCombo<Boolean>> llenarListaTipoRepuesto() {
		List<ModeloCombo<Boolean>> listaTipoRepuesto = new ArrayList<ModeloCombo<Boolean>>();
		listaTipoRepuesto.add(new ModeloCombo<Boolean>("Indistinto", null));
		listaTipoRepuesto.addAll(llenarListaTipoRepuestoProveedor());
		return listaTipoRepuesto;
	}

	protected static List<ModeloCombo<Boolean>> llenarListaTipoProveedor() {
		List<ModeloCombo<Boolean>> listaTipoProveedor = new ArrayList<ModeloCombo<Boolean>>();
		listaTipoProveedor.add(new ModeloCombo<Boolean>("Internacional", false));
		listaTipoProveedor.add(new ModeloCombo<Boolean>("Nacional", true ));
		return listaTipoProveedor;
	}

	private static List<ModeloCombo<String>> llenarListaEstatus(
			List<EEstatusRequerimiento> listEstatus) {
		List<ModeloCombo<String>> listaEstatus = new ArrayList<ModeloCombo<String>>();
		for (EEstatusRequerimiento estatus : listEstatus)
			listaEstatus.add(new ModeloCombo<String>(estatus.getNombre(),
					estatus.getValue()));
		return listaEstatus;
	}

	protected static List<ModeloCombo<String>> llenarListaEstatusEmitidos() {
		return llenarListaEstatus(EEstatusRequerimiento.getEstatusEmitidos());
	}

	protected static List<ModeloCombo<String>> llenarListaEstatusProcesados() {
		return llenarListaEstatus(EEstatusRequerimiento.getEstatusProcesados());
	}

	protected static List<ModeloCombo<String>> llenarListaEstatusOfertados() {
		return llenarListaEstatus(EEstatusRequerimiento.getEstatusOfertados());
	}

	protected static List<ModeloCombo<String>> llenarListaEstatusGeneral() {
		return llenarListaEstatus(EEstatusRequerimiento.getEstatusGeneral());
	}

	protected static List<ModeloCombo<Boolean>> llenarTiposFleteNacional() {
		List<ModeloCombo<Boolean>> listaTiposFlete = new ArrayList<ModeloCombo<Boolean>>();
		listaTiposFlete.add(new ModeloCombo<Boolean>(
				"Incluido en el Precio de Venta", false));
		listaTiposFlete.add(new ModeloCombo<Boolean>(
				"No Incluido en el Precio de Venta", true));
		return listaTiposFlete;
	}

	protected static List<ModeloCombo<Boolean>> llenarTiposFleteInternacional() {
		List<ModeloCombo<Boolean>> listaTiposFlete = new ArrayList<ModeloCombo<Boolean>>();
		listaTiposFlete.add(new ModeloCombo<Boolean>("CIF", true));
		listaTiposFlete.add(new ModeloCombo<Boolean>("FOB", false));
		return listaTiposFlete;
	}

	protected static List<ModeloCombo<Boolean>> llenarFormasDeEnvio() {
		List<ModeloCombo<Boolean>> listaFormasEnvio = new ArrayList<ModeloCombo<Boolean>>();
		listaFormasEnvio.add(new ModeloCombo<Boolean>("Aereo", true));
		listaFormasEnvio.add(new ModeloCombo<Boolean>("Maritimo", false));
		return listaFormasEnvio;
	}

	public int getYearDay() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	public CustomConstraint getNotEmptyValidator() {
		return new GeneralConstraint(EConstraint.NO_EMPTY);
	}
	
	public CustomConstraint getFechaValidator(@Default("-1") int tipo){
		EConstraint constraint = null;
		switch(tipo){
		case 1: constraint = EConstraint.NO_FUTURE; break;
		case 2: constraint = EConstraint.NO_PAST; break;
		default: constraint = EConstraint.NO_TODAY; break;
		}
		return new GeneralConstraint(EConstraint.NO_EMPTY, constraint);
	}

	public CustomConstraint getEmailValidator() {
		RegExpression[] constrains = new RegExpression[] { new RegExpression(
				"/.+@.+\\.[a-z]+/",
				"Debe contener una direccion de correo valida.") };
		return new RegExpressionConstraint(constrains, EConstraint.NO_EMPTY,
				EConstraint.CUSTOM);
	}
	
	public CustomConstraint getValidatorCantPositiva(){
		return new GeneralConstraint(EConstraint.NO_EMPTY, EConstraint.NO_ZERO, EConstraint.NO_NEGATIVE);
	}

	public CustomConstraint getValidatorCantidad(
			@BindingParam("cantidadRequerida") Long cantidadRequerida) {
		return new MayorCantidadConstraint(cantidadRequerida);
	}

	public CustomConstraint getValidatorAnno(
			@BindingParam("minYear") Integer minYear,
			@BindingParam("maxYear") Integer maxYear) {
		return new AnnoConstraint(minYear, maxYear);
	}

	public CustomConstraint getTelefonoValidator() {
		RegExpression[] constrains = new RegExpression[] { new RegExpression(
				"/.[0-9]+/",
				"Debe Contener Un Numero Telefonico Valido Ej. 025141785289") };
		return new RegExpressionConstraint(constrains, EConstraint.NO_EMPTY,
				EConstraint.CUSTOM);

	}

	public CustomConstraint getValidatorClienteCedulaRif() {
		return new GeneralConstraint(EConstraint.NO_EMPTY,
				EConstraint.NO_NEGATIVE, EConstraint.NO_ZERO);
	}

	public CustomConstraint getCantValidator() {
		RegExpression[] constrains = new RegExpression[] { new RegExpression(
				"/.[0-9]+/", "Debe Contener Un Numero Valido") };
		return new RegExpressionConstraint(constrains, EConstraint.NO_EMPTY,
				EConstraint.NO_NEGATIVE, EConstraint.NO_ZERO,
				EConstraint.CUSTOM);

	}
	
	public CustomConstraint getCantValidatorOrEmptyString() {
		RegExpression[] constrains = new RegExpression[] { new RegExpression(
				"/^(\\s*|\\d+)$/", "Debe contener un numero valido") };
		return new RegExpressionConstraint(constrains,
				EConstraint.NO_NEGATIVE, EConstraint.NO_ZERO,
				EConstraint.CUSTOM);
	}
	
	
	public CustomConstraint getValidatorClienteCedulaRif2() {
        
        RegExpression[] constrains = new RegExpression[] { new RegExpression(
		"/.[0-9]+/",
		"Introduzca RIF o Cedula. Solo Numeros, sin Guiones Ej.: 402405374") };
        return new RegExpressionConstraint(constrains, EConstraint.NO_EMPTY, EConstraint.NO_NEGATIVE,EConstraint.NO_ZERO);
     }
	
	public CustomConstraint getValidatorPrecio() {
		
		RegExpression[] constrains = new RegExpression[] { new RegExpression(
				"/.[0-9]+/", "Debe Contener Valores Numericos Validos") };
		return new RegExpressionConstraint(constrains, EConstraint.NO_EMPTY,
				EConstraint.NO_NEGATIVE, EConstraint.NO_ZERO, EConstraint.CUSTOM);
	}
	
	
    public CustomConstraint getValidatorFechaVencimiento() {
    	return new GeneralConstraint(EConstraint.NO_EMPTY,
				EConstraint.NO_PAST );
	}
    
    public CustomConstraint getValidatorEqualsAndIntervalValue(@BindingParam("valorEqual") String valorEqual,
    		@BindingParam("minValue") Integer minValue, @BindingParam("maxValue") Integer maxValue){
    	return new EqualsAndIntervalValueConstraint(valorEqual, minValue, maxValue);
    }
    
    public void setButtonDisiabled(final org.zkoss.zul.Button button){
    	String sclass = button.getSclass();
    	if(sclass.contains("active"))
    		sclass.replace("active", "disabled");
    	else if(sclass.contains("disabled"))
    		sclass.replace("disabled", "active");
    	else
    		sclass = sclass + "active";
    	button.setSclass(sclass);
    }
    
	/**GETTERS Y SETTERS*/
    public String getFormatDate() {
		return formatDate;
	}
    
    public String getFormatNumber() {
		return formatNumber;
	}

	public String getLocaleNumber() {
		return localeNumber;
	}
	
    public FormatedNumberConverter getFormatedNumber() {
		return formatedNumber;
	}

	public void setFormatedNumber(FormatedNumberConverter formatedNumber) {
		this.formatedNumber = formatedNumber;
	}
	
	public FormatedMonedaConverter getFormatedMoneda() {
		return formatedMoneda;
	}

	public void setFormatedMoneda(FormatedMonedaConverter formatedMoneda) {
		this.formatedMoneda = formatedMoneda;
	}
	
	public MailService getMailService() {
		return mailService;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	public SMaestros getsMaestros() {
		return sMaestros;
	}

	public void setsMaestros(SMaestros sMaestros) {
		this.sMaestros = sMaestros;
	}
	
	public SControlConfiguracion getsControlConfiguracion() {
		return sControlConfiguracion;
	}
	
	public void setsControlConfiguracion(SControlConfiguracion sControlConfiguracion) {
		this.sControlConfiguracion = sControlConfiguracion;
	}
	
	public SControlUsuario getsControlUsuario() {
		return sControlUsuario;
	}

	public void setsControlUsuario(SControlUsuario sControlUsuario) {
		this.sControlUsuario = sControlUsuario;
	}

	public List<Ciudad> getListaCiudades() {
		return listaCiudades;
	}

	public void setListaCiudades(List<Ciudad> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public ICoverterMoneda getMonedaBase() {
		return monedaBase;
	}

	public void setMonedaBase(ICoverterMoneda monedaBase) {
		this.monedaBase = monedaBase;
	}
	
	
}
