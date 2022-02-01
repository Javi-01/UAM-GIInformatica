package controlador.entrada;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

import modelo.sistema.Sistema;
import modelo.zona.Zona;
import modelo.zona.ZonaCompuesta;
import modelo.zona.ZonaSentado;
import vista.panelUsuarioLogueado.PanelUsuarioLogueado;
import vista.panelUsuarioLogueado.ventanaCompraEntradas.PestanaComprarEntrada;
import vista.panelUsuarioLogueado.ventanaCompraEntradas.seleccionInfo.CardSeleccionarZonaCompuestaEntrada;
import vista.panelUsuarioLogueado.ventanaCompraEntradas.seleccionInfo.CardSeleccionarZonaSimpleEntrada;

/**
 * Clase para controlar el evento de pinchar sobre una 
 * de las filas de la tabla de zonas de las que se 
 * quiere comprar una entrada de una representacion  
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorSeleccionarZona implements MouseListener {

	private PestanaComprarEntrada pestanaCompra;
	private CardSeleccionarZonaSimpleEntrada cardSeleccionarZona;
	private CardSeleccionarZonaCompuestaEntrada cardSeleccionarZonaCompuesta;

	public ControladorSeleccionarZona(PestanaComprarEntrada pestanaCompra,
			CardSeleccionarZonaSimpleEntrada cardSeleccionarZona,
			CardSeleccionarZonaCompuestaEntrada cardSeleccionarZonaCompuesta) {

		this.cardSeleccionarZonaCompuesta = cardSeleccionarZonaCompuesta;
		this.pestanaCompra = pestanaCompra;
		this.cardSeleccionarZona = cardSeleccionarZona;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		int zonaElegida = cardSeleccionarZona.getTablaZonas().rowAtPoint(e.getPoint());

		if (zonaElegida != -1) {
			String nombreZona = null;

			if (pestanaCompra.getZonaVistaEntrada() == null) {
				nombreZona = (String) cardSeleccionarZona.getTablaModelo().getValueAt(zonaElegida, 0);
			} else if (pestanaCompra.getZonaVistaEntrada().getClass().equals(ZonaCompuesta.class)) {
				nombreZona = (String) cardSeleccionarZonaCompuesta.getTablaModelo().getValueAt(zonaElegida, 0);
			} else {
				nombreZona = (String) cardSeleccionarZona.getTablaModelo().getValueAt(zonaElegida, 0);
			}

			Zona zona = Sistema.getInstance().sistemaBuscarZona(nombreZona);
			
			if (pestanaCompra.getRepresentacionVistaEntrada().getRepresentacionEntradasDisponiblesEnZona(zona).isEmpty()
					&& !zona.getClass().equals(ZonaCompuesta.class)) {
				pestanaCompra.getRepresentacionVistaEntrada()
						.getRepresentacionListaDeEsperaEnZona(zona)
						.addUsuariosEnEspera(Sistema.getInstance().getSistemaUsuarioLogeado());
				JOptionPane.showMessageDialog(pestanaCompra,
						"La zona no tiene aforo disponible, se le ha introducido a la lista "
								+ "de espera. Se le notificara en caso de que haya una cancelacion");
				return;
			}

			if (zona.getClass().equals(ZonaCompuesta.class)) {
				cardSeleccionarZonaCompuesta.destruirTablaZonasCompuestas();
				pestanaCompra.setZonaVistaEntrada(zona);
				cardSeleccionarZonaCompuesta
						.actualizarTablaZonasCompuestas(pestanaCompra.getRepresentacionVistaEntrada(), zona);
				pestanaCompra.showElegirZonaCompuesta();
			} else {
				if (zona.getClass().equals(ZonaSentado.class)) {
					pestanaCompra.setZonaVistaEntrada(zona);
					PanelUsuarioLogueado.getVentanaNumEntradasSentado().setVisible(true);
				} else {
					pestanaCompra.setZonaVistaEntrada(zona);
					PanelUsuarioLogueado.getVentanaNumEntradasPie().setVisible(true);
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
