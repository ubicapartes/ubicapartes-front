package com.okiimport.app.mvvm.controladores.maestros;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;

public class RegistrarProveedorViewModel extends com.okiimport.app.mvvm.controladores.RegistrarProveedorViewModel{
	
	@AfterCompose(superclass=true)
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
		
	}
	
	@Command
	@Override
	public void registrar(@BindingParam("btnEnviar") Button btnEnviar,
			@BindingParam("btnLimpiar") Button btnLimpiar,
			@BindingParam("recordMode") String recordMode,
			@BindingParam("tipoReg") String tipoR) {
		
		setTipoRegistro(tipoR);
		
		if (checkIsFormValid() && !this.getValidacionCorreo()) {
			//esto para que permita actualizar el registro del proveedor
			System.out.println("valor es: "+this.getValor());
					if(this.getValor()!=null && this.getValor().equals("editar")){
						 registrarProveedor(false);
						 return;
					}
					
					//esta condicion es para validad que no se registre varias veces el mismo proveedor
			if(!verificarExistencia()){
				if (proveedor.getMarcaVehiculos().size() > 0
						&& proveedor.getClasificacionRepuestos().size() > 0) {

					btnEnviar.setDisabled(true);
					btnLimpiar.setDisabled(true);

					registrarProveedor(true);
				}

				else
					mostrarMensaje("Informaci\u00F3n", "Agregue al Menos una Marca y Una Clasificaci\u00F3n de Repuesto. "
							+ "(Add at Least a Brand and a Classification of Parts)",
							null, null, null, null);
			}
			else
				mostrarMensaje("Informaci\u00F3n", "Ya se encuentra registrado en el sistema"
						+ " (Already registered in the system)",
						null, null, null, null);
		}
		
	}

}
