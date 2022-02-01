package controlador.abonos;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import vista.panelAdministrador.pestanaCrearAbonos.PestanaCrearAbonoCenter;
import vista.panelAdministrador.pestanaCrearAbonos.PestanaCrearAbonoWest;

/**
 * Clase para controlar el itemListener con el que se 
 * controla si mostrar el panel de crear abono anual o ciclo
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorFiltroCrearAbono implements ItemListener{
	
	PestanaCrearAbonoCenter vistaCrearAbono;
	PestanaCrearAbonoWest vistaCbCrearAbono;
	
	/**
	 * constructor del action listener encargado del 
	 * boton de confirmar una vez introducia la tarjeta
	 *
	 * @param vistaCrearAbono	panel desde la que se crea el abono	
	 * @param vistaCbCrearAbono		panel donde se encuentra el deslizable
	 */
	public ControladorFiltroCrearAbono (PestanaCrearAbonoCenter vistaCrearAbono, PestanaCrearAbonoWest vistaCbCrearAbono) {
		this.vistaCbCrearAbono = vistaCbCrearAbono;
		this.vistaCrearAbono = vistaCrearAbono;
	}

	/**
	 * controlador del listener del despegable
	 *
	 * @param e		evento donde se encuentran las opciones del desplegable	
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		
		if (vistaCbCrearAbono.getCombobox().getSelectedItem().toString().equals("Abono Anual")) {
			vistaCrearAbono.showPestanaCrearAbonoAnual();
			vistaCrearAbono.getPestanaCrearCiclo().getCamposCiclo().destruirElementosEnCiclo();
			vistaCrearAbono.getPestanaCrearCiclo().getTablaElementos().destruirTablaCompraCiclo();
		}else {
			vistaCrearAbono.getPestanaCrearCiclo().getTablaElementos().destruirTablaCompraCiclo();
			vistaCrearAbono.getPestanaCrearCiclo().getCamposCiclo().destruirElementosEnCiclo();
			vistaCrearAbono.showPestanaCrearCiclo();
		}
	}
	
}
