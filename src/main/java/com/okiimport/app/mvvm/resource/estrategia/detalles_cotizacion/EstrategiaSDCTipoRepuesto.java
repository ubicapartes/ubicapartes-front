package com.okiimport.app.mvvm.resource.estrategia.detalles_cotizacion;

import com.okiimport.app.model.DetalleCotizacion;

public class EstrategiaSDCTipoRepuesto<DC extends DetalleCotizacion> extends EstrategiaSortDetalleCotizacionImpl<DC> {

	@Override
	public int comparatorResolve(DC object1, DC object2, EstrategiaSortDetalleCotizacion<DC> estrategiaResolve) {
		Boolean tipoR1 = object1.getTipoRepuesto();
		Boolean tipoR2 = object2.getTipoRepuesto();
		
		if(tipoR1 == tipoR2){
			if(estrategiaResolve!=null)
				return estrategiaResolve.comparatorResolve(object1, object2, estrategiaResolve);
			
			return object1.compareWithDate(object2);
		}
		
		return tipoR1.compareTo(tipoR2);
	}

}
