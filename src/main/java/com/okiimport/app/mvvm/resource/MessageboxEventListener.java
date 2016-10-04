package com.okiimport.app.mvvm.resource;

import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

@SuppressWarnings("rawtypes")
public class MessageboxEventListener implements EventListener {
	
	
	private OnComunicatorListener listener;
	private Map<String, Object> params;

	public MessageboxEventListener(OnComunicatorListener listener, Map<String, Object> params) {
		this.params = params;
		this.listener = listener;
	}

	@Override
	public void onEvent(Event event) throws Exception {
		listener.onEvent(event, params);
	}
	
	/**
	 * Descripcion: interface para la comunicacion con el vm
	 * */
	public interface OnComunicatorListener {
		void onEvent(Event event, Map<String, Object> params);
	}

}
