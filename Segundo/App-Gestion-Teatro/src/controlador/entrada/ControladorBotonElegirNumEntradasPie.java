package controlador.entrada;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import modelo.entrada.Entrada;
import modelo.sistema.Sistema;
import modelo.zona.ZonaPie;
import vista.panelUsuarioLogueado.VentanaNumeroDeEntradasPie;
import vista.panelUsuarioLogueado.ventanaCompraEntradas.PestanaComprarEntrada;

/**
 * Clase para controlar el boton con el que se elige comprar entradas de pie  
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorBotonElegirNumEntradasPie implements ActionListener, ChangeListener{

	Integer sliderNum = (int) Math.floor(Sistema.getInstance().getNumMaxEntradas()/2) + 1;
	ArrayList <Entrada> entradasAComprar  = new ArrayList<Entrada>();
	ArrayList <Entrada> entradasAbono  = new ArrayList<Entrada>();
	ArrayList <Entrada> entradasTarjeta = new ArrayList<Entrada>();
	VentanaNumeroDeEntradasPie vistaNumEntradasPie;
	PestanaComprarEntrada pestanaCompra;
	
	public ControladorBotonElegirNumEntradasPie(PestanaComprarEntrada pestanaCompra, VentanaNumeroDeEntradasPie vistaNumEntradasPie) {
		
		this.vistaNumEntradasPie = vistaNumEntradasPie;
		this.pestanaCompra = pestanaCompra;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		pestanaCompra.getCardCarritoCompra().delEntradasCompra();		
		pestanaCompra.getCardCarritoCompra().getTablaCarritoCompra().destruirTablaCompra();
		
		if (pestanaCompra.getZonaVistaEntrada().getClass().equals(ZonaPie.class)) {
			entradasAComprar = pestanaCompra.getRepresentacionVistaEntrada().getRepresentacionEntradasSobreUnNumero(pestanaCompra.getZonaVistaEntrada(), sliderNum);
			try {
				entradasAbono = pestanaCompra.getRepresentacionVistaEntrada().representacionEntradaConAbono(entradasAComprar);
				
				for (Entrada ent : entradasAComprar) {
					if (!entradasAbono.contains(ent)) {
						entradasTarjeta.add(ent);
					}
				}

			}catch (NullPointerException e1) {
				pestanaCompra.getRepresentacionVistaEntrada().getRepresentacionListaDeEsperaEnZona(
						pestanaCompra.getZonaVistaEntrada()).addUsuariosEnEspera(Sistema.getInstance().getSistemaUsuarioLogeado());
				JOptionPane.showMessageDialog(vistaNumEntradasPie, "No se pueden comprar las entradas, entradas disponibles: " 
						+ pestanaCompra.getRepresentacionVistaEntrada().getRepresentacionEntradasDisponiblesEnZona(pestanaCompra.getZonaVistaEntrada()).size() + ""
								+ ". Se le ha añadido a la lista de espera");
				return;
			}
			
			pestanaCompra.getCardCarritoCompra().getTablaCarritoCompra().setEntradasCompraAbono(entradasAbono);
			pestanaCompra.getCardCarritoCompra().getTablaCarritoCompra().setEntradasCompraTarjeta(entradasTarjeta);
			pestanaCompra.getCardCarritoCompra().getTablaCarritoCompra().actualizarTablaCompra(entradasAComprar);
			vistaNumEntradasPie.setVisible(false);
			pestanaCompra.showCarritoCompra();	
		}
	}


	@Override
	public void stateChanged(ChangeEvent e) {
		 sliderNum = vistaNumEntradasPie.getJSliderNumEntradas().getValue();
	}
}
