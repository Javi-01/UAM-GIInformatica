package controlador.abonos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import vista.panelUsuarioLogueado.pestanaCompraAbono.PestanaCompraAbonoCenter;

/**
 * Clase para controlar la introduccion de la tarjeta al comprar un abono
 *
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorAbonoIntroduceTarjeta implements ActionListener{

	private PestanaCompraAbonoCenter compraAbono;
	
	/**
	 * constructor del action listener encargado del boton 
	 * que se encuentra en el panel de comprar abono 
	 *
	 * @param compraAbono jpanel de de la comproa de abono	
	 */
	public ControladorAbonoIntroduceTarjeta(PestanaCompraAbonoCenter compraAbono) {
		this.compraAbono = compraAbono;
	}
	
	/**
	 * metodo que controla el boton 
	 *
	 * @param e evento que sucede al presionar sobre el boton de comprar
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		compraAbono.getVentanaVistaTarjeta().setVisible(true);		
	}
}
