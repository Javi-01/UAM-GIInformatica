package controlador.crearEvento;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import vista.panelAdministrador.pestanaCrearEvento.PestanaCrearEvento;
import vista.panelAdministrador.pestanaCrearEvento.precioZonas.PestanaCrearEventoPrecioZona;

/**
 * Clase para controlar el boton con el que se cancela la 
 * creacion de un evento
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorCancelarCrearEvento implements ActionListener{

	private PestanaCrearEvento vistaCrearEvento;
	private PestanaCrearEventoPrecioZona vistaBtCancelarEvento;
	
	public ControladorCancelarCrearEvento(PestanaCrearEvento vistaCrearEvento, PestanaCrearEventoPrecioZona vistaBtCancelarEvento) {

		this.vistaCrearEvento = vistaCrearEvento;
		this.vistaBtCancelarEvento = vistaBtCancelarEvento;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		vistaBtCancelarEvento.limpiarTxtPrecioZona();
		vistaCrearEvento.showPestanaRellenarCampos();
	}
}
