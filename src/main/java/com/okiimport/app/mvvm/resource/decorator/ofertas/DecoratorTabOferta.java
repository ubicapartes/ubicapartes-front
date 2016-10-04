package com.okiimport.app.mvvm.resource.decorator.ofertas;

import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;

import com.okiimport.app.model.DetalleOferta;
import com.okiimport.app.model.Oferta;
import com.okiimport.app.model.enumerados.EEstatusOferta;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;


public class DecoratorTabOferta extends AbstractRequerimientoViewModel {
	
	public static final String LISTITEM_RED = "listitem-red";
	public static final String LISTITEM_GREEN = "listitem-green";
	
	//Listener
	private OnComunicatorOfertaListener listener;
		
	//Atributos
	private Oferta oferta;
	private Boolean visibleBtnOfertas;
	
	@Init
    public void init(@ExecutionArgParam("listener") OnComunicatorOfertaListener listener, 
    		@ExecutionArgParam("oferta") Oferta oferta){
		this.listener = listener;
		this.setOferta(oferta);
		for(DetalleOferta detalle : oferta.getDetalleOfertas()){
			detalle.setAprobado(null);
		}
	}
	
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		super.doAfterCompose(view);
	}
	
	/**COMMANDS*/
	@Command
	@NotifyChange({"oferta", "visibleBtnOfertas"})
	public void updateOferta(@BindingParam("acept") boolean acept){
		List<DetalleOferta> detalles = this.oferta.getDetalleOfertas();
		for(DetalleOferta detalle : detalles)
			detalle.setAprobado(acept);
		cambiarEstatusOferta(acept);
	}
	
	@Command
	@NotifyChange({"oferta", "visibleBtnOfertas"})
	public void aprobar(@BindingParam("detalleOferta") DetalleOferta detalleOferta,
			@BindingParam("acept") boolean acept){
		detalleOferta.setAprobado(acept);
		updateRow(detalleOferta);
		cambiarEstatusOferta(acept);
	}
	
	@Command
	@NotifyChange("oferta")
	public void recotizar(@BindingParam("actions") Component actions){
		if(oferta.validoParaRecotizar()){
			listener.registrarRecotizacion(oferta);
			applyRecotizacion(actions);
		}
		else
			listener.mostrarMensajeInvalidaRecotizacion(this, oferta);
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	//Metodos Publicos
	public String updateRow(DetalleOferta detalle){
		Boolean acept = detalle.getAprobado();
		if(acept == null)
			return "";
		else
			return (acept) ? LISTITEM_GREEN : LISTITEM_RED;
	}
	
	public boolean isAprobado(DetalleOferta detalle, boolean btnAprobado){
		Boolean ofertaRecotizada = oferta.isReCotizacion();
		Boolean acept = null;
		if(ofertaRecotizada)
			return false;
		else if((acept=detalle.getAprobado()) == null)
			return true;
		else
			return (btnAprobado) ? !acept : acept;
	}
	
	public void applyRecotizacion(Component actions){
		for(Component child : actions.getChildren()){
			child.setVisible(false);
		}
	}
	
	//Metodos Privados
	private void cambiarEstatusOferta(boolean acept){
		if(!acept)
			oferta.setEstatus(EEstatusOferta.INVALIDA);
		else if(oferta.isAprobar())
			oferta.setEstatus(EEstatusOferta.SELECCIONADA);
		
		updateVisibleBtnOfertas();
	}
	
	private void updateVisibleBtnOfertas(){
		this.visibleBtnOfertas = (oferta.isCreada()) ? true : oferta.isAprobar();
	}
	
	/**GETTERS Y SETTERS*/
	public Oferta getOferta() {
		return oferta;
	}

	public void setOferta(Oferta oferta) {
		this.oferta = oferta;
	}

	public Boolean getVisibleBtnOfertas() {
		return visibleBtnOfertas;
	}

	public void setVisibleBtnOfertas(Boolean visibleBtnOfertas) {
		this.visibleBtnOfertas = visibleBtnOfertas;
	}



	/**
	 * Descripcion: listener para la comunicacion con el viewmodel respectivo
	 * */
	public interface OnComunicatorOfertaListener {
		void registrarRecotizacion(Oferta oferta);
		void mostrarMensajeInvalidaRecotizacion(DecoratorTabOferta decorator, Oferta oferta);
	}
}
