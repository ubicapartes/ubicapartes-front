package com.okiimport.app;


import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

import java.util.Locale;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.okiimport.app.service.seguridad.SAcceso;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private SAcceso sAcceso;
	
	/**
	 * Simply selects the home view to render by returning its name. web/login
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "../../index.zul";
	}
	
	@RequestMapping(value= "/inicioSession", method = RequestMethod.GET)
	public String iniciarSession(){
//		1. Simple
//		mailService.send("eugeniohernandez17@gmail.com", "SISTEMA", "PRUEBA DE MENSAJE");
//		2.Completo
//		//El Objecto que se envia debe declararse final, esto quiere decir que no puede instanciarse sino solo una vez
//		final Cliente cliente = new Cliente("20186243"); 
//		Map<String, Object> model = new HashMap<String, Object>();
//		model.put("usuario", "Eugenio Caicedo");
//		model.put("cliente", cliente);
//		String archivo = obtenerDirectorioRecursos("prueba.html");
//		mailService.send("eugeniohernandez17@gmail.com", "SISTEMA", "prueba2.html", model, new File(archivo));
		return "security/index.zul";
	}
	
	
	@RequestMapping(value= "/contacto", method = RequestMethod.GET)
	public String contactar(){
		return "security/contactanos.zul";
	}
	
	@RequestMapping(value= "/admin/home", method = RequestMethod.GET)
	public String iniciarAdministrador(){
		return "sistema/index.zul";
	}
	
	@RequestMapping(value= "/sessionExpirada", method = RequestMethod.GET)
	public String expirarSession(){
		return "security/expirar.zul";
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	/**
	 * Descripcion: Verifica if el usuario al iniciar session uso remember me
	 * Parametros: Ninguno
	 * Retorno: Boolean - si el usuario uso o no el remember me 
	 */
	private Boolean isRememberMeAuthenticated() {
		Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user instanceof User)
			if(sAcceso.validarAutenticacion((User) user)){
				SecurityContextHolder.getContext().setAuthentication(sAcceso.consultarAutenticacion((User) user));
				return true;
			}
 
		return false;
	}
	
	/** Metodo que permite obtener el directorio actual del proyecto */
	public static String obtenerDirectorio() {
		URL rutaURL = HomeController.class.getProtectionDomain().getCodeSource()
				.getLocation();
		String ruta = rutaURL.getPath();
		//return "/" + ruta.substring(1, ruta.indexOf("SITEG"));
		return ruta.substring(0, ruta.indexOf("WEB-INF"));
	}
	
	public static String obtenerDirectorioRecursos(){
		return obtenerDirectorio()+"resources/";
	}
	
	public static String obtenerDirectorioRecursos(String path){
		return obtenerDirectorioRecursos()+path;
	}
	
	@RequestMapping(value= "/proveedorF", method = RequestMethod.GET)
	public String obtenerFproveedor(){
		return "portal/formularioProveedor.zul";
	}
	
	@RequestMapping(value= "/registrate", method = RequestMethod.GET)
	public String obtenerFregistro(){
		return "portal/formularioRegistroUsuario.zul";
	}
	
	
}
