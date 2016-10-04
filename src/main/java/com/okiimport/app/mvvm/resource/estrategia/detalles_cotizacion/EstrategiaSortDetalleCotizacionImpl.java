package com.okiimport.app.mvvm.resource.estrategia.detalles_cotizacion;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.okiimport.app.model.DetalleCotizacion;

public abstract class EstrategiaSortDetalleCotizacionImpl<DC extends DetalleCotizacion> 
	implements EstrategiaSortDetalleCotizacion<DC> {

	protected EstrategiaSortDetalleCotizacion<DC> estrategiaResolve;
	
	@Override
	public void sortDetalleCotizacion(List<DC> detallesCotizacion) {
		if(!detallesCotizacion.isEmpty() && detallesCotizacion.size()>1){
			Collections.sort(detallesCotizacion, new Comparator<DC>(){

				@Override
				public int compare(DC object1, DC object2) {
					return comparatorResolve(object1, object2, estrategiaResolve);
				}
				
			});
		}
	}
	
	@Override
	public EstrategiaSortDetalleCotizacion<DC> updateEstrategiaResolve(EstrategiaSortDetalleCotizacion<DC> estrategiaResolve){
		setEstrategiaResolve(estrategiaResolve);
		return this;
	}

	/**SETTERS Y GETTERS*/
	public EstrategiaSortDetalleCotizacion<DC> getEstrategiaResolve() {
		return estrategiaResolve;
	}

	public void setEstrategiaResolve(EstrategiaSortDetalleCotizacion<DC> estrategiaResolve) {
		this.estrategiaResolve = estrategiaResolve;
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
}
