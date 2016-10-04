package com.okiimport.app.mvvm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.JFileChooser;
import javax.xml.bind.DatatypeConverter;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.view.JasperViewer;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Default;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkmax.zul.Nav;
import org.zkoss.zkmax.zul.Navitem;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.LayoutRegion;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Messagebox.Button;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

import com.okiimport.app.mvvm.constraint.CustomConstraint;
import com.okiimport.app.mvvm.constraint.GeneralConstraint;
import com.okiimport.app.mvvm.constraint.CustomConstraint.EConstraint;

import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.resource.model.ModelNavbar;

public abstract class AbstractViewModel {
	
	private static final SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
	
	protected static final String BaseApp = "/WEB-INF/views/";
	protected static final String BaseResources = "/resources/";
	
	private Component form;
	
	protected HttpSession sesion;
	
	public HttpSession getSesion() {
		setSesion(sesion);
		return sesion;
	}

	public void setSesion(HttpSession sesion) {
		this.sesion = ((HttpServletRequest) Executions.getCurrent().getNativeRequest()).getSession();
		//this.sesion.setMaxInactiveInterval(40);
		this.sesion.setMaxInactiveInterval(2*60*60);
	}
	
	protected UserDetails getUser(){
		this.sesion = this.getSesion();
		Object sci = sesion.getAttribute("SPRING_SECURITY_CONTEXT");
		Object user = (sci instanceof SecurityContextImpl) ? (((SecurityContextImpl) sci).getAuthentication().getPrincipal()) : null;
		return (user instanceof UserDetails) ? ((UserDetails) user) : null; 
	}
	
	/**
	 * Descripcion: Evento que se ejecuta luego de la inicializacion de los componente
	 * Parametros: @param view: ventana asociada al viewmodel
	 * Retorno: Ninguno
	 * Nota: No se Ejecutara sino es invocado en la seccion respectiva del formulario
	 * */
	public void doAfterCompose(Component view){
		form = view;
		Selectors.wireComponents(view, this, false); 
		buildInjection(this);
	}
	
	/**
	 * Descripcion: permite verificar si el componente es valido con su respectivo constraint.
	 * Parametros: @param component: componente de la vista a evaluar.
	 * Retorno: variable boolean que indica si el componente es valido o no
	 * */
	private boolean check(Component component) {
		boolean exito = true;
		if (component instanceof InputElement)
			if (!((InputElement) component).isValid()) {
				exito = false;
				((InputElement) component).getText(); // Force show errorMessage
			}
		return exito;
	}
	
	/**
	 * Descripcion: Permitira verificar si el componente y sus hijos son validos con su respectivo constraint.
	 * Parametros: @param component: componente de la vista a evaluar.
	 * Retorno: variable boolean que indica si el componente y sus hijos son validos o no
	 * */
	protected boolean checkIsValid(Component component) {
		if(!check(component))
			return false;
		
		List<Component> children = component.getChildren();
		for (Component each: children) {
			if(!checkIsValid(each))
				return false;
		}
		return true;
	}
	
	/**
	 * Descipcion: permitira validar si el formulario y sus hijos son validos o no
	 * Parametros: Ninguno
	 * Retorno: variable boolean que indica si el formulario y sus hijos son validos o no
	 * */
	protected boolean checkIsFormValid(){
		if(form!=null)
			return checkIsValid(form);
		return true;
	}
	
	/**
	 * Descripcion: permitira limpiar los constraint de algun elemento de la vista
	 * Parametros: @param component: componente de la vista
	 * Retorno: variable boolean que define si se ha ejecutado el limpiar de la vista
	 * */
	protected boolean cleanConstraint(Component component){
		if (component instanceof InputElement) {
			Constraint constraint = ((InputElement) component).getConstraint();
			if(constraint instanceof CustomConstraint){
				((CustomConstraint)constraint).hideComponentError();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Descripcion: permitira limpiar los constraint del componente de la vista pasado como parametro
	 * y los hijos asociados a este
	 * Parametros: @param component: componente de la vista
	 * Retorno: Ninguno
	 * */
	protected void cleanConstraintComponent(Component component){	
		cleanConstraint(component);
		
		List<Component> children = component.getChildren();
		if(!children.isEmpty())
			for(int i=0; i<children.size(); i++){
				Component each = children.get(i);
				cleanConstraintComponent(each);
			}
	}
	
	/**
	 * Descripcion: permitira limpiar los constraint del formulario
	 * Parametros: Ninguno
	 * Retorno: Ninguno
	 * */
	protected void cleanConstraintForm(){
		if(form!=null){
			cleanConstraintComponent(form);
		}
	}
	
	/**
	 * Descripcion: inyectara los componentes de acuerdo a la anotacion BeanInjector
	 * Parametros. @param objeto: objeto que contiene las anotaciones
	 * Retorno: ninguno
	 * */
	private void buildInjection(Object objeto){
		if(objeto!=null)
			buildInjection(objeto.getClass(), objeto);
	}
	
	public void buildInjection(Class<?> clase, Object objeto){
		if(clase.equals(Object.class))
			return;
		else {
			for(Field field : clase.getDeclaredFields()){
				if(field.isAnnotationPresent(BeanInjector.class)){
					BeanInjector annotation=field.getAnnotation(BeanInjector.class);
					//Object fieldInject = annotation.clase().cast(SpringUtil.getBean(annotation.beanName()));
					Class<?> claseObjeto = (!annotation.clase().equals(Class.class)) 
							? annotation.clase() : field.getType();
					Object fieldInject = claseObjeto.cast(SpringUtil.getBean(annotation.value()));
					try {
						BeanUtils.copyProperty(objeto, field.getName(), fieldInject);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						System.out.println("IllegalAccessException");
						continue;
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						System.out.println("InvocationTargetException");
						continue;
					}
					buildInjection(fieldInject);
				}
			}
			buildInjection(clase.getSuperclass(), objeto);
		}
	}
	
	/**
	 * Descripcion: Permitira calcular el tamaño porcentual de la grid east 
	 * en base a si esta abierta o cerrada la grid west
	 * Parametros: 
	 * @param west: grid izquierda de la pantalla
	 * @param east: grid derecha de la pantalla
	 * @param baseW: tamaño base de la region
	 * Retorno: Ninguno
	 * */
	@Command
	public void openRegionWest(@BindingParam("west") LayoutRegion west, @BindingParam("east") LayoutRegion east,
			@Default("62.5%") @BindingParam("baseW") String baseW,
			@Default("97%") @BindingParam("baseWC") String baseWC){
		String width = (west.isOpen()) ? baseW : baseWC;
		east.setWidth(width);
	}
	
	/**
	 * Descripcion: Permitira cerrar el modal creado
	 * Parametros: Ninguno
	 * Retorno: Ninguno
	 * */
	@Command
	public void closeModal(){
		System.out.println("CERRAR MODAL");
		if(form instanceof Window)
			((Window) form).onClose();
	}
	
	@Command
	public void updatePaging(@BindingParam("paging") Paging paging, @BindingParam("label") Label label){
		int total = paging.getTotalSize();
		if(total!=0){
			int page = paging.getActivePage();
			int pageSize = paging.getPageSize();
			int fistElement = 0, lastElement = 0;
			fistElement = page*pageSize;
			lastElement = fistElement+pageSize;
			label.setVisible(true);
			label.setValue("[ "+String.valueOf(fistElement+1)+" - "+String.valueOf((total<lastElement) ? total : lastElement)+" / "+total+" ]");
		}
		else
			label.setVisible(false);
	}
	
	/**
	 * Descripcion: permitira mover datos seleccionados de una colleccion de origen a una destino
	 * Parametros:
	 * @param origen: datos de origen
	 * @param destino: datos de destino
	 * @param selection: datos seleccionados a mover del destino al origen
	 * @param failMessage: mensaje si la seleccion esta vacia
	 * Retorno: Ninguno
	 * */
	protected <T> void moveSelection(final Collection<T> origen, final Collection<T> destino, final Collection<T> selection, 
			final String failMessage) {
		moveSelection(origen, destino, selection, null, true,  failMessage);
    }
	
	/**
	 * Descripcion: permitira mover datos seleccionados de una colleccion de origen a una destino por medio de un comparable
	 * Parametros:
	 * @param origen: datos de origen
	 * @param destino: datos de destino
	 * @param selection: datos seleccionados a mover del destino al origen
	 * @param comparable: medio de comparacion para la busqueda binaria
	 * @param remover: indicara si es requerido remover los datos del origen si es factible
	 * @param failMessage: mensaje si la seleccion esta vacia
	 * Retorno: Ninguno
	 * */
	/**
	 * Descripcion: permitira mover datos seleccionados de una colleccion de origen a una destino por medio de un comparable
	 * Parametros:
	 * @param origen: datos de origen
	 * @param destino: datos de destino
	 * @param selection: datos seleccionados a mover del destino al origen
	 * @param comparable: medio de comparacion para la busqueda binaria
	 * @param remover: indicara si es requerido remover los datos del origen si es factible
	 * @param failMessage: mensaje si la seleccion esta vacia
	 * Retorno: Ninguno
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected <T> void moveSelection(final Collection<T> origen, final Collection<T> destino, final Collection<T> selection, 
			final Comparator<T> comparable, final boolean remover, final String failMessage){
		if(selection!=null && destino!=null && origen!=null)
			if (selection.isEmpty())
				Clients.showNotification((failMessage==null)? "No se Ha Seleccionado Nada": failMessage,
						"info", null, null, 2000, true);
			else {
				//Eliminacion de registros repetidos
				LinkedHashSet selecctionSet = new LinkedHashSet(selection);
				selection.clear();
				selection.addAll(selecctionSet);
				
				if(destino.isEmpty())
					destino.addAll(selection);
				else if(comparable!=null)
				{
					int index;
					for(T model : selection){
						index = Collections.binarySearch((List<T>) destino, model, comparable);
						if(index<0)
							destino.add(model);
					}
				}
				
				selection.clear();
				if(remover)
					try {
						origen.removeAll(selection);
					}
					catch(UnsupportedOperationException exception){
						System.out.println("No se puede remover del origen");
					}
			}
		else
			System.out.println("Alguna coleccion esta vacia.");
	}
	
	/**
	 * Descripcion: Premitira buscar un componente en el html por su id respectivo.
	 * @param page: referencia a la pagina (cualquier componente @Wire pude usarse para tomar la referencia)
	 * @param id: id del componente del html
	 * Retorno: T - componente generico al que se quiere obtener.
	 * */
	@SuppressWarnings("unchecked")
	protected <T> T findComponent(Page page, String id){
		return (T) Selectors.iterable(page, id).iterator().next();
	}
	
	/**
	 * Descripcion: Permitira incluir un componente en el componente del html con el id respectivo pasado por parametro
	 * @param page: referencia a la pagina (cualquier componente @Wire pude usarse para tomar la referencia)
	 * @param id: id del componente del html
	 * @param route: ruta del archivo .zul donde se encuentra el componente a incluir
	 * Retorno: Include objeto incluido a partir del archivo .zul
	 * */
	protected Include insertComponent(Page page, String id, String route){
		Include include = (Include) findComponent(page, id);
		include.setSrc(route);
		return include;
	}
	
	/**
	 * Descripcion: Creara un componente a partir del archivo en la ruta especificada por parametro
	 * @param ruta: ruta del componente especificado
	 * @param parametros: parametros que se le pasaran al viewmodel del componente
	 * Retorno: T-componente que se requiere crear
	 * */
	@SuppressWarnings("unchecked")
	protected <T> T crearComponente(String ruta, Map<String, Object> parametros){
		return (T) Executions.createComponents(ruta, null, parametros);
	}
	
	/**
	 * Descripcion: Creara un emergente (Window) a partir del archivo en la ruta especificada por parametro
	 * @param ruta: ruta del emergente especificado
	 * @param parametros: parametros que se le pasaran al viewmodel del emergente
	 * Retorno: Emergente que se requiere crear
	 * */
	protected Window crearModal(String ruta, Map<String, Object> parametros){
		Window window = (Window) crearComponente(ruta, parametros);
        window.doModal(); 
        return window;
	}
	
	/**
	 * Descripcion: Mostrara un mensaje en pantalla sobre algun tipo de mensaje que se requiera
	 * Parametros: 
	 * @param titulo: titulo del mensaje
	 * @param mensaje: texto a mostrar
	 * @param icon: tipo del icono a mostrar en el mensaje (Messagebox. )
	 * @param botones: array con los botones del mensaje
	 * @param clickEvent: evento al presionar algun evento de los botones
	 * Retorno: Ninguno
	 * */
	/**
	 * Descripcion: Mostrara un mensaje en pantalla sobre algun tipo de mensaje que se requiera
	 * Parametros: 
	 * @param titulo: titulo del mensaje
	 * @param mensaje: texto a mostrar
	 * @param icon: tipo del icono a mostrar en el mensaje (Messagebox. )
	 * @param botones: array con los botones del mensaje
	 * @param clickEvent: evento al presionar algun evento de los botones
	 * Retorno: Ninguno
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void mostrarMensaje(String titulo, String mensaje, String icon, Button[] botones, 
			EventListener clickEvent, Map<String, String> params){
		
		botones = (botones==null) ? new Button[]{org.zkoss.zul.Messagebox.Button.OK} : botones;
		icon = (icon==null) ? Messagebox.INFORMATION : icon;
		params = (params==null) ? new HashMap<String, String>() : params;
		params.put("sclass", "btn btn-sm");
		Messagebox.show(mensaje, titulo, botones, null, icon, null, clickEvent, params);
	}
	
	/**
	 * Descripcion: Mostrara una Notificacion de acuerdo al tipo de mensaje pasado por parametro
	 * Parametros:
	 * @param mensaje: mensaje de la notificacion
	 * @param tipo: tipo de mensaje a mostrar -> "info", "warning", "error". Default "info"
	 * @param duration: duracion de la notificacion
	 * @param closable: si puede o no cerrarse la notificacion
	 * @param parent: objeto de referencia donde se colocara la notificacion
	 * Retorno: Ninguno
	 * */
	protected void mostrarNotification(String Mensaje, String tipo, Integer duration, Boolean closable, Component parent){
		
		tipo= (tipo==null) ? "info" : tipo;
		duration= (duration==null) ? 2000 : duration;
		closable=(closable==null) ? false : closable;
		Clients.showNotification(Mensaje, tipo, parent, null, duration, closable);
	}
	
	/**
	 * Descripcion: redireccionara a la url pasada como parametro
	 * @param url: url a la que se quiere redirigir
	 * Retorno: Ninguno
	 * */
	protected void redireccionar(String url){
		Executions.sendRedirect(url);
	}
	
	/**
	 * Descripcion. ejecutara el command global (@GlobalCommand) con el nombre especificado en el parametro
	 * @param name: nombre del comando global
	 * @param parametros: parametros que requiere el commando, puede ser nulo
	 * Retorno: Ninguno
	 * */
	protected void ejecutarGlobalCommand(String name, Map<String, Object> parametros){
		BindUtils.postGlobalCommand(null, null, name, parametros);
	}
	
	/**
	 * Descripcion. construira el menu de acuerdo al modelo especifico del Navbar
	 * @param modelo: representa los hijos a agregar en el componente parent
	 * @param parent: componente padre de los hijos
	 * Retorno: Ninguno
	 * */
	@SuppressWarnings("unchecked")
	protected void constructMenu(List<TreeNode<ModelNavbar>> modelo, Component parent){
		if(modelo!=null)
			for(TreeNode<ModelNavbar> nodo : modelo){
				Component child;
				ModelNavbar item = nodo.getData();
				if(nodo.getChildren().size()>0){
					child = new Nav();
					((Nav) child).setLabel(item.getLabel());
					((Nav) child).setIconSclass(item.getIcon());
					((Nav) child).setSclass("sidebar-fn");
					constructMenu(nodo.getChildren(), child);
				}
				else{
					String icon = (item.getIcon()!=null) ? item.getIcon() : "z-icon-angle-double-right";
					child = new Navitem();
					((Navitem) child).setLabel(item.getLabel());
					((Navitem) child).setIconSclass(icon);
					((Navitem) child).setSclass("sidebar-fn");
					((Navitem) child).setAttribute("locationUri", item.getUriLocation());
					
			        //new and register listener for events
					child.addEventListener(Events.ON_CLICK, (EventListener<? extends Event>) this);
				}
				
				parent.appendChild(child);
			}
	}
	
	/**
	 * Descripcion: agregara el evento del sort a los listhead del listbox pasado como parametro
	 * @param grid: grid que contiene Listheader para agregar el evento respectivo
	 * Retorno: Ninguno
	 * */
	protected void agregarGridSort(Listbox grid){
		Listhead head = grid.getListhead();
		List<Listheader> headers = head.getChildren();
		if(headers!=null)
			for(Listheader header : headers){
				if (header.getSortAscending() != null || header.getSortDescending() != null)
					header.addEventListener(Events.ON_SORT, (EventListener<? extends Event>) this);
			}
	}
	
	public void notifyChange(String field){
		BindUtils.postNotifyChange(null,null, this, field);
	}
	
	/**METODOS PROPIOS DE LA CLASE**/
	public static String decodificarImagen(byte[] imagen){
		if(imagen!=null){
			return "data:image/jpeg;base64,"+DatatypeConverter.printBase64Binary(imagen);
		}
		return null;
	}
	
	public static String decodificarDocumento(byte[] documento){
		if(documento!=null){
			return "data:application/pdf;base64,"+DatatypeConverter.printBase64Binary(documento);
		}
		return null;
	}
	
	/***
	 * Metodo que selecciona la ruta para la carpeta
	 */
	public String seleccionarRuta() {

		JFileChooser chooser = new JFileChooser();
		// Titulo que llevara la ventana
		chooser.setDialogTitle("Seleccione...");

		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		chooser.showOpenDialog(null);

		// Retornando el directorio destino directorio
		if (chooser.getSelectedFile() != null) 
			return chooser.getSelectedFile().getPath();
		else
			return "";
	}
	
	/***
	 * Metodo que permite seleccionar el archivo a restaurar
	 */
	public Map<String, String> seleccionarArchivo() {

		Map<String, String> parametros=null;
		JFileChooser chooser = new JFileChooser();
		// Titulo que llevara la ventana
		chooser.setDialogTitle("Seleccione...");

		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.showOpenDialog(null);

		// Si seleccionamos algunn archivo retornaremos su ubicacion
		if (chooser.getSelectedFile() != null) {
			parametros = new HashMap<String, String>();
			parametros.put("extension", chooser.getSelectedFile().getName());
			parametros.put("ruta", chooser.getSelectedFile().getPath());
		}
		
		return parametros;
	}
	
	public String getFormatoFecha(Timestamp fecha){
		return formatDate.format(new Date(fecha.getTime()));
	}
	
	/**REPORTES*/
	private JasperPrint crearReporte(String ruta, Map parametros, List lista){
		try {
			System.out.println("Ruta: "+ruta);
			
			JasperReport jasperRepor = JasperCompileManager.compileReport(ruta);

			/*JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(reporteSrc);*/

			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperRepor, parametros, new JRBeanCollectionDataSource(
							lista));
			
			return jasperPrint;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		
		return null;
	}
	
	protected void generarReporte(String ruta, Map parametros, List lista){
		try {
			JasperPrint jasperPrint = crearReporte(ruta, parametros, lista);
			JasperViewer.viewReport(jasperPrint, false);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}
	
	protected void exportarReporteExcel(String ruta, Map parametros, List lista){
		try {
			
			String rutaExport = seleccionarRuta();
			if(!rutaExport.equalsIgnoreCase("")){
				JasperPrint jasperPrint = crearReporte(ruta, parametros, lista);

				OutputStream ouputStream= new FileOutputStream(new File(ruta));
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				JRXlsExporter exporterXLS = new JRXlsExporter();
				exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT,
						jasperPrint);
				exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM,
						byteArrayOutputStream);
				exporterXLS.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER,
						Boolean.FALSE);
				exporterXLS.setParameter(
						JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				exporterXLS.setParameter(
						JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
						Boolean.TRUE);
				exporterXLS.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN,
						Boolean.TRUE);

				exporterXLS.setParameter(
						JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
						Boolean.TRUE);
				exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
						Boolean.FALSE);
				exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,
						Boolean.TRUE);

				exporterXLS.setParameter(JRXlsExporterParameter.IS_IMAGE_BORDER_FIX_ENABLED,
						Boolean.TRUE);
				exporterXLS.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED,
						Boolean.TRUE);

				exporterXLS.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS,
						Boolean.FALSE);


				exporterXLS.exportReport();
				ouputStream.write(byteArrayOutputStream.toByteArray());
				ouputStream.flush();
				ouputStream.close();

			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}
	
	protected void exportarReporteTxt(String ruta, Map parametros, List lista){
		try {

			String rutaExport = seleccionarRuta();
			if(!rutaExport.equalsIgnoreCase("")){
				JasperPrint jasperPrint = crearReporte(ruta, parametros, lista);

				File file = new File(rutaExport);
				OutputStream ouputStream= new FileOutputStream(file);
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				JRTextExporter exporter = new JRTextExporter();

				exporter.setParameter(JRTextExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRTextExporterParameter.OUTPUT_FILE, file);
				exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, new Float(300));
				exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, new Float(100));
				exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, new Float(8));
				exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Float((25)));
				exporter.exportReport();

				ouputStream.write(byteArrayOutputStream.toByteArray());
				ouputStream.flush();
				ouputStream.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}
	
	/**RECURSOS*/
	/**
	 * Iconos: http://fortawesome.github.io/Font-Awesome/icons/ -> Section:Web Application Icons -> fa = z-icon 
	 * Botones:
	 * Azul: btn btn-sm btn-info
	 * Verde: btn btn-sm btn-success
	 * Anaranjado: btn btn-sm btn-warning
	 * Rojo: btn btn-sm btn-danger
	 * Negro: btn btn-sm btn-inverse
	 * Rosado: btn btn-sm btn-pink
	 * Amarillo: btn btn-sm btn-yellow
	 * */
	
	
	public CustomConstraint getNotEmptyValidator() {
		return new GeneralConstraint(EConstraint.NO_EMPTY);
	}
	
	
}
