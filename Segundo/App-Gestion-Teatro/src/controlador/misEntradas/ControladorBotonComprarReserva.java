package controlador.misEntradas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import es.uam.eps.padsof.tickets.NonExistentFileException;
import es.uam.eps.padsof.tickets.UnsupportedImageTypeException;
import modelo.sistema.Sistema;
import vista.panelUsuarioLogueado.misEntradas.PestanaMisEntradasCentre;
import vista.panelUsuarioLogueado.misEntradas.VentanaTarjetaMisEntradas;

/**
* Clase para controlar el boton que compra la reserva de una entrada dentro del 
* panel de mis entradas, para ello, se mira primero que esa entrada la pueda comprar
* con abono, y si es asi, la compra directamente
*
* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
*/
public class ControladorBotonComprarReserva implements ActionListener{

	PestanaMisEntradasCentre vistaMisEntradas;	
	VentanaTarjetaMisEntradas vistaTarjeta;
	
	/**
	* Contructor de la clase que recibe la pestaña principal de mis entradas, 
	* y la opcion a elegir de mis entradas que se corresponde con la ventana flotantes
	* accionando el boton de comprar
	* 
	* @param vistaMisEntradas es el panel principal de mis entradas
	* @param vistaTarjeta es la ventana con los botones de las opciones
	*
	* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
	*/
	public ControladorBotonComprarReserva(PestanaMisEntradasCentre vistaMisEntradas,
			VentanaTarjetaMisEntradas vistaTarjeta) {
		this.vistaMisEntradas = vistaMisEntradas;
		this.vistaTarjeta = vistaTarjeta;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		try {
			if (Sistema.getInstance().getSistemaUsuarioLogeado().comprarAbonoRepresentacionEntradaReservada(vistaMisEntradas.getMisEntradasEntradaAsociada())) {
				JOptionPane.showMessageDialog(vistaMisEntradas, "La entrada se ha podido comprar con abono correctamente");
				vistaMisEntradas.getCardEntradasCompradas().destruirTablaMisEntradasCompradas();
				vistaMisEntradas.getCardEntradasReservadas().destruirTablaMisEntradasReservadas();
				vistaMisEntradas.getCardEntradasCompradas().actualizarTablaMisEntradasCompradas(
						Sistema.getInstance().getSistemaUsuarioLogeado().getUsuarioEntradasCompradas());
				vistaMisEntradas.getCardEntradasReservadas().actualizarTablaMisEntradasReservadas(
						Sistema.getInstance().getSistemaUsuarioLogeado().getUsuarioEntradasReservadas());
				vistaMisEntradas.setMisEntradasEntradaAsociada(null);
				vistaMisEntradas.getVentanaOpcionReserva().setVisible(false);	
				return;
			}
		} catch (NonExistentFileException | UnsupportedImageTypeException e1) {
			e1.printStackTrace();
		}	
		vistaMisEntradas.getVentanaOpcionReserva().setVisible(false);	
		vistaTarjeta.setVisible(true);
	}

}
