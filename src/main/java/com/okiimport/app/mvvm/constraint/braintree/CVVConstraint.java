package com.okiimport.app.mvvm.constraint.braintree;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;

import com.okiimport.app.mvvm.constraint.CustomConstraint;

public class CVVConstraint extends CustomConstraint {

	public CVVConstraint() {
		super(EConstraint.NO_EMPTY, 
				EConstraint.NO_NEGATIVE, 
				EConstraint.NO_ZERO,
				EConstraint.CUSTOM);
	}

	@Override
	protected void validateCustom(Component comp, Object value) throws WrongValueException {
		Integer lengthCVV = String.valueOf(value).length();
		
		if(lengthCVV < 3 || lengthCVV > 4){
			String mensaje = "CVV Invalido";
			throw new WrongValueException(comp, mensaje);
		}

	}

}
