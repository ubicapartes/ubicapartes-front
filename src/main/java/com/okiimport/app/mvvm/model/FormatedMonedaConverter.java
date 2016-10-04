package com.okiimport.app.mvvm.model;

import java.io.Serializable;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.sys.Binding;
import org.zkoss.zk.ui.Component;

import com.okiimport.app.resource.model.ICoverterMoneda;

public class FormatedMonedaConverter extends FormatedNumberConverter implements Serializable {
	private static final long serialVersionUID = 3186924879404402405L;

	/**
	 * Convert Number to String.
	 * @param val number to be converted
	 * @param comp associated component
	 * @param ctx bind context for associate {@link Binding} and extra parameter (e.g. format)
	 * @return the converted String
	 */
	@Override
	public String coerceToUi(Object val, Component comp, BindContext ctx) {
		final ICoverterMoneda monedaBase = (ICoverterMoneda) ctx.getConverterArg("monedaBase");
		final ICoverterMoneda converterMoneda = (ICoverterMoneda) ctx.getConverterArg("converterMoneda");
		if(converterMoneda==null) throw new NullPointerException("converter attribute not found");
		if(monedaBase==null) throw new NullPointerException("moneda attribute not found");
		final Object valorFlotante = (Object) super.coerceToUi(converterMoneda.convert((Number) val), comp, ctx);
		return monedaBase.withSimbolo((String) valorFlotante);
	}

	/**
	 * Convert String to Number.
	 * @param val number in string form
	 * @param comp associated component
	 * @param ctx bind context for associate {@link Binding} and extra parameter (e.g. format)
	 * @return the converted Number
	 */
	@Override
	public Object coerceToBean(String val, Component comp, BindContext ctx) {
		final ICoverterMoneda monedaBase = (ICoverterMoneda) ctx.getConverterArg("monedaBase");
		final ICoverterMoneda converterMoneda = (ICoverterMoneda) ctx.getConverterArg("converterMoneda");
		if(converterMoneda==null) throw new NullPointerException("converter attribute not found");
		if(monedaBase==null) throw new NullPointerException("moneda attribute not found");
		final Number montoPorUnidadBase = converterMoneda.getMontoPorUnidadBase();
		String valorFlotante = val.split(monedaBase.getSimboloMoneda())[0];
		if(valorFlotante!=null){
			valorFlotante = String.valueOf(Float.valueOf(valorFlotante.trim())*montoPorUnidadBase.floatValue());
			return super.coerceToBean(valorFlotante, comp, ctx);
		}
		return null;
	}
}
