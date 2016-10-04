package com.okiimport.app.mvvm.controladores;


import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;

public class avisoModalViewModel extends AbstractRequerimientoViewModel{
	@Wire("#winVisor")
	private Window winVisor;
	
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view)
	{	
		super.doAfterCompose(view);	
		
	}
	
	@Command
    public void cerrar() {
		try{
    		ejecutarGlobalCommand("limpiarCamposRequerimiento",null);
    		winVisor.onClose();
			}catch(Exception e){
				e.printStackTrace();
			}
          
    }


}
