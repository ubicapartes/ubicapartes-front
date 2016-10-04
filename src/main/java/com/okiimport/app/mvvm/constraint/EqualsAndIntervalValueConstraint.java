package com.okiimport.app.mvvm.constraint;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;

public class EqualsAndIntervalValueConstraint extends CustomConstraint {
	
	private String valorEqual;
	private Integer caracteresMin;
	private Integer caracteresMax;

	public EqualsAndIntervalValueConstraint(String valorEqual, Integer caracteresMin, Integer caracteresMax) {
		super(CustomConstraint.EConstraint.NO_EMPTY,
				CustomConstraint.EConstraint.CUSTOM);
		this.valorEqual=valorEqual;
		this.caracteresMin=caracteresMin;
		this.caracteresMax=caracteresMax;
	}

	@Override
	protected void validateCustom(Component comp, Object value) throws WrongValueException {
		String valor = (String) value;
		if(valor != null && valorEqual!=null && !valor.equalsIgnoreCase(valorEqual)){
			String mensaje = "No coinciden !";
			throw new WrongValueException(comp, mensaje);
		}
		else if(valor.length() < caracteresMin){
			String mensaje = "El minimo de carcateres permitidos es "+this.caracteresMin+"!";
			throw new WrongValueException(comp, mensaje);
		}
		else if(valor.length() > caracteresMax){
			String mensaje = "El maximo de carcateres permitidos es "+this.caracteresMax+"!";
			throw new WrongValueException(comp, mensaje);
		}
	}

}
