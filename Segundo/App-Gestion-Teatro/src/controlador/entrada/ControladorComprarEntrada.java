package controlador.entrada;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import es.uam.eps.padsof.tickets.NonExistentFileException;
import es.uam.eps.padsof.tickets.UnsupportedImageTypeException;
import vista.panelUsuarioLogueado.ventanaCompraEntradas.PestanaComprarEntrada;
import vista.panelUsuarioLogueado.ventanaCompraEntradas.Carrito.CardCompraEntradaCarrito;
import vista.panelUsuarioLogueado.ventanaCompraEntradas.Carrito.VentanaIntroducirTarjetaCredito;

/**
 * Clase para controlar el boton con el que se confirma
 * la compra de entradas de la cesta  
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorComprarEntrada implements ActionListener{

	private VentanaIntroducirTarjetaCredito vistaTarjetaIntroducida;
	private PestanaComprarEntrada vistaCompraEntrada;
	private CardCompraEntradaCarrito carritoCompra;
	
	
	public ControladorComprarEntrada(VentanaIntroducirTarjetaCredito vistaTarjetaIntroducida,
			PestanaComprarEntrada vistaCompraEntrada, CardCompraEntradaCarrito carritoCompra) {
		
		this.vistaTarjetaIntroducida = vistaTarjetaIntroducida;
		this.vistaCompraEntrada = vistaCompraEntrada;
		this.carritoCompra = carritoCompra;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
				
		if (!carritoCompra.getTablaCarritoCompra().getEntradasCompraTarjeta().isEmpty() || 
				!carritoCompra.getTablaCarritoCompra().getEntradasCompraAbono().isEmpty()) {
			try {
				
				if (vistaCompraEntrada.getRepresentacionVistaEntrada().comprarRepresentacionEntradaTarjeta(
						carritoCompra.getTablaCarritoCompra().getEntradasCompraTarjeta(), vistaTarjetaIntroducida.getTxtTarjetaCompra()) &&
						vistaCompraEntrada.getRepresentacionVistaEntrada().comprarRepresentacionEntradaAbono(
								carritoCompra.getTablaCarritoCompra().getEntradasCompraAbono())) {
					
					JOptionPane.showMessageDialog(vistaCompraEntrada, "Compra realizada correctamente");
				}else {
					JOptionPane.showMessageDialog(vistaCompraEntrada, "La compra con no se ha enfectuado correctamente", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				vistaCompraEntrada.showElegirRepreEntrada();
				
			} catch (NonExistentFileException | UnsupportedImageTypeException e1) {
				JOptionPane.showMessageDialog(vistaCompraEntrada, "La compra con tarjeta no se ha enfectuado correctamente", "Error", JOptionPane.ERROR_MESSAGE);
				carritoCompra.getTablaCarritoCompra().delEntradasCompraAbono();
				carritoCompra.getTablaCarritoCompra().delEntradasCompraTarjeta();
		
				vistaCompraEntrada.getCardZonaEntrada().destruirTablaZonas();
				vistaCompraEntrada.getCardZonaCompuestaEntrada().destruirTablaZonasCompuestas();
				vistaTarjetaIntroducida.limpiarTxtTarjetaCompra();
				vistaCompraEntrada.getCardCarritoCompra().delEntradasCompra();
				vistaTarjetaIntroducida.setVisible(false);
				return;
			} finally {
				
				carritoCompra.getTablaCarritoCompra().delEntradasCompraAbono();
				carritoCompra.getTablaCarritoCompra().delEntradasCompraTarjeta();
		
				vistaCompraEntrada.getCardZonaEntrada().destruirTablaZonas();
				vistaCompraEntrada.getCardZonaCompuestaEntrada().destruirTablaZonasCompuestas();
				vistaTarjetaIntroducida.limpiarTxtTarjetaCompra();
				vistaCompraEntrada.getCardCarritoCompra().delEntradasCompra();
				vistaTarjetaIntroducida.setVisible(false);
			}

				carritoCompra.getTablaCarritoCompra().delEntradasCompraAbono();
				carritoCompra.getTablaCarritoCompra().delEntradasCompraTarjeta();
				
				vistaCompraEntrada.getCardZonaEntrada().destruirTablaZonas();
				vistaCompraEntrada.getCardCarritoCompra().delEntradasCompra();
				vistaCompraEntrada.getCardZonaCompuestaEntrada().destruirTablaZonasCompuestas();
				vistaCompraEntrada.getCardCarritoCompra().getTablaCarritoCompra().destruirTablaCompra();
				vistaTarjetaIntroducida.limpiarTxtTarjetaCompra();
				vistaTarjetaIntroducida.setVisible(false);
			}
		}
}
