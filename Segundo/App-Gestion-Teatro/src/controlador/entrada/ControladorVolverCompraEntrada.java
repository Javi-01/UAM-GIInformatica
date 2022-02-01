package controlador.entrada;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import vista.panelUsuarioLogueado.VentanaElegirCompraSentado;
import vista.panelUsuarioLogueado.VentanaNumeroDeEntradasPie;
import vista.panelUsuarioLogueado.VentanaNumeroDeEntradasSentado;
import vista.panelUsuarioLogueado.ventanaCompraEntradas.PestanaComprarEntrada;

/**
 * Clase para controlar el boton de volver atras dentro de los 
 * paneles de elegir zonas, representaciones o carrito de compra  
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorVolverCompraEntrada implements ActionListener{

	private PestanaComprarEntrada vistaCompraEntrada;
	private VentanaNumeroDeEntradasPie numPie;
	private VentanaNumeroDeEntradasSentado numSentado;
	private VentanaElegirCompraSentado compraSentado;
	
	public ControladorVolverCompraEntrada (PestanaComprarEntrada vistaCompraEntrada, VentanaNumeroDeEntradasPie numPie,
			VentanaNumeroDeEntradasSentado numSentado, VentanaElegirCompraSentado compraSentado) {
		
		this.compraSentado = compraSentado;
		this.numPie = numPie;
		this.numSentado = numSentado;
		this.vistaCompraEntrada = vistaCompraEntrada;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (!vistaCompraEntrada.getCardCarritoCompra().getTablaCarritoCompra().getEntradasCompraAbono().isEmpty()) {
			vistaCompraEntrada.getCardCarritoCompra().getTablaCarritoCompra().delEntradasCompraAbono();
		}
		
		if (!vistaCompraEntrada.getCardCarritoCompra().getTablaCarritoCompra().getEntradasCompraTarjeta().isEmpty()) {
			vistaCompraEntrada.getCardCarritoCompra().getTablaCarritoCompra().delEntradasCompraTarjeta();
		}
		vistaCompraEntrada.getCardCarritoCompra().delEntradasCompra();
		vistaCompraEntrada.setZonaVistaEntrada(null);
		compraSentado.showCardMenuSugerencias();
		compraSentado.setVisible(false);
		numPie.setVisible(false);
		numSentado.setVisible(false);
		compraSentado.getCardElegirEntradas().limpiarEntradas();
		vistaCompraEntrada.getCardZonaEntrada().destruirTablaZonas();
		vistaCompraEntrada.getCardZonaCompuestaEntrada().destruirTablaZonasCompuestas();
		vistaCompraEntrada.getCardCarritoCompra().getTablaCarritoCompra().destruirTablaCompra();
		vistaCompraEntrada.showElegirRepreEntrada();
	}
}
