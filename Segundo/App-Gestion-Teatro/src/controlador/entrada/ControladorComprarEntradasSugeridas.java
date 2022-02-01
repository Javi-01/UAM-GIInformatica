package controlador.entrada;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import modelo.entrada.Entrada;
import modelo.zona.ZonaSentado;
import vista.panelUsuarioLogueado.VentanaElegirCompraSentado;
import vista.panelUsuarioLogueado.ventanaCompraEntradas.PestanaComprarEntrada;

/**
 * Clase para controlar el boton con el que se confirma
 * la sugerencia de entras y el número de entradas a comprar   
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorComprarEntradasSugeridas implements ActionListener{

	VentanaElegirCompraSentado entradasSugeridas;
	PestanaComprarEntrada pestanaCompra;
	
	ArrayList <Entrada> entradasTarjeta = new ArrayList<Entrada>();
	ArrayList <Entrada> entradasAbono = new ArrayList<Entrada>();
	
	public ControladorComprarEntradasSugeridas(VentanaElegirCompraSentado entradasSugeridas,
			PestanaComprarEntrada pestanaCompra) {
		
		this.entradasSugeridas = entradasSugeridas;
		this.pestanaCompra = pestanaCompra;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		pestanaCompra.getCardCarritoCompra().getTablaCarritoCompra().destruirTablaCompra();
		pestanaCompra.getCardZonaCompuestaEntrada().destruirTablaZonasCompuestas();
		pestanaCompra.getCardZonaEntrada().destruirTablaZonas();
		
		if (pestanaCompra.getZonaVistaEntrada().getClass().equals(ZonaSentado.class)) {
			ArrayList <Entrada> entradas = pestanaCompra.getCardCarritoCompra().getEntradasCompra();
			if (entradas.isEmpty()) {
				JOptionPane.showMessageDialog(pestanaCompra, "No has elegido sugerencia.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			entradasAbono = pestanaCompra.getRepresentacionVistaEntrada().representacionEntradaConAbono(entradas);
				
			for (Entrada ent : entradas) {
				if (!entradasAbono.contains(ent)) {
					entradasTarjeta.add(ent);
				}
			}
			
			pestanaCompra.getCardCarritoCompra().getTablaCarritoCompra().delEntradasCompraAbono();
			pestanaCompra.getCardCarritoCompra().getTablaCarritoCompra().delEntradasCompraTarjeta();
			pestanaCompra.getCardCarritoCompra().getTablaCarritoCompra().setEntradasCompraAbono(entradasAbono);
			pestanaCompra.getCardCarritoCompra().getTablaCarritoCompra().setEntradasCompraTarjeta(entradasTarjeta);
			pestanaCompra.getCardCarritoCompra().getTablaCarritoCompra().actualizarTablaCompra(entradasTarjeta);
			entradasSugeridas.showCardMenuSugerencias();
			entradasSugeridas.setVisible(false);
			pestanaCompra.showCarritoCompra();	
		}
	}
}
