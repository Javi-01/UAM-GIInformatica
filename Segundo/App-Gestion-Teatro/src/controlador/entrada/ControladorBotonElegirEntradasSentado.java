package controlador.entrada;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import vista.panelUsuarioLogueado.VentanaElegirCompraSentado;

/**
 * Clase para controlar el boton con el que se elige 
 * la opcion de elegir las butacas a comprar cuando no se quieren las sugeridas  
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorBotonElegirEntradasSentado implements ActionListener{

	private VentanaElegirCompraSentado vistaCompraSentado;
	
	
	public ControladorBotonElegirEntradasSentado(VentanaElegirCompraSentado vistaCompraSentado) {
		this.vistaCompraSentado = vistaCompraSentado;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		vistaCompraSentado.showCardElegirEntradas();
	}

}
