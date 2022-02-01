package controlador.abonos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import vista.panelUsuarioLogueado.pestanaCompraAbono.PestanaCompraAbonoCenter;
import vista.panelUsuarioLogueado.pestanaCompraAbono.PestanaCompraAbonoWest;
import vista.panelUsuarioLogueado.ventanaCompraEntradas.Carrito.VentanaIntroducirTarjetaCredito;

/**
 * Clase para controlar el boton con el que se confirma la compra una
 * vez introducia la tarjeta al comprar un abono
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorComprarAbonoTarjeta implements ActionListener{

	private VentanaIntroducirTarjetaCredito vistaTarjeta;
	private PestanaCompraAbonoCenter vistaCompra;
	private PestanaCompraAbonoWest vistaBoxCompra;
	
	/**
	 * constructor del action listener encargado del 
	 * boton de confirmar una vez introducia la tarjeta
	 *
	 * @param vistaTarjeta		panel donde se introduce la tarjeta 
	 * @param vistaCompra		panel de compra 
	 * @param vistaBoxCompra	panel del comboBox de compra	
	 */
	public ControladorComprarAbonoTarjeta(VentanaIntroducirTarjetaCredito vistaTarjeta,
			PestanaCompraAbonoCenter vistaCompra, PestanaCompraAbonoWest vistaBoxCompra) {
		this.vistaTarjeta = vistaTarjeta;
		this.vistaCompra = vistaCompra;
		this.vistaBoxCompra = vistaBoxCompra;
	}

	/**
	 * controlador del evento que ocurre al confirmar la compra del abono
	 *
	 * @param e		evento de raton que ocurre tras confirmar la compra del abono	
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (vistaBoxCompra.getCombobox().getSelectedItem().toString().equals("Abono Anual")) {
			if (!vistaCompra.getAbonoAComprar().CompraAbonoUsuario(vistaTarjeta.getTxtTarjetaCompra())) {
				JOptionPane.showMessageDialog(vistaCompra, "La compra del abono no se ha realizado correctamente", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}else {
			if (!vistaCompra.getCicloAsociado().CompraCicloUsuario(vistaTarjeta.getTxtTarjetaCompra())) {
				JOptionPane.showMessageDialog(vistaCompra, "La compra del ciclo no se ha realizado correctamente", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		JOptionPane.showMessageDialog(vistaCompra, "La compra se ha realizado correctamente");
		
		vistaTarjeta.limpiarTxtTarjetaCompra();
		vistaCompra.getCardTablaAbonos().destruirTablaAbonos();
		vistaCompra.getCardResumenCompra().getAbajoCompraAbono().limpiarTxtElementosAbono();
		vistaCompra.getCardResumenCompra().getArribaCompraAbono().limpiarElementosAbono();
		vistaCompra.setAbonoAsociado(null);
		vistaCompra.setCicloAsociado(null);
		vistaTarjeta.setVisible(false);
		vistaCompra.showCardTablaAbonos();
	}
	
	
}
