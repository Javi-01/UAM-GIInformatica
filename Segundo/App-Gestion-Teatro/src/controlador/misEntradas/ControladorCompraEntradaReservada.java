package controlador.misEntradas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import es.uam.eps.padsof.tickets.NonExistentFileException;
import es.uam.eps.padsof.tickets.UnsupportedImageTypeException;
import modelo.sistema.Sistema;
import vista.panelUsuarioLogueado.misEntradas.PestanaMisEntradasCentre;
import vista.panelUsuarioLogueado.misEntradas.VentanaElegirOpcionEnEntrada;
import vista.panelUsuarioLogueado.misEntradas.VentanaTarjetaMisEntradas;

/**
* Clase para controlar el boton de comprar, primero se pide la tarjeta de credito por 
* mediacion de una ventana flotante, y a continuacion, una vez confirma, si la 
* tarjeta es correcta, se compra
*
* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
*/
public class ControladorCompraEntradaReservada implements ActionListener{

	private PestanaMisEntradasCentre vistaEntradas;
	private VentanaTarjetaMisEntradas vistaTarjeta;
	private VentanaElegirOpcionEnEntrada vistaMisEntradas;
	
	/**
	* Contructor de la clase que tras mostrar la pestaña de comprar, y se pulsa el boton de confirmar,
	* se valida si ese puede ser comprada, y en caso afirmativo, la compra
	* 
	* @param vistaEntradas es la pestaña principal dentro del panel
	* @param vistaTarjeta es la ventana de tarjetas
	* @param vostaMisEntradas es la ventana que muestra las opciones
	*
	* @author Jaime Diaz, Javier Fraile, Ivan Fernandez
	*/
	public ControladorCompraEntradaReservada(PestanaMisEntradasCentre vistaEntradas,
			VentanaTarjetaMisEntradas vistaTarjeta, VentanaElegirOpcionEnEntrada vostaMisEntradas) {

		this.vistaEntradas = vistaEntradas;
		this.vistaTarjeta = vistaTarjeta;
		this.vistaMisEntradas = vostaMisEntradas;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (!Sistema.getInstance().getSistemaUsuarioLogeado().comprarTarjetaRepresentacionEntradaReservada(
					vistaTarjeta.getTxtTarjetaCompra(), vistaEntradas.getMisEntradasEntradaAsociada())) {
				JOptionPane.showMessageDialog(vistaMisEntradas, "La compra no se ha podido realizar correctamente");
				return;
			}
		} catch (NonExistentFileException | UnsupportedImageTypeException e1) {
			JOptionPane.showMessageDialog(vistaMisEntradas, "Ha habido un problema realizando la compra");
			return;
		}
		
		JOptionPane.showMessageDialog(vistaMisEntradas, "La compra se ha efectuado correctamente");
		vistaEntradas.getCardEntradasCompradas().destruirTablaMisEntradasCompradas();
		vistaEntradas.getCardEntradasReservadas().destruirTablaMisEntradasReservadas();
		vistaEntradas.getCardEntradasCompradas().actualizarTablaMisEntradasCompradas(
				Sistema.getInstance().getSistemaUsuarioLogeado().getUsuarioEntradasCompradas());
		vistaEntradas.getCardEntradasReservadas().actualizarTablaMisEntradasReservadas(
				Sistema.getInstance().getSistemaUsuarioLogeado().getUsuarioEntradasReservadas());
		vistaEntradas.setMisEntradasEntradaAsociada(null);
		vistaTarjeta.limpiarTxtTarjetaCompra();
		vistaTarjeta.setVisible(false);
		vistaMisEntradas.setVisible(false);
	}

}
