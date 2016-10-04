package com.okiimport.app.mvvm.resource.estrategia.detalles_cotizacion;

import com.okiimport.app.model.Ciudad;
import com.okiimport.app.model.DetalleCotizacion;
import com.okiimport.app.model.Estado;
import com.okiimport.app.model.Pais;
import com.okiimport.app.model.Proveedor;
import com.okiimport.app.service.web.SLocalizacion;

public class EstrategiaSDCUbicacion<DC extends DetalleCotizacion> extends EstrategiaSortDetalleCotizacionImpl<DC> {
	
	private static Pais PaisBase = new Pais("Venezuela");
	private static Ciudad CiudadBase = new Ciudad("Barquisimeto", new Estado("Lara"));
	
	private SLocalizacion sLocalizacion;
	
	public EstrategiaSDCUbicacion(SLocalizacion sLocalizacion){
		this.sLocalizacion = sLocalizacion;
	}

	@Override
	public int comparatorResolve(DC object1, DC object2, final EstrategiaSortDetalleCotizacion<DC> estrategiaResolve) {
		Proveedor prov1 = object1.getCotizacion().getProveedor();
		Proveedor prov2 = object2.getCotizacion().getProveedor();
		
		if(prov1.equals(prov2)){
			if(estrategiaResolve!=null)
				return estrategiaResolve.comparatorResolve(object1, object2, estrategiaResolve);
			
			return object1.compareWithDate(object2);
		}
		return compararDistancias(object1, object2, estrategiaResolve);
	}

	/**METODOS PROPIOS DE LA CLASE*/
	private int compararDistancias(DC object1, DC object2, final EstrategiaSortDetalleCotizacion<DC> estrategiaResolve){
		Proveedor prov1 = object1.getCotizacion().getProveedor();
		Proveedor prov2 = object2.getCotizacion().getProveedor();
		
		//CASO 1: Ambos Proveedores Nacionales
		if(prov1.isNacional() && prov2.isNacional()){
			Double distProv1 = sLocalizacion.calcularDistancia(prov1.getCiudad(), CiudadBase);
			Double distProv2 = sLocalizacion.calcularDistancia(prov2.getCiudad(), CiudadBase);
			
			if(distProv1 == distProv2){
				if(estrategiaResolve!=null)
					return estrategiaResolve.comparatorResolve(object1, object2, estrategiaResolve);
				
				return object1.compareWithDate(object2);
			}
			
			return distProv1.compareTo(distProv2);
		}
		else if(prov1.isNacional() && !prov2.isNacional()){
			return -1;
		} 
		else if(!prov1.isNacional() && prov2.isNacional()){
			return 1;
		}
		else {
			Double distProv1 = sLocalizacion.calcularDistancia(prov1.getPais(), PaisBase);
			Double distProv2 = sLocalizacion.calcularDistancia(prov2.getPais(), PaisBase);
			
			if(distProv1 == distProv2){
				if(estrategiaResolve!=null)
					return estrategiaResolve.comparatorResolve(object1, object2, estrategiaResolve);
				
				return object1.compareWithDate(object2);
			}
			
			return distProv1.compareTo(distProv2);
		}
	}
}
