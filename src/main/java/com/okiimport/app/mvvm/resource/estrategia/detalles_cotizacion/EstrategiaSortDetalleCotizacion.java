package com.okiimport.app.mvvm.resource.estrategia.detalles_cotizacion;

import java.util.List;

import com.okiimport.app.model.DetalleCotizacion;

public interface EstrategiaSortDetalleCotizacion<DC extends DetalleCotizacion> {
	void sortDetalleCotizacion(List<DC> detallesCotizacion);
	EstrategiaSortDetalleCotizacion<DC> updateEstrategiaResolve(EstrategiaSortDetalleCotizacion<DC> estrategiaResolve);
	int comparatorResolve(DC object1, DC object2, final EstrategiaSortDetalleCotizacion<DC> estrategiaResolve);
}
