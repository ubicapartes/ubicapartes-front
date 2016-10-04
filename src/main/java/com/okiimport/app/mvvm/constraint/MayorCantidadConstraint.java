package com.okiimport.app.mvvm.constraint;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;

public class MayorCantidadConstraint extends CustomConstraint {
	
	//Atributos
	private Long cantidadRequerida;

	public MayorCantidadConstraint(Long cantidadRequerida) {
		// TODO Auto-generated constructor stub
		super(CustomConstraint.EConstraint.NO_EMPTY,
				CustomConstraint.EConstraint.NO_NEGATIVE,
				CustomConstraint.EConstraint.NO_ZERO,
				CustomConstraint.EConstraint.CUSTOM);
		this.cantidadRequerida = cantidadRequerida;
	}

	@Override
	protected void validateCustom(Component comp, Object value) throws WrongValueException {
		// TODO Auto-generated method stub
		Integer cantidadOfrecida = (Integer) value;
		if(cantidadOfrecida!=null && cantidadRequerida!=null && cantidadOfrecida > cantidadRequerida){
			String mensaje = "No puede ser mayor que "+cantidadRequerida+" !";
			throw new WrongValueException(comp, mensaje);
		}
	}

}
