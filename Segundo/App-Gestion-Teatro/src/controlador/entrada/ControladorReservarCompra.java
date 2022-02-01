package controlador.entrada;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import vista.panelUsuarioLogueado.ventanaCompraEntradas.PestanaComprarEntrada;
import vista.panelUsuarioLogueado.ventanaCompraEntradas.Carrito.CardCompraEntradaCarrito;

/**
 * Clase para controlar el boton con el que se elige 
 * reservar las entradas que se muestran en el carrito de compra
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorReservarCompra implements ActionListener{

	private PestanaComprarEntrada vistaComprarEntrada;
	private CardCompraEntradaCarrito carritoCompra;
	
	public ControladorReservarCompra(PestanaComprarEntrada vistaComprarEntrada, CardCompraEntradaCarrito carritoCompra) {

		this.carritoCompra = carritoCompra;
		this.vistaComprarEntrada = vistaComprarEntrada;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (!carritoCompra.getTablaCarritoCompra().getEntradasCompraTarjeta().isEmpty() || 
				!carritoCompra.getTablaCarritoCompra().getEntradasCompraAbono().isEmpty()) {
				
				if (!vistaComprarEntrada.getRepresentacionVistaEntrada().reservarRepresentacionEntrada(carritoCompra.getTablaCarritoCompra().getEntradasCompraAbono()) &&
						!vistaComprarEntrada.getRepresentacionVistaEntrada().reservarRepresentacionEntrada(carritoCompra.getTablaCarritoCompra().getEntradasCompraTarjeta())) {
					JOptionPane.showMessageDialog(vistaComprarEntrada, "La reserva no se ha realizado correctamente", "Error", JOptionPane.ERROR_MESSAGE);
					vistaComprarEntrada.showElegirRepreEntrada();
					carritoCompra.getTablaCarritoCompra().delEntradasCompraAbono();
					carritoCompra.getTablaCarritoCompra().delEntradasCompraTarjeta();
					vistaComprarEntrada.getCardCarritoCompra().delEntradasCompra();
					vistaComprarEntrada.getCardZonaEntrada().destruirTablaZonas();
					vistaComprarEntrada.getCardZonaCompuestaEntrada().destruirTablaZonasCompuestas();
					vistaComprarEntrada.getCardCarritoCompra().getTablaCarritoCompra().destruirTablaCompra();
					return;
				}				
				JOptionPane.showMessageDialog(vistaComprarEntrada, "La reserva se ha enfectuado correctamente");		
				
				carritoCompra.getTablaCarritoCompra().delEntradasCompraAbono();
				carritoCompra.getTablaCarritoCompra().delEntradasCompraTarjeta();
				
				vistaComprarEntrada.showElegirRepreEntrada();
				vistaComprarEntrada.getCardCarritoCompra().delEntradasCompra();
				vistaComprarEntrada.getCardZonaEntrada().destruirTablaZonas();
				vistaComprarEntrada.getCardZonaCompuestaEntrada().destruirTablaZonasCompuestas();
				vistaComprarEntrada.getCardCarritoCompra().getTablaCarritoCompra().destruirTablaCompra();
			}
		}
	}

