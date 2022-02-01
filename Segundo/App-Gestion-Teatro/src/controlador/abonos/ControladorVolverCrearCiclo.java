package controlador.abonos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import vista.panelAdministrador.pestanaCrearAbonos.PestanaCrearAbono;

/**
 * Clase para controlar el boton de volver al inicio de la pestaña de crear ciclo
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorVolverCrearCiclo implements ActionListener{

	private PestanaCrearAbono vistaCrearAbono;
	
	/**
	 * constructor del action listener encargado del 
	 * boton de volver 
	 *
	 * @param vistaCrearAbono		vista de crear un abono	
	 */
	public ControladorVolverCrearCiclo(PestanaCrearAbono vistaCrearAbono) {
		this.vistaCrearAbono = vistaCrearAbono;
	}

	/**
	 * controlador del evento que ocurre al pinchar en el boton de volver
	 *
	 * @param e		evento de raton que ocurre tras pinchar al boton de volver	
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		vistaCrearAbono.showPestanaBorderAbono();		
	}

}
