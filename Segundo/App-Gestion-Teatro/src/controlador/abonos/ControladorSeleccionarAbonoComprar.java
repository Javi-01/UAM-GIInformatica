package controlador.abonos;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import modelo.abono.Abono;
import modelo.abono.Ciclo;
import modelo.sistema.Sistema;
import vista.panelUsuarioLogueado.pestanaCompraAbono.PestanaCompraAbonoCenter;
import vista.panelUsuarioLogueado.pestanaCompraAbono.PestanaCompraAbonoWest;

/**
 * Clase del controlador del listener cuando se pincha con el raton a un boton 
 * o fila de una tabla
 * 
 * @author Jaime Diaz, Javier Fraile, Ivan Fernandez
 */
public class ControladorSeleccionarAbonoComprar implements MouseListener{

	private PestanaCompraAbonoCenter vistaAbono;
	private PestanaCompraAbonoWest vistaBoxAbono;
	
	/**
	 * constructor del mouse listener tras pibnchar sobre el abono 
	 * de la tabla de abonos que se quiere comprar
	 *
	 * @param vistaAbono		panel del abono 
	 * @param vistaBoxAbono		panel del comboBox de abono	
	 */
	public ControladorSeleccionarAbonoComprar(PestanaCompraAbonoCenter vistaAbono, PestanaCompraAbonoWest vistaBoxAbono) {
		this.vistaAbono = vistaAbono;
		this.vistaBoxAbono = vistaBoxAbono;
	}

	/**
	 * controlador del evento que ocurre al pinchar sobre una fila de la tabla
	 *
	 * @param e		evento de raton que ocurre tras pinchar sobre una fila de la tabla	
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		
		int abonoElegido = vistaAbono.getCardTablaAbonos().getTablaAbonos().rowAtPoint(e.getPoint());
		
		if (abonoElegido != -1) {
			if (vistaBoxAbono.getCombobox().getSelectedItem().toString().equals("Abono Anual")) {
				
				Abono abono = Sistema.getInstance().getSistemaAbonos().get(abonoElegido);
				vistaAbono.getCardResumenCompra().getArribaCompraAbono().setTxtNombreAbono("Abono Anual "+ (abonoElegido +1));
				vistaAbono.getCardResumenCompra().getArribaCompraAbono().setTxtZonaAbono(abono.getAbonoZona().getZonaNombre());
				vistaAbono.getCardResumenCompra().getArribaCompraAbono().setTxtPrecioAbono(String.valueOf(abono.getAbonoPrecio()) + "$");
				vistaAbono.getCardResumenCompra().getAbajoCompraAbono().setTxtElementosAbono("Disponible para todos los eventos");
				
				vistaAbono.setAbonoAsociado(abono);
				
			}else {
				
				Ciclo ciclo = Sistema.getInstance().getSistemaCiclos().get(abonoElegido);
				vistaAbono.getCardResumenCompra().getArribaCompraAbono().setTxtNombreAbono(ciclo.getCicloNombre());
				vistaAbono.getCardResumenCompra().getArribaCompraAbono().setTxtZonaAbono(ciclo.getCicloZona().getZonaNombre());
				vistaAbono.getCardResumenCompra().getArribaCompraAbono().setTxtPrecioAbono(String.valueOf(ciclo.getPrecio()) + "$");
				vistaAbono.getCardResumenCompra().getAbajoCompraAbono().setTxtElementosAbono(
						(String)vistaAbono.getCardTablaAbonos().getTablaModeloAbonos().getValueAt(abonoElegido, 3));
				
				vistaAbono.setCicloAsociado(ciclo);
			}
		}
		
		vistaAbono.showCardResumenCompra();
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
