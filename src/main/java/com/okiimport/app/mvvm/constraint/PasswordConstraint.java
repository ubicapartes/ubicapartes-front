package com.okiimport.app.mvvm.constraint;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;

import com.okiimport.app.model.Persona;

public class PasswordConstraint extends CustomConstraint {
	
	private CustomConstraint passwordGeneralConstraint;
	
	private PasswordConstraintComunicator comunicator;
	
	private Persona persona;

	public PasswordConstraint(Persona persona, CustomConstraint passwordGeneralConstraint, PasswordConstraintComunicator comunicator) {
		super(EConstraint.CUSTOM);
		this.passwordGeneralConstraint = passwordGeneralConstraint;
		this.comunicator = comunicator;
		this.persona = persona;
	}

	@Override
	protected void validateCustom(Component comp, Object value) throws WrongValueException {
		passwordGeneralConstraint.validate(comp, value);
		
		String password = String.valueOf(value);
		
		if(persona.getCedula()!=null && persona.getCedula().equalsIgnoreCase(password)){
			String mensaje = "La contrase\u00f1a ingresada no puede ser igual a la cedula o rif!";
			throw new WrongValueException(comp, mensaje);
		}
		else if(persona.getCorreo()!=null && persona.getCorreo().equalsIgnoreCase(password)){
			String mensaje = "La contrase\u00f1a ingresada no puede ser igual a al correo!";
			throw new WrongValueException(comp, mensaje);
		}
		else if(!contain(this.comunicator.getMayusculas(), password)){
			String mensaje = "La contrase\u00f1a ingresada debe contener al menos una mayuscula!";
			throw new WrongValueException(comp, mensaje);
		} else if(!contain(this.comunicator.getMinusculas(), password)){
			String mensaje = "La contrase\u00f1a debe contener al menos una minuscula!";
			throw new WrongValueException(comp, mensaje);
		} else if(!contain(this.comunicator.getNumeros(), password)){
			String mensaje = "La contrase\u00f1a debe contener al menos una n\u00famero!";
			throw new WrongValueException(comp, mensaje);
		}
		
	}
	
	private boolean contain(String characteres, String email){
		for(int i=0; i<email.length(); i++){
			if (characteres.indexOf(email.charAt(i),0)!=-1){
				return true;
			}
		}
		return false;
	}
	
	public static interface PasswordConstraintComunicator {
		String getMayusculas();
		String getMinusculas();
		String getNumeros();
	}

}
