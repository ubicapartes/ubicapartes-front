package com.okiimport.app.mvvm.controladores.maestros;

import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.GregorianCalendar;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.okiimport.app.model.HistoricoMoneda;
import com.okiimport.app.model.Moneda;
import com.okiimport.app.model.enumerados.EEstatusGeneral;
import com.okiimport.app.mvvm.AbstractRequerimientoViewModel;
import com.okiimport.app.mvvm.model.ModeloCombo;
import com.okiimport.app.mvvm.resource.BeanInjector;
import com.okiimport.app.service.transaccion.STransaccion;

public class RegistrarMonedaViewModel extends AbstractRequerimientoViewModel{
	
	
	//Servicios
		@BeanInjector("sTransaccion")
		private STransaccion sTransaccion;
		
		//GUI
		@Wire("#winMoneda")
		private Window winMoneda;
		
		
		@Wire("#btnLimpiar")
		private Button btnLimpiar;
		
		@Wire("#idNombre")
		public Textbox idNombre;
		
		@Wire("#idSimbolo")
		public Textbox idSimbolo;
		
		@Wire("#idMontoConversion")
		public Textbox idMontoConversion;
		
		//Atributos
		private boolean makeAsReadOnly;
		protected Boolean cerrar;
		private String recordMode;
		protected Moneda moneda;
		private EEstatusGeneral estatus;
		protected HistoricoMoneda historico;
		private float mConversionViejo;
		protected HistoricoMoneda historicoNuevo;
		private List<ModeloCombo<Boolean>> tiposPais;
		private ModeloCombo<Boolean> tipoPais;
		
		/**
		 * Descripcion: Llama a inicializar la clase 
		 * Parametros: @param view: formularioMoneda.zul 
		 * Retorno: Ninguno
		 * Nota: Ninguna
		 * */
		@AfterCompose
		public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view,
				@ExecutionArgParam("moneda") Moneda moneda,
				@ExecutionArgParam("recordMode") String recordMode,
				@ExecutionArgParam("cerrar") Boolean cerrar){
			try{
			super.doAfterCompose(view);
			tiposPais = llenarListaTipoProveedor();
			tipoPais = tiposPais.get(0);
			this.recordMode = (recordMode == null) ? "EDIT" : recordMode;
			this.moneda = (moneda==null) ? new Moneda() :  moneda;
			
			
			
			//System.out.println("Moneda inicio  "+moneda.getIdMoneda());
			
			if(moneda!=null)
				
			{
				
			for(int i=0; i<this.moneda.getHistoricoMonedas().size(); i++)
				
			{	System.out.println("Historico numero "+i+"  "+this.moneda.getHistoricoMonedas().get(i).getEstatus()+"Monto  "+ this.moneda.getHistoricoMonedas().get(i).getMontoConversion());
				if(this.moneda.getHistoricoMonedas().get(i).getEstatus()==EEstatusGeneral.ACTIVO)
					this.historico = this.moneda.getHistoricoMonedas().get(i);
			}
			
				
				mConversionViejo= historico.getMontoConversion();
				System.out.println("Historico "+ moneda.getHistoricoMonedas().size());}
			else
			{
				this.historico = (historico==null) ? new HistoricoMoneda() :  historico;
				this.historicoNuevo = (historicoNuevo==null) ? new HistoricoMoneda() :  historicoNuevo;
			}
			
			
			if(moneda!=null){
				
				System.out.println("PAIS"+ tipoPais.getNombre()+tipoPais.getValor());
			
				tipoPais = consultarTipoPais(this.moneda.getPais().toString(),tiposPais);
			System.out.println("Historico "+ this.historico.getMontoConversion());}
			this.cerrar = (cerrar==null) ? true : cerrar;
			makeAsReadOnly = (recordMode != null && recordMode.equalsIgnoreCase("READ"))? true : false;
			} catch (Exception e) {
			    System.out.println("xxxxxx   "+e.getMessage());
			}
			
		}
		
		
		/**
		 * Descripcion: Permite Habilitar el boton limpiar segun el evento que se solicite
		 * Parametros: @param view: formularioMoneda.zul 
		 * Retorno: Ninguno 
		 * Nota: Ninguna
		 * */
		@Command
		public void habilitarBtnLimpiar(@BindingParam("id") String id) {
			
				btnLimpiar.setVisible(false);
		}
		
		
		/**
		 * Descripcion: Permite limpiar los campos del formulario registrar moneda
		 * Parametros: @param view: formularioMoneda.zul 
		 * Retorno: Ninguno 
		 * Nota: Ninguna
		 * */
		@Command
		@NotifyChange({ "moneda","historico"})
		public void limpiar() {
			moneda = new Moneda();
			historico = new HistoricoMoneda();
			//limpiarEstadoYCiudad();
		}
		
		
		/**
		 * Descripcion: Permite Registrar la Moneda
		 * Parametros: @param view: formularioMoneda.zul 
		 * Retorno: Ninguno
		 * Nota: Ninguna
		 * */
		@Command
		public void registrar(@BindingParam("btnEnviar") Button btnEnviar,
				@BindingParam("btnLimpiar") Button btnLimpiar,
				@BindingParam("recordMode") String recordMode) {
			try{
				if (checkIsFormValid()) {
			}
				
						btnEnviar.setDisabled(true);
						btnLimpiar.setDisabled(true);
						if(moneda.getIdMoneda()==null)
							registrarMonedaNueva(cerrar);
						else
							registrarMoneda(cerrar);
						
						limpiar();
				
			
		} catch (ConcurrentModificationException e) {
		    System.out.println(e.getMessage());
		}
		
		}
		
		
		
		/**
		 * Descripcion: Permite registrar una moneda en el sistema
		 * Parametros: @param view: formularioMoneda.zul 
		 * Retorno: Ninguno
		 * Nota: Ninguna
		 * */
		protected Moneda registrarMoneda(boolean enviarEmail){
			
			Calendar c1 = GregorianCalendar.getInstance();
			//moneda.setEstatus(estatus.ACTIVO);

				moneda.setPais(tipoPais.getValor());//moneda.setPais(false);
				System.out.println("moneda "+moneda.getNombre());
				System.out.println("conversion actual 1 "+mConversionViejo+"  Conversion vieja"+historico.getMontoConversion());
			if (historico.getMontoConversion()!=mConversionViejo)
			{
				System.out.println("conversion actual 2 "+mConversionViejo+"  Conversion vieja"+moneda.getHistoricoMonedas().get(moneda.getHistoricoMonedas().size()-1).getMontoConversion());
				this.historicoNuevo= new HistoricoMoneda();
				historicoNuevo.setEstatus(EEstatusGeneral.ACTIVO);
				historicoNuevo.setFechaCreacion(c1.getTime());
				historicoNuevo.setMontoConversion(historico.getMontoConversion());
				historicoNuevo.setMoneda(moneda);
				historicoNuevo=sMaestros.registrarHistorico(this.historicoNuevo);
				historico.setEstatus(EEstatusGeneral.INACTIVO);
				System.out.println("conversion vieja  "+mConversionViejo);
				this.historico.setMontoConversion(mConversionViejo);
				System.out.println("conversion vieja a guardar "+this.historico.getMontoConversion());
				historico=sMaestros.registrarHistorico(this.historico);
				System.out.println("conversion vieja guardada "+this.historico.getMontoConversion());
				moneda.getHistoricoMonedas().add(this.historicoNuevo);
				
			}
			historico.setMontoConversion(moneda.getHistoricoMonedas().get(moneda.getHistoricoMonedas().size()-1).getMontoConversion());
			moneda = sMaestros.registrarMoneda(this.moneda);
			String str = null;
			
				str = "Moneda actualizada Exitosamente";

			

				
				mostrarMensaje("Informacion", str, null, null,
						new EventListener<Event>() {
							public void onEvent(Event event) throws Exception {
								winMoneda.onClose();
								ejecutarGlobalCommand("consultarMonedas", null);
							}
						}, null);
				limpiar();
			return moneda;
			
		}

		@Command	
		protected Moneda registrarMonedaNueva(boolean enviarEmail){
			
			Calendar c1 = GregorianCalendar.getInstance();
			
			this.moneda.setEstatus(EEstatusGeneral.ACTIVO);
			this.moneda.setFechaCreacion(c1.getTime());
			this.moneda.setPais(tipoPais.getValor());//moneda.setPais(false);
			System.out.println("monedan "+moneda.getNombre());
			sMaestros.registrarMoneda(this.moneda);
				HistoricoMoneda	historicoN= new HistoricoMoneda();
				historicoN.setEstatus(EEstatusGeneral.ACTIVO);
				historicoN.setFechaCreacion(c1.getTime());
				historicoN.setMoneda(moneda);
				historicoN.setMontoConversion(this.historico.getMontoConversion());
				System.out.println("Historico n "+historicoN.getMontoConversion());
				historicoN=sMaestros.registrarHistorico(historicoN);
				//moneda.getHistoricoMonedas().add(historicoN);
				System.out.println("guarde");
			
			String str = null;
			
				str = "Moneda actualizada Exitosamente";

			

				
				mostrarMensaje("Informacion", str, null, null,
						new EventListener<Event>() {
							public void onEvent(Event event) throws Exception {
								winMoneda.onClose();
								ejecutarGlobalCommand("consultarMonedas", null);
							}
						}, null);
			
			return moneda;
		}
		
		private ModeloCombo<Boolean> consultarTipoPais(String pais, List <ModeloCombo<Boolean>> tiposPais){
			if (pais!=null){
				
				for(ModeloCombo<Boolean> tipoPaiss: tiposPais )
					if (tipoPaiss.getValor().toString().equalsIgnoreCase(pais))
						return tipoPaiss;
			}
			return this.tipoPais;
		}

		public STransaccion getsTransaccion() {
			return sTransaccion;
		}


		public void setsTransaccion(STransaccion sTransaccion) {
			this.sTransaccion = sTransaccion;
		}


		public Window getWinMoneda() {
			return winMoneda;
		}


		public void setWinMoneda(Window winMoneda) {
			this.winMoneda = winMoneda;
		}


		public Button getBtnLimpiar() {
			return btnLimpiar;
		}


		public void setBtnLimpiar(Button btnLimpiar) {
			this.btnLimpiar = btnLimpiar;
		}


		public boolean isMakeAsReadOnly() {
			return makeAsReadOnly;
		}


		public void setMakeAsReadOnly(boolean makeAsReadOnly) {
			this.makeAsReadOnly = makeAsReadOnly;
		}


		public Boolean getCerrar() {
			return cerrar;
		}


		public void setCerrar(Boolean cerrar) {
			this.cerrar = cerrar;
		}


		public String getRecordMode() {
			return recordMode;
		}


		public void setRecordMode(String recordMode) {
			this.recordMode = recordMode;
		}


		public Moneda getMoneda() {
			return moneda;
		}


		public void setMoneda(Moneda moneda) {
			this.moneda = moneda;
		}


		public EEstatusGeneral getEstatus() {
			return estatus;
		}


		public void setEstatus(EEstatusGeneral estatus) {
			this.estatus = estatus;
		}


		public HistoricoMoneda getHistorico() {
			return historico;
		}


		public void setHistorico(HistoricoMoneda historico) {
			this.historico = historico;
		}


		public List<ModeloCombo<Boolean>> getTiposPais() {
			return tiposPais;
		}


		public void setTiposPais(List<ModeloCombo<Boolean>> tiposPais) {
			this.tiposPais = tiposPais;
		}


		public ModeloCombo<Boolean> getTipoPais() {
			return tipoPais;
		}


		public void setTipoPais(ModeloCombo<Boolean> tipoPais) {
			this.tipoPais = tipoPais;
		}
		


}
