package controlador.entrada;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import modelo.entrada.Entrada;
import modelo.zona.ZonaSentado;
import vista.panelUsuarioLogueado.VentanaElegirCompraSentado;
import vista.panelUsuarioLogueado.VentanaNumeroDeEntradasSentado;
import vista.panelUsuarioLogueado.ventanaCompraEntradas.PestanaComprarEntrada;

/**
 * Clase para controlar la eleccion de butacas que el usuario
 * quiere comprar de una zona sentado  
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorTxtElegirEntradasSentado implements ActionListener{

	PestanaComprarEntrada compraEntrada;
	VentanaNumeroDeEntradasSentado numEntradas;
	VentanaElegirCompraSentado elegirSentado;
		
	public ControladorTxtElegirEntradasSentado(PestanaComprarEntrada compraEntrada,
			VentanaElegirCompraSentado elegirSentado, VentanaNumeroDeEntradasSentado numEntradas) {

		this.numEntradas = numEntradas;
		this.compraEntrada = compraEntrada;
		this.elegirSentado = elegirSentado;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		compraEntrada.getCardCarritoCompra().getTablaCarritoCompra().destruirTablaCompra();
		
		String txtEntradas[] = elegirSentado.getCardElegirEntradas().getTxtElegirSentado().split(" ");
		Integer num = numEntradas.getJSliderNumEntradas().getValue();
		if (txtEntradas.length > num) {
			JOptionPane.showMessageDialog(elegirSentado, "No puedes poner mas entradas de las que has escogido", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		ArrayList <Entrada> entradas = compraEntrada.getRepresentacionVistaEntrada().
				getRepresentacionEntradasEnZona(compraEntrada.getZonaVistaEntrada());
		
		ArrayList<Entrada> entradasAComprar = new ArrayList<>();
		Integer idEntrada = null;
		
		boolean encontrado = false;
		
		for (int k = 0 ; k < txtEntradas.length; k++) {
			for (Entrada ents : entradas) {
				if (ents.getCodigoValidacion() == Integer.parseInt(txtEntradas[k])) {
					encontrado = true;
					break;
				}	
			}
			if (encontrado == true) {
				encontrado = false;
			}else {
				JOptionPane.showMessageDialog(elegirSentado, "No puedes elegir una entrada que no sea de esa zona", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		for (int i = 0; i < txtEntradas.length; i++) {			
			for (Entrada ent : entradas) {
				try {
					idEntrada = Integer.parseInt(txtEntradas[i]);
					
					for (int j = i +1; j < txtEntradas.length; j++) {
						if (idEntrada == Integer.parseInt(txtEntradas[j])) {
							JOptionPane.showMessageDialog(elegirSentado, "No puedes comprar la misma entrada varias veces", "Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
				}catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(elegirSentado, "El valor que has introducido no es un numero", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (ent.getCodigoValidacion() == idEntrada) {
					if (ent.getEntradaHabilitada() && !ent.getEntradaOcupada()) {
						entradasAComprar.add(ent);
						break;
					}else {
						JOptionPane.showMessageDialog(elegirSentado, "Has escogido una entrada que no esta dispoinble", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			}
		}
		
		ArrayList <Entrada> entradasTarjeta = new ArrayList<>();
		ArrayList <Entrada> entradasAbono = new ArrayList<>();
		compraEntrada.getCardCarritoCompra().getTablaCarritoCompra().destruirTablaCompra();
		
		if (compraEntrada.getZonaVistaEntrada().getClass().equals(ZonaSentado.class)) {
			try {
				
				entradasTarjeta = compraEntrada.getRepresentacionVistaEntrada().representacionEntradaConAbono(entradasAComprar);
				
				for (Entrada ent : entradas) {
					if (!entradasAbono.contains(ent)) {
						entradasTarjeta.add(ent);
					}
				}
			} catch (NullPointerException e1) {
				JOptionPane.showMessageDialog(compraEntrada, "No se pueden comprar las entradas, entradas disponibles: " 
						+ compraEntrada.getRepresentacionVistaEntrada().getRepresentacionEntradasDisponiblesEnZona(compraEntrada.getZonaVistaEntrada()).size());
				return;
			}
			
			compraEntrada.getCardCarritoCompra().getTablaCarritoCompra().delEntradasCompraAbono();
			compraEntrada.getCardCarritoCompra().getTablaCarritoCompra().delEntradasCompraTarjeta();
			compraEntrada.getCardCarritoCompra().getTablaCarritoCompra().setEntradasCompraTarjeta(entradasTarjeta);
			compraEntrada.getCardCarritoCompra().getTablaCarritoCompra().setEntradasCompraAbono(entradasAbono);
			compraEntrada.getCardCarritoCompra().getTablaCarritoCompra().actualizarTablaCompra(entradasAComprar);
			elegirSentado.getCardElegirEntradas().limpiarEntradas();
			compraEntrada.getCardZonaEntrada().destruirTablaZonas();
			compraEntrada.getCardZonaCompuestaEntrada().destruirTablaZonasCompuestas();
			numEntradas.setVisible(false);
			elegirSentado.setVisible(false);
			compraEntrada.showCarritoCompra();	
		}
	}

}
