package com.okiimport.app.mvvm.constraint;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;

public class AnnoConstraint extends CustomConstraint {

	private Integer minYear;
	private Integer maxYear;
	
	public AnnoConstraint(Integer minYear, Integer maxYear) {
		// TODO Auto-generated constructor stub
		super(EConstraint.NO_EMPTY, 
				EConstraint.NO_NEGATIVE, 
				EConstraint.NO_ZERO,
				EConstraint.CUSTOM);
		this.minYear = minYear;
		this.maxYear = maxYear;
	}

	@Override
	protected void validateCustom(Component comp, Object value) throws WrongValueException {
		// TODO Auto-generated method stub
		String mensaje = null;
		Integer anno = (Integer) value;
		
		if(minYear!=null && maxYear!=null){
			if(minYear > anno || anno > maxYear){
				mensaje = "El A\u00f1o ingresado es Invalido !";
				throw new WrongValueException(comp, mensaje);
			}
		}
		else if(minYear!=null && minYear > anno){
			mensaje = "El A\u00f1o ingresado "+anno+" debe ser mayor que "+minYear+"!";
			throw new WrongValueException(comp, mensaje);
		}
		else if(maxYear!=null && anno > maxYear){
			mensaje = "El A\u00f1o ingresado "+anno+" debe ser menor que "+maxYear+"!";
			throw new WrongValueException(comp, mensaje);
		}

	}

}
