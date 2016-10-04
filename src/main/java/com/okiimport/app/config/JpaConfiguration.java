package com.okiimport.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.okiimport.app.service.maestros.SMaestros;
import com.okiimport.app.service.maestros.impl.SMaestrosImpl;
import com.okiimport.app.service.transaccion.STransaccion;
import com.okiimport.app.service.transaccion.impl.STransaccionImpl;
import com.okiimport.persistencia.AbstractJpaConfiguration;

@Configuration
public class JpaConfiguration extends AbstractJpaConfiguration {

	public JpaConfiguration() {
		super("requerimientos.urbicars@gmail.com", "R123456789");
	}

//	@Bean(name="sMaestros")
//	public SMaestros sMaestros(){
//		return new SMaestrosImpl();
//	}
//	
//	@Bean(name="sTransaccion")
//	public STransaccion sTransaccion(){
//		return new STransaccionImpl();
//	}
}
