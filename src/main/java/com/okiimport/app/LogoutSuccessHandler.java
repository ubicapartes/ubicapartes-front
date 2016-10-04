package com.okiimport.app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.okiimport.app.model.Usuario;
import com.okiimport.app.service.seguridad.SHistorial;
import com.okiimport.app.service.configuracion.SControlUsuario;


@Component
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
	
	@Autowired
	private SHistorial sHistorial;
	
	@Autowired
	private SControlUsuario sControlUsuario;
	
	/**METODOS OVERRIDE*/
	@Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
		User user = (User) authentication.getPrincipal();
		Usuario usuario = sControlUsuario.consultarUsuario(user.getUsername(), user.getPassword(), null);
		sHistorial.cerarHistorialSession(usuario);
		setDefaultTargetUrl("/");
		super.onLogoutSuccess(request, response, authentication);
	}
}
