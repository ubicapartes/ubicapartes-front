package com.okiimport.app.mvvm.constraint;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;

public class EmailConstraint extends CustomConstraint {
	
	private CustomConstraint emailGeneralValidator;
	
	private EmailConstraintComunicator comunicator;

	public EmailConstraint(CustomConstraint emailGeneralValidator, EmailConstraintComunicator comunicator) {
		super(EConstraint.CUSTOM);
		this.emailGeneralValidator = emailGeneralValidator;
		this.comunicator = comunicator;
	}

	@Override
	protected void validateCustom(Component comp, Object value) throws WrongValueException {
		emailGeneralValidator.validate(comp, value);
		String email = String.valueOf(value);
		if(comunicator.searchClient(email)){
			String mensaje = "El correo ingresado ya esta registrado!";
			throw new WrongValueException(comp, mensaje);
		}
	}
	
	public static interface EmailConstraintComunicator {
		Boolean searchClient(String email);
	}

}
