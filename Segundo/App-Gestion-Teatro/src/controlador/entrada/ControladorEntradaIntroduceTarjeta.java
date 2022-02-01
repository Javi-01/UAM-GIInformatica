package controlador.entrada;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import vista.panelUsuarioLogueado.ventanaCompraEntradas.PestanaComprarEntrada;

/**
 * Clase para controlar el boton con el que se confirma la compra
 * y a paritr de ahi se muestra la ventana donde se introduce el numero 
 * de la tarjeta  
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorEntradaIntroduceTarjeta implements ActionListener{
	
	private PestanaComprarEntrada compraEntrada;
	
	public ControladorEntradaIntroduceTarjeta(PestanaComprarEntrada compraEntrada) {
		this.compraEntrada = compraEntrada;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		compraEntrada.getVentanaVistaTarjeta().setVisible(true);
		
	}
}
