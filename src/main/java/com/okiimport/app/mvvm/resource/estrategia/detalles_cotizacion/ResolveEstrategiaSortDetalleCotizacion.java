package com.okiimport.app.mvvm.resource.estrategia.detalles_cotizacion;

import java.util.List;

import com.okiimport.app.model.DetalleCotizacion;
import com.okiimport.app.service.web.SLocalizacion;

public class ResolveEstrategiaSortDetalleCotizacion<DC extends DetalleCotizacion> {
	
	private SLocalizacion sLocalizacion;
	
	private EstrategiaSortDetalleCotizacion<DC> est1, est2, est3;
	
	public ResolveEstrategiaSortDetalleCotizacion(SLocalizacion sLocalizacion){
		this.sLocalizacion = sLocalizacion;
	}

	public void resolveEstrategia(List<DC> lista, int tipo){
		switch(tipo){
		case 1: resolvePrecioTipoRepuestoUbicacion().sortDetalleCotizacion(lista); break;
		case 2: resolvePrecioUbicacionTipoRepuesto().sortDetalleCotizacion(lista); break;
		case 3: resolveTipoRepuestoPrecioUbicacion().sortDetalleCotizacion(lista); break;
		case 4: resolveTipoRepuestoUbicacionPrecio().sortDetalleCotizacion(lista); break;
		case 5: resolveUbicacionPrecioTipoRepuesto().sortDetalleCotizacion(lista); break;
		case 6: resolveUbicacionTipoRepuestoPrecio().sortDetalleCotizacion(lista); break;
		default: break;
		}
	}

	/**METODOS PROPIOS DE LA CLASE*/
	private EstrategiaSortDetalleCotizacion<DC> resolvePrecioTipoRepuestoUbicacion(){
		est1 = new EstrategiaSDCPrecio<DC>();
		est2 = new EstrategiaSDCTipoRepuesto<DC>();
		est3 = new EstrategiaSDCUbicacion<DC>(sLocalizacion);
		return updateEstrategias();
	}
	
	private EstrategiaSortDetalleCotizacion<DC> resolvePrecioUbicacionTipoRepuesto() {
		est1 = new EstrategiaSDCPrecio<DC>();
		est2 = new EstrategiaSDCUbicacion<DC>(sLocalizacion);
		est3 = new EstrategiaSDCTipoRepuesto<DC>();
		return updateEstrategias();
	}
	
	private EstrategiaSortDetalleCotizacion<DC> resolveTipoRepuestoPrecioUbicacion() {
		est1 = new EstrategiaSDCTipoRepuesto<DC>();
		est2 = new EstrategiaSDCPrecio<DC>();
		est3 = new EstrategiaSDCUbicacion<DC>(sLocalizacion);
		return updateEstrategias();
	}
	
	private EstrategiaSortDetalleCotizacion<DC> resolveTipoRepuestoUbicacionPrecio() {
		est1 = new EstrategiaSDCTipoRepuesto<DC>();
		est2 = new EstrategiaSDCUbicacion<DC>(sLocalizacion);
		est3 = new EstrategiaSDCPrecio<DC>();
		return updateEstrategias();
	}
	
	private EstrategiaSortDetalleCotizacion<DC> resolveUbicacionPrecioTipoRepuesto() {
		est1 = new EstrategiaSDCUbicacion<DC>(sLocalizacion);
		est2 = new EstrategiaSDCPrecio<DC>();
		est3 = new EstrategiaSDCTipoRepuesto<DC>();
		return updateEstrategias();
	}
	
	private EstrategiaSortDetalleCotizacion<DC> resolveUbicacionTipoRepuestoPrecio() {
		est1 = new EstrategiaSDCUbicacion<DC>(sLocalizacion);
		est2 = new EstrategiaSDCTipoRepuesto<DC>();
		est3 = new EstrategiaSDCPrecio<DC>();
		return updateEstrategias();
	}
	
	private EstrategiaSortDetalleCotizacion<DC> updateEstrategias(){
		est1.updateEstrategiaResolve(est2);
		est2.updateEstrategiaResolve(est3);
		return est1;
	}
}
