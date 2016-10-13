package com.okiimport.app.mvvm.constraint;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;

import com.okiimport.app.model.Cliente;

public class ClienteCedulaRifConstraint extends CustomConstraint {
	
	private ClienteCedulaRifComunicator comunicator;

	public ClienteCedulaRifConstraint(ClienteCedulaRifComunicator comunicator) {
		super(EConstraint.NO_EMPTY, EConstraint.NO_NEGATIVE, 
				EConstraint.NO_ZERO, EConstraint.CUSTOM);
		this.comunicator = comunicator;
	}

	@Override
	protected void validateCustom(Component comp, Object value) throws WrongValueException {
		String tipo = this.comunicator.getTypeClient();
		String cedula = String.valueOf(value);
		String cedulaBuscar = tipo + cedula;
		if(this.comunicator.searchClient(cedulaBuscar) != null){
			String mensaje = "La cedula o rif ingresado ya esta registrado!";
			throw new WrongValueException(comp, mensaje);
		}
		
	}
	
	public static interface ClienteCedulaRifComunicator {
		public String getTypeClient();
		public Cliente searchClient(String cedulaORif);
	}

}
