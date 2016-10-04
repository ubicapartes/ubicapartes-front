package com.okiimport.app.mvvm.constraint;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.SimpleConstraint;

public class RegExpressionConstraint extends CustomConstraint {
	
	public static class RegExpression {
		private String expression;
		private String mensaje;
		
		/**Constructor*/
		public RegExpression(String expression, String mensaje) {
			super();
			this.expression = expression;
			this.mensaje = mensaje;
		}
		
		/**GETTERS*/
		public String getExpression() {
			return expression;
		}
		public String getMensaje() {
			return mensaje;
		}
	}
	
	private RegExpression[] regExpressions;

	public RegExpressionConstraint(RegExpression[] regExpressions, EConstraint... eConstraints) {
		// TODO Auto-generated constructor stub
		super(eConstraints);
		this.regExpressions = regExpressions;
	}

	@Override
	protected void validateCustom(Component comp, Object value) throws WrongValueException {
		// TODO Auto-generated method stub
		for(RegExpression expression : regExpressions){
			try {
				SimpleConstraint constraint = SimpleConstraint.getInstance(expression.getExpression());
				constraint.validate(comp, value);
			}
			catch (Exception e){
				throw new WrongValueException(comp, expression.getMensaje());
			}
		}
	}

}
