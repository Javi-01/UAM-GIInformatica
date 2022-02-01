package controlador.misEntradas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import modelo.sistema.Sistema;
import vista.panelUsuarioLogueado.misEntradas.PestanaMisEntradasCentre;
import vista.panelUsuarioLogueado.misEntradas.VentanaElegirOpcionEnEntrada;

/**
* Clase para controlar el boton que cancela la reserva de una entrada dentro del 
* panel de mis entradas, para ello, se mira primero si esa entrada se puede cancelar 
* correctamente, y actualiza posteriormente las tablas
*
* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
*/
public class ControladorBotonCancelarReserva implements ActionListener{

	private PestanaMisEntradasCentre vistaMisEntradas;
	private VentanaElegirOpcionEnEntrada vistaOpcion;
	
	/**
	* Contructor de la clase que recibe la pestaña principal de mis entradas, 
	* y la opcion a elegir de mis entradas que se corresponde con la ventana flotantes
	* accionando el boton de reservar
	* 
	* @param vistaMisEntradas es el panel principal de mis entradas
	* @param vistaOpcion es la ventana con los botones de las opciones
	*
	* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
	*/
	public ControladorBotonCancelarReserva(PestanaMisEntradasCentre vistaMisEntradas,
			VentanaElegirOpcionEnEntrada vistaOpcion) {
		
		this.vistaMisEntradas = vistaMisEntradas;
		this.vistaOpcion = vistaOpcion;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (!Sistema.getInstance().getSistemaUsuarioLogeado().cancelarUsuarioEntradasReservadasRepresentacion(vistaMisEntradas.getMisEntradasEntradaAsociada())) {
			JOptionPane.showMessageDialog(vistaMisEntradas, "Ha habido un problema cancelando la compra");
			return;
		}
		JOptionPane.showMessageDialog(vistaMisEntradas, "La entrada se ha podido cancelar correctamente");
		vistaMisEntradas.getCardEntradasCompradas().destruirTablaMisEntradasCompradas();
		vistaMisEntradas.getCardEntradasReservadas().destruirTablaMisEntradasReservadas();
		vistaMisEntradas.getCardEntradasCompradas().actualizarTablaMisEntradasCompradas(
				Sistema.getInstance().getSistemaUsuarioLogeado().getUsuarioEntradasCompradas());
		vistaMisEntradas.getCardEntradasReservadas().actualizarTablaMisEntradasReservadas(
				Sistema.getInstance().getSistemaUsuarioLogeado().getUsuarioEntradasReservadas());
		vistaMisEntradas.setMisEntradasEntradaAsociada(null);
		vistaOpcion.setVisible(false);
	}

}
