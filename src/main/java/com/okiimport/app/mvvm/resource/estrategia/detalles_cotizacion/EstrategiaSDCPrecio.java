package com.okiimport.app.mvvm.resource.estrategia.detalles_cotizacion;

import com.okiimport.app.model.DetalleCotizacion;

public class EstrategiaSDCPrecio<DC extends DetalleCotizacion> extends EstrategiaSortDetalleCotizacionImpl<DC> {

	@Override
	public int comparatorResolve(DC object1, DC object2, EstrategiaSortDetalleCotizacion<DC> estrategiaResolve) {
		Float totalObj1 = object1.calcularTotalConvert();
		Float totalObj2 = object2.calcularTotalConvert();

		if(totalObj1 == totalObj2){
			if(estrategiaResolve!=null)
				return estrategiaResolve.comparatorResolve(object1, object2, estrategiaResolve);
			
			return object1.compareWithDate(object2);
		}

		return totalObj1.compareTo(totalObj2);
	}
}
