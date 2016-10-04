package com.okiimport.app.mvvm.constraint;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;

public class GeneralConstraint extends CustomConstraint {

	public GeneralConstraint(EConstraint... eConstraints) {
		super(eConstraints);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void validateCustom(Component comp, Object value) throws WrongValueException {
		// TODO Auto-generated method stub
		//NO REQUIERE IMPLEMENTACION
	}

}
