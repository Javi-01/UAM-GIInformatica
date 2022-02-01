package controlador.entrada;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import modelo.entrada.Entrada;
import modelo.sistema.Sistema;
import modelo.zona.ZonaSentado;
import vista.panelUsuarioLogueado.VentanaElegirCompraSentado;
import vista.panelUsuarioLogueado.VentanaNumeroDeEntradasSentado;
import vista.panelUsuarioLogueado.ventanaCompraEntradas.VentanaCompraEntradasSugeridas;
import vista.panelUsuarioLogueado.ventanaCompraEntradas.PestanaComprarEntrada;

/**
 * Clase para controlar el jslider para elegir el número de entradas
 * de la zona sentado que se quieren comprar  
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorBotonElegirNumEntradasSentado implements ActionListener, ChangeListener, ItemListener{

	Integer sliderNum = (int) Math.floor(Sistema.getInstance().getNumMaxEntradas()/2) + 1;
	String preferencia = "Juntas";
	ArrayList <Entrada> entradasAComprar  = new ArrayList<Entrada>();
	
	VentanaNumeroDeEntradasSentado vistaNumEntradasSentado;
	VentanaElegirCompraSentado elegirOpcionSentado;
	PestanaComprarEntrada pestanaCompra;
	VentanaCompraEntradasSugeridas entradasSugeridas;
	
	public ControladorBotonElegirNumEntradasSentado(PestanaComprarEntrada pestanaCompra, VentanaNumeroDeEntradasSentado vistaNumEntradasSentado, 
			VentanaElegirCompraSentado elegirOpcionSentado) {
		
		this.vistaNumEntradasSentado = vistaNumEntradasSentado;
		this.pestanaCompra = pestanaCompra;
		this.elegirOpcionSentado = elegirOpcionSentado;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
			
		pestanaCompra.getCardCarritoCompra().getTablaCarritoCompra().destruirTablaCompra();
		pestanaCompra.getCardCarritoCompra().getTablaCarritoCompra().delEntradasCompraAbono();
		pestanaCompra.getCardCarritoCompra().getTablaCarritoCompra().delEntradasCompraTarjeta();
		
		if (pestanaCompra.getZonaVistaEntrada().getClass().equals(ZonaSentado.class)) {
			
			if (vistaNumEntradasSentado.getCbSugerirEntradas().getSelectedItem().toString().equals("Sin sugerencia")) {
				entradasAComprar = null;
				
			}else {
				if ((entradasAComprar = pestanaCompra.getRepresentacionVistaEntrada().
			
					sugerirRepresentacionEntradas(preferencia, 
							pestanaCompra.getZonaVistaEntrada(), sliderNum)) == null){
				JOptionPane.showMessageDialog(pestanaCompra, "La sugerencia no esta disponible", "Error", JOptionPane.ERROR_MESSAGE);
				return;
				}
			}
			
			pestanaCompra.getCardCarritoCompra().setEntradasCompra(entradasAComprar);
			
			entradasSugeridas = new VentanaCompraEntradasSugeridas
					(pestanaCompra.getZonaVistaEntrada(), pestanaCompra.getRepresentacionVistaEntrada(), entradasAComprar);
			
			vistaNumEntradasSentado.setVisible(false);
			entradasSugeridas.setVisible(true);
			elegirOpcionSentado.setVisible(true);
			
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		sliderNum = vistaNumEntradasSentado.getJSliderNumEntradas().getValue();
	}


	@Override
	public void itemStateChanged(ItemEvent e) {
			preferencia = e.getItem().toString();
	}

}
