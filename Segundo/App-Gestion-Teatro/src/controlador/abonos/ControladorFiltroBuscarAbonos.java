package controlador.abonos;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import vista.panelUsuarioLogueado.pestanaCompraAbono.PestanaCompraAbonoCenter;
import vista.panelUsuarioLogueado.pestanaCompraAbono.PestanaCompraAbonoWest;

/**
 * Clase para controlar el desplegable para seleccionar o abonos
 * o ciclos
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorFiltroBuscarAbonos implements ItemListener{

	private PestanaCompraAbonoWest vistaBoxFiltrarAbonos;
	private PestanaCompraAbonoCenter vistaTablaFiltrarAbonos;
	
	/**
	 * constructor del action listener encargado del 
	 * boton de confirmar una vez introducia la tarjeta
	 *
	 * @param vistaBoxFiltrarAbonos		panel donde se situa el desplegable
	 * @param vistaTablaFiltrarAbonos	tabla donde se encuentran las elecciones del desplegable	
	 */
	public ControladorFiltroBuscarAbonos (PestanaCompraAbonoWest vistaBoxFiltrarAbonos, PestanaCompraAbonoCenter vistaTablaFiltrarAbonos) {
		this.vistaBoxFiltrarAbonos = vistaBoxFiltrarAbonos;
		this.vistaTablaFiltrarAbonos = vistaTablaFiltrarAbonos;
	}

	/**
	 * controlador del evento que ocurre tras pinchar sobre el despleable
	 *
	 * @param e		evento de raton que ocurre tras pinchar sobre el despegable	
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (vistaBoxFiltrarAbonos.getCombobox().getSelectedItem().equals(e.getItem())) {
			vistaTablaFiltrarAbonos.getCardTablaAbonos().destruirTablaAbonos();
			if (e.getItem().equals("Abono Anual")) {
				vistaTablaFiltrarAbonos.getCardResumenCompra().getAbajoCompraAbono().limpiarTxtElementosAbono();
				vistaTablaFiltrarAbonos.getCardResumenCompra().getArribaCompraAbono().limpiarElementosAbono();
				vistaTablaFiltrarAbonos.showCardTablaAbonos();
				vistaTablaFiltrarAbonos.getCardTablaAbonos().actualizarTablaAbonos();
			}else {
				vistaTablaFiltrarAbonos.getCardResumenCompra().getAbajoCompraAbono().limpiarTxtElementosAbono();
				vistaTablaFiltrarAbonos.getCardResumenCompra().getArribaCompraAbono().limpiarElementosAbono();
				vistaTablaFiltrarAbonos.showCardTablaAbonos();
				vistaTablaFiltrarAbonos.getCardTablaAbonos().actualizarTablaCiclos();
			}
		}
	}	
}
