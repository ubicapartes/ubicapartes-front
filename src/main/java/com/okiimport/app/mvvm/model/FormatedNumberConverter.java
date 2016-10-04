package com.okiimport.app.mvvm.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.bind.sys.Binding;
import org.zkoss.util.Locales;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;

public class FormatedNumberConverter implements Converter<String, Object, Component>, Serializable {
	private static final long serialVersionUID = -1490575563956819115L;

	/**
	 * Convert Number to String.
	 * @param val number to be converted
	 * @param comp associated component
	 * @param ctx bind context for associate {@link Binding} and extra parameter (e.g. format)
	 * @return the converted String
	 */
	@Override
	public String coerceToUi(Object val, Component comp, BindContext ctx) {
		//user sets format in annotation of binding or args when calling binder.addPropertyBinding()  
		final String formatPtn = (String) ctx.getConverterArg("format");
		String locale = (String) ctx.getConverterArg("locale");
		if(formatPtn==null) throw new NullPointerException("format attribute not found");
		return val == null ? null : 
			getLocalizedDecimalFormat(formatPtn, locale).format((Number) val);
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
		final String format = (String) ctx.getConverterArg("format");
		String locale = (String) ctx.getConverterArg("locale");
		if(format==null) throw new NullPointerException("format attribute not found");
		try {
			return val == null ? null : 
				getLocalizedDecimalFormat(format, locale).parse(val);
		} catch (ParseException e) {
			throw UiException.Aide.wrap(e);
		}
	}
	
	private static DecimalFormat getLocalizedDecimalFormat(String pattern, String locale){
		if(locale==null || locale.trim().equals("")) locale = Locale.getDefault().getLanguage();
		final DecimalFormat df = 
			(DecimalFormat)NumberFormat.getInstance(Locales.getLocale(locale));
		df.applyPattern(pattern);
		return df;
	}

}
