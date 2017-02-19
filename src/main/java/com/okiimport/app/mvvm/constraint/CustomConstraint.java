package com.okiimport.app.mvvm.constraint;

import org.apache.commons.lang.ArrayUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Label;
import org.zkoss.zul.SimpleConstraint;

public abstract class CustomConstraint implements Constraint, org.zkoss.zul.CustomConstraint {
	
	/**ENUM OF CONSTRAINT POSIBLE*/
	public enum EConstraint {
		NO_POSITIVE(SimpleConstraint.NO_POSITIVE, "Solo n\u00fameros negativos"),
		NO_NEGATIVE(SimpleConstraint.NO_NEGATIVE, "Solo n\u00fameros positivos"),
		NO_ZERO(SimpleConstraint.NO_ZERO, "Valor 0 no permitido"),
		NO_EMPTY(SimpleConstraint.NO_EMPTY, "Valor vacio no permitido"),
		STRICT(SimpleConstraint.STRICT, null),
		SERVER(SimpleConstraint.SERVER, null),
		NO_FUTURE(SimpleConstraint.NO_FUTURE, "Valores futuros no permitido"),
		NO_PAST(SimpleConstraint.NO_PAST, "Valores pasados no permitido"),
		NO_TODAY(SimpleConstraint.NO_TODAY, "El dia de hoy no permitido"),
		BEFORE_START(SimpleConstraint.BEFORE_START, null),
		BEFORE_END(SimpleConstraint.BEFORE_END, null),
		END_BEFORE(SimpleConstraint.END_BEFORE, null),
		END_AFTER(SimpleConstraint.END_AFTER, null),
		AFTER_END(SimpleConstraint.AFTER_END, null),
		AFTER_START(SimpleConstraint.AFTER_START, null),
		START_AFTER(SimpleConstraint.START_AFTER, null),
		START_BEFORE(SimpleConstraint.START_BEFORE, null),
		OVERLAP(SimpleConstraint.OVERLAP, null),
		OVERLAP_END(SimpleConstraint.OVERLAP_END, null),
		OVERLAP_BEFORE(SimpleConstraint.OVERLAP_BEFORE, null),
		OVERLAP_AFTER(SimpleConstraint.OVERLAP_AFTER, null),
		AT_POINTER(SimpleConstraint.AT_POINTER, null),
		AFTER_POINTER(SimpleConstraint.AFTER_POINTER, null),
		CUSTOM(-1, null),
		EMAIL_INVALID(-1,"Correo no valido");
		
		//ATRIBUTOS
		private int value;
		private String mensaje;
		
		/**Constructor*/
		private EConstraint(int value, String mensaje) {
			// TODO Auto-generated constructor stub
			this.value = value;
			this.mensaje = mensaje;
		}
		
		/**GETTERS Y SETTERS*/
		public int getValue(){
			return this.value;
		}
		
		public String getMensaje(){
			return this.mensaje;
		}
		
		/**METODOS ESTATICOS DE LA CLASE*/
		public static final boolean checkCustomValue(EConstraint... eConstraints){
			for(EConstraint constraint : eConstraints)
				if(constraint.equals(EConstraint.CUSTOM))
					return true;
			
			return false;
		}
	}
	
	//GUI
	private Component parent;
	private Label componentError;
	
	//ATRIBUTOS
	private EConstraint[] eConstraints;
	private EConstraint eConstraint;
	
	/**Constructor*/
	public CustomConstraint(EConstraint... eConstraints){
		this.eConstraints = eConstraints;
		
		if(!EConstraint.checkCustomValue(eConstraints))
			this.eConstraints=concatArrayConstraint(eConstraints, new EConstraint[]{EConstraint.CUSTOM});
	}

	/**METODOS OVERRIDE*/
	@Override
	public void validate(Component comp, Object value) throws WrongValueException {
		// TODO Auto-generated method stub
		for(EConstraint constraint : eConstraints){
			try {
				eConstraint = constraint;

				if(parent==null && comp!=null)
					parent = comp.getParent();

				if(constraint.equals(EConstraint.CUSTOM))
					validateCustom(comp, value);
				else {
					hideComponentError();
					SimpleConstraint simpleCostraint = new SimpleConstraint(constraint.getValue());
					simpleCostraint.validate(comp, value);
				}
			} catch (WrongValueException e) {
				showCustomError(comp, e);
				throw e;
			}
		}
	}
	
	@Override
	public void showCustomError(Component comp, WrongValueException ex) {
		// TODO Auto-generated method stub
		if(componentError==null){
			componentError = new Label();
			componentError.setWidth("100%");
			componentError.setStyle("color: red");
			
		}
		
		if(eConstraint.equals(EConstraint.CUSTOM) && ex!=null)
			componentError.setValue(ex.getMessage());
		else 
			componentError.setValue(eConstraint.getMensaje());
		
		parent.appendChild(componentError);
		
		parent.applyProperties();
	}

	/**METODOS ABSTRACTOS DE LA CLASE*/
	protected abstract void validateCustom(Component comp, Object value) throws WrongValueException;
	
	/**METODOS PROPIOS DE LA CLASE*/
	protected EConstraint[] concatArrayConstraint(EConstraint[] array, EConstraint... eConstraints){
		return (EConstraint[]) ArrayUtils.addAll(array, eConstraints);
	}
	
	public void hideComponentError(){
		if(parent!=null && componentError!=null)
			parent.removeChild(componentError);
	}
}
